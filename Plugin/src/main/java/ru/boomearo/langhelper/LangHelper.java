package ru.boomearo.langhelper;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.potion.PotionEffectType;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.AbstractTranslateManager;
import ru.boomearo.langhelper.versions.LangType;
import ru.boomearo.langhelper.versions.Translate;
import ru.boomearo.langhelper.versions.Translate1_12_R1;
import ru.boomearo.langhelper.versions.Translate1_13_R2;
import ru.boomearo.langhelper.versions.Translate1_14_R1;
import ru.boomearo.langhelper.versions.Translate1_15_R1;
import ru.boomearo.langhelper.versions.Translate1_16_R3;
import ru.boomearo.langhelper.versions.Translate1_17_R1;
import ru.boomearo.langhelper.versions.Translate1_18_R2;
import ru.boomearo.langhelper.versions.cached.UrlManifestManager;
import ru.boomearo.langhelper.versions.exceptions.LangException;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;
import ru.boomearo.langhelper.versions.exceptions.LangVersionException;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;

    private final UrlManifestManager manifestManager = new UrlManifestManager();

    private static LangHelper instance = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private static final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2.class,
            Translate1_14_R1.class,
            Translate1_15_R1.class,
            Translate1_16_R3.class,
            Translate1_17_R1.class,
            Translate1_18_R2.class
    );

    private static final String TRANSLATION_FILE_URL = "http://resources.download.minecraft.net/%s/%s";

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
                String languageName = tra.getTranslate("language.name");
                if (languageName == null) {
                    languageName = "Unknown-name";
                }
                String languageRegion = tra.getTranslate("language.region");
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
                catch (Exception ignored) {
                }
                if (parsedType == null) {
                    continue;
                }

                this.getLogger().info("Используем язык: " + parsedType.getName());
                tmpEnabledLanguages.add(parsedType);
            }
        }

        this.enabledLanguages = Collections.unmodifiableList(tmpEnabledLanguages);
    }

    /**
     * Метод проверяет и скачивает с серверов mojang нужный язык для нужной версии.
     * Сам бы я не узнал как именно скачивать языки. Спасибо автору который реализовал утилиту: https://gist.github.com/Mystiflow/c2b8838688e3215bb5492041046e458e
     **/
    public void checkAndDownloadLanguages() throws LangParseException {
        File currentTranFolder = new File(this.getDataFolder(), "languages" + File.separator + this.version.getVersion() + File.separator);

        for (LangType lt : this.enabledLanguages) {
            //Убеждаемся что файл языка существует.
            //Нам на самом деле не важно, пустой или модифицирован, главное, что он есть.
            File langFile = new File(currentTranFolder, lt.name());
            if (langFile.exists()) {
                continue;
            }

            //Пытаемся получить хэш для скачивания этого языка
            String hash = this.manifestManager.getLanguageHash(this.version.getVersion(), lt.name().toLowerCase());

            try {
                //Пытаемся скачать язык, используя хэш.
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

    /**
     * @return Перевод зачарования. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргументы entity или language являются null
     */
    public String getEnchantmentName(Enchantment entity, LangType language) {
        if (entity == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getEnchantName(entity, language);
        if (name == null) {
            name = this.version.getEnchantName(entity, LangType.EN_US);
            if (name == null) {
                name = capitalize(entity.getName());
            }
        }
        return name;
    }

    /**
     * @return Перевод предмета. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргументы item или language являются null
     */
    public String getItemTranslate(ItemStack item, LangType language) {
        if (item == null) {
            throw new IllegalArgumentException("item является null!");
        }
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getItemName(item, language);
        if (name == null) {
            name = this.version.getItemName(item, LangType.EN_US);
            if (name == null) {
                short dur = item.getDurability();
                name = capitalize(item.getType().name()) + (dur > 0 ? ":" + dur : "");
            }
        }
        return name;
    }

    /**
     * @return Перевод уровня зачарования. Обычно во всех переводах одинаковый. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргумент language являются null
     */
    public String getEnchantLevelTranslate(int level, LangType language) {
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getEnchantLevelName(level, language);
        if (name == null) {
            name = this.version.getEnchantLevelName(level, LangType.EN_US);
            if (name == null) {
                name = "" + level;
            }
        }
        return name;
    }

    /**
     * @return Перевод сущности. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргументы entity или language являются null
     */
    public String getEntityTranslate(EntityType entity, LangType language) {
        if (entity == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getEntityName(entity, language);
        if (name == null) {
            name = this.version.getEntityName(entity, LangType.EN_US);
            if (name == null) {
                name = capitalize(entity.name());
            }
        }
        return name;
    }

    /**
     * @return Перевод типа зелья. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргументы effect или language являются null
     */
    public String getPotionEffectTranslate(PotionEffectType effect, LangType language) {
        if (effect == null) {
            throw new IllegalArgumentException("effect является null!");
        }
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getPotionEffectName(effect, language);
        if (name == null) {
            name = this.version.getPotionEffectName(effect, LangType.EN_US);
            if (name == null) {
                name = capitalize(effect.getName());
            }
        }
        return name;
    }

    /**
     * @return Перевод биома. Если перевода не оказалось, возвращает перевод по умолчанию.
     * @throws IllegalStateException если аргументы biome или language являются null
     */
    public String getBiomeTranslate(Biome biome, LangType language) {
        if (biome == null) {
            throw new IllegalArgumentException("biome является null!");
        }
        if (language == null) {
            throw new IllegalArgumentException("language является null!");
        }
        String name = this.version.getBiomeName(biome, language);
        if (name == null) {
            name = this.version.getBiomeName(biome, LangType.EN_US);
            if (name == null) {
                name = capitalize(biome.name());
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
            //Вызываем новое, но свое
            throw new LangVersionException(e.getMessage());
        }
    }

    public static LangHelper getInstance() {
        return instance;
    }

    public static File getLanguageFolder() {
        return new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
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

}
