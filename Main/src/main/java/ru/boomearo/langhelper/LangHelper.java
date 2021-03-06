package ru.boomearo.langhelper;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.utils.JsonUtils;
import ru.boomearo.langhelper.versions.*;
import ru.boomearo.langhelper.versions.exceptions.LangException;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;
import ru.boomearo.langhelper.versions.exceptions.LangVersionException;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;

    private static LangHelper instance = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private static final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2.class,
            Translate1_14_R1.class,
            Translate1_15_R1.class,
            Translate1_16_R3.class,
            Translate1_17_R1.class
    );

    private static final String TRANSLATION_FILE_URL = "http://resources.download.minecraft.net/%s/%s";
    private static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private List<LangType> enabledLanguages = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        try {
            File configFile = new File(getDataFolder() + File.separator + "config.yml");
            if (!configFile.exists()) {
                getLogger().info("Конфиг не найден, создаю новый...");
                saveDefaultConfig();
            }

            //Загружаем информацию с конфига
            loadConfigData();

            //Вычисляем версию сервера и создаем соответствующий экземпляр
            this.version = matchVersion();

            //Проверяем, на месте ли включенные языки
            checkAndDownloadLanguages();

            //Подгружаем языки с диска
            this.version.loadLanguages(getLanguageFolder(), this.enabledLanguages);

            //Просто оповещаем о том, сколько строк и какие языки были загружены
            for (Translate tra : this.version.getAllTranslate()) {
                String languageName = tra.getTranstale("language.name");
                if (languageName == null) {
                    languageName = "Unknown-name";
                }
                String languageRegion = tra.getTranstale("language.region");
                if (languageRegion == null) {
                    languageRegion = "Unknown-region";
                }
                this.getLogger().info("Язык '" + tra.getLangType().getName() + " [" + languageName + "-" + languageRegion + "]' успешно загружен. Количество строк: " + tra.getAllTranslate().size());
            }
        }
        catch (LangParseException e) {
            this.getLogger().warning("Ошибка при получении языков от mojang: " + e.getMessage());
        }
        catch (LangVersionException e) {
            this.getLogger().warning("Ошибка при получении версии сервера: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getCommand("langhelper").setExecutor(new CmdExecutorLangHelper());

        this.getLogger().info("Плагин успешно запущен!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Плагин успешно выключен!");
    }

    public void loadConfigData() {
        reloadConfig();

        List<LangType> tmpEnabledLanguages = new ArrayList<>();
        List<String> configLangs = this.getConfig().getStringList("enabledLanguages");
        if (configLangs != null) {
            for (String t : configLangs) {
                LangType parsedType = null;
                try {
                    parsedType = LangType.valueOf(t.toUpperCase());
                }
                catch (Exception e) {}
                if (parsedType == null) {
                    continue;
                }

                this.getLogger().info("Используем язык: " + parsedType.getName());
                tmpEnabledLanguages.add(parsedType);
            }
        }

        this.enabledLanguages = Collections.unmodifiableList(tmpEnabledLanguages);
    }

    // Метод проверяет и скачивает с серверов mojang нужный язык для нужной версии.
    // Сам бы я не узнал как именно скачивать языки. Спасибо автору который реализовал утилиту: https://gist.github.com/Mystiflow/c2b8838688e3215bb5492041046e458e
    public void checkAndDownloadLanguages() throws LangParseException {
        //Получаем обьект MANIFEST который имеет список всех существующих версий игры
        JSONObject jsonManifest = JsonUtils.connectNormal(VERSION_MANIFEST_URL);
        if (jsonManifest == null) {
            throw new LangParseException("Не удалось спарсить json обьект для MANIFEST");
        }

        //Далее ниже все что мы делаем, это убеждаемся что он существует а потом извлекаем из него ссылку на ресурс этой версии
        JSONArray versionArray = JsonUtils.getJsonArrayObject(jsonManifest.get("versions"));
        if (versionArray == null) {
            throw new LangParseException("Не удалось спарсить json array обьект для MANIFEST");
        }

        String versionUrl = null;

        for (Object arrO : versionArray) {
            JSONObject versionJson = JsonUtils.getJsonObject(arrO);
            if (versionJson == null) {
                continue;
            }

            String versionId = JsonUtils.getStringObject(versionJson.get("id"));
            if (versionId == null) {
                continue;
            }

            //Ищем только ту версию которая сейчас
            if (versionId.equals(this.version.getVersion())) {
                versionUrl = JsonUtils.getStringObject(versionJson.get("url"));
                break;
            }
        }

        if (versionUrl == null) {
            throw new LangParseException("Не удалось найти версию " + this.version.getVersion() + " в json обьекте MANIFEST");
        }

        //Получив успешно обьект ресурсов этой версии, ищем далее в ней требуемый язык
        JSONObject versionJson = JsonUtils.connectNormal(versionUrl);
        if (versionJson == null) {
            throw new LangParseException("Не удалось спарсить json обьект по ссылке '" + versionUrl + "'");
        }

        JSONObject jsonAssertIndex = JsonUtils.getJsonObject(versionJson.get("assetIndex"));
        if (jsonAssertIndex == null) {
            throw new LangParseException("Не удалось спарсить json обьект assetIndex");
        }

        String langUrl = JsonUtils.getStringObject(jsonAssertIndex.get("url"));
        if (langUrl == null) {
            throw new LangParseException("Не удалось найти url строку в assetIndex");
        }

        JSONObject jsonLang = JsonUtils.connectNormal(langUrl);
        if (jsonLang == null) {
            throw new LangParseException("Не удалось спарсить json обьект по ссылке '" + langUrl + "'");
        }

        JSONObject langObjects = JsonUtils.getJsonObject(jsonLang.get("objects"));
        if (langObjects == null) {
            throw new LangParseException("Не удалось найти objects обьект");
        }

        File currentTranFolder = new File(this.getDataFolder(), "languages" + File.separator + this.version.getVersion() + File.separator);

        //Пытаемся скачать для каждого языка файл
        for (LangType lt : this.enabledLanguages) {
            File langFile = new File(currentTranFolder, lt.name());
            //Скачиваем только тот язык, которого нет
            if (langFile.exists()) {
                continue;
            }

            //Все языки ниже 1.13 не имеют формата json, поэтому учитываем это ниже.
            JSONObject langJsonData = JsonUtils.getJsonObject(langObjects.get("minecraft/lang/" + lt.getName() + ".json"));
            if (langJsonData == null) {
                langJsonData = JsonUtils.getJsonObject(langObjects.get("minecraft/lang/" + lt.getName() + ".lang"));
            }

            if (langJsonData == null) {
                continue;
            }

            //Получаем наконец то хэш, который используется для скачивания языка
            String hash = JsonUtils.getStringObject(langJsonData.get("hash"));
            if (hash == null) {
                continue;
            }

            try {
                //Пытаемся скачать язык, используя хэш
                URL url = new URL(String.format(TRANSLATION_FILE_URL, hash.substring(0, 2), hash));
                try (InputStream stream = url.openStream()) {
                    String pat = currentTranFolder.getAbsolutePath() + File.separator + lt.name();
                    //Странно что методу copy требуется чтобы директория существовала..
                    File tmp = new File(pat);
                    tmp.getParentFile().mkdirs();

                    Path outputPath = Paths.get(pat);
                    Files.copy(stream, outputPath);
                    this.getLogger().info("Скачан язык " + lt.getName() + " для версии " + this.version.getVersion());
                }
            }
            catch (Exception e) {
                this.getLogger().severe("Не удалось скачать язык " + lt.getName() + " для версии " + this.version.getVersion());
                e.printStackTrace();
            }
        }
    }

    public AbstractTranslateManager getAbstractTranslateManager() {
        return this.version;
    }

    private static String capitalize(String name) {
        String material = name.replace("_", " ").toLowerCase();

        StringBuilder sb = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = material.split(" ")).length;
        for (int i = 0; i < j; i++) {
            String word = arrayOfString[i];
            sb.append(" ");
            if (i == 0) {
                sb.append(word.toUpperCase().charAt(0));
                sb.append(word.substring(1));
            }
            else {
                sb.append(word.substring(0));
            }
        }
        return sb.toString().substring(1);
    }

    public String getEnchantmentName(Enchantment enc, LangType lang) {
        String name = this.version.getEnchantName(enc, lang);
        if (name == null) {
            name = this.version.getEnchantName(enc, LangType.EN_US);
            if (name == null) {
                name = capitalize(enc.getName());
            }
        }
        return name;
    }

    public String getItemTranslate(ItemStack item, LangType lang) {
        String name = this.version.getItemName(item, lang);
        if (name == null) {
            name = this.version.getItemName(item, LangType.EN_US);
            if (name == null) {
                short dur = item.getDurability();
                name = capitalize(item.getType().name()) + (dur > 0 ? ":" + dur : "");
            }
        }
        return name;
    }

    public String getEnchantLevelTranslate(int level, LangType lang) {
        String name = this.version.getEnchantLevelName(level, lang);
        if (name == null) {
            name = this.version.getEnchantLevelName(level, LangType.EN_US);
            if (name == null) {
                name = "" + level;
            }
        }
        return name;
    }

    public String getEntityTranslate(EntityType entity, LangType lang) {
        String name = this.version.getEntityName(entity, lang);
        if (name == null) {
            name = this.version.getEntityName(entity, LangType.EN_US);
            if (name == null) {
                name = capitalize(entity.name());
            }
        }
        return name;
    }

    public List<LangType> getEnabledLanguages() {
        return this.enabledLanguages;
    }

    private AbstractTranslateManager matchVersion() throws LangVersionException {
        try {
            return versions.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new LangException("Плагин не поддерживает данную версию сервера!")).
                            getConstructor().
                            newInstance();
        }
        catch (Exception e) {
            //Вызываем новое но свое
            throw new LangVersionException(e.getMessage());
        }
    }

    public static LangHelper getInstance() {
        return instance;
    }

    public static File getLanguageFolder() {
        return new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
    }

}
