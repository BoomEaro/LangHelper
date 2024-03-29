package ru.boomearo.langhelper.versions;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.versions.cached.UrlManifestManager;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;

/**
 * Абстракция, представляет версию игры со всеми видами переводов
 */
public abstract class DefaultTranslateManager implements TranslateManager {

    private final UrlManifestManager urlManifestManager = new UrlManifestManager();
    private final String version;
    private final JavaPlugin javaPlugin;
    private ConcurrentMap<LangType, TranslatedMessages> types;

    private List<LangType> enabledLanguages = new ArrayList<>();

    private static final String TRANSLATION_FILE_URL = "https://resources.download.minecraft.net/%s/%s";

    public DefaultTranslateManager(String version, JavaPlugin javaPlugin) {
        this.version = version;
        this.javaPlugin = javaPlugin;
    }

    /**
     * @return версия игры
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Загружает языки из файла в менеджере, учитывая включенные языки.
     */
    public void loadLanguages() {
        ConcurrentMap<LangType, TranslatedMessages> types = new ConcurrentHashMap<>();
        try {
            ClassLoader classLoader = Bukkit.getServer().getClass().getClassLoader();

            String defaultEnPath = "assets/minecraft/lang/" + LangType.EN_US.getName();

            InputStream stream = classLoader.getResourceAsStream(defaultEnPath + ".lang");
            if (stream == null) {
                stream = classLoader.getResourceAsStream(defaultEnPath + ".json");
                if (stream == null) {
                    throw new IllegalArgumentException("Не найден языковый файл по умолчанию!");
                }
            }

            ConcurrentMap<String, String> translates = parseTranslate(stream);
            if (translates != null) {
                types.put(LangType.EN_US, new TranslatedMessages(LangType.EN_US, translates));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(this.javaPlugin.getDataFolder(), "languages" + File.separator);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            if (file.isDirectory()) {
                File ver = new File(file, getVersion());
                if (ver.isDirectory()) {
                    File[] type = ver.listFiles();
                    if (type != null) {
                        for (File t : type) {
                            if (t.isFile()) {
                                LangType lt = null;
                                try {
                                    lt = LangType.valueOf(t.getName().toUpperCase());
                                }
                                catch (Exception ignored) {
                                }
                                if (lt != null) {
                                    if (lt.isExternal()) {
                                        if (this.enabledLanguages.contains(lt)) {
                                            InputStream stream = new FileInputStream(t);
                                            ConcurrentMap<String, String> translate = parseTranslate(stream);
                                            if (translate != null) {
                                                types.put(lt, new TranslatedMessages(lt, translate));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.types = types;
    }

    public Set<LangType> getAllTranslateLang() {
        return this.types.keySet();
    }

    /**
     * Метод проверяет и скачивает с серверов mojang нужный язык для нужной версии.
     * Сам бы я не узнал как именно скачивать языки. Спасибо автору который реализовал утилиту: https://gist.github.com/Mystiflow/c2b8838688e3215bb5492041046e458e
     **/
    public void checkAndDownloadLanguages() throws LangParseException {
        File currentTranFolder = new File(this.javaPlugin.getDataFolder(), "languages" + File.separator + this.version + File.separator);

        for (LangType lt : this.enabledLanguages) {
            //Убеждаемся что файл языка существует.
            //Нам на самом деле не важно, пустой или модифицирован, главное, что он есть.
            File langFile = new File(currentTranFolder, lt.name());
            if (langFile.exists()) {
                continue;
            }

            //Пытаемся получить хэш для скачивания этого языка
            String hash = this.urlManifestManager.getLanguageHash(this.version, lt.name().toLowerCase());

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
                    this.javaPlugin.getLogger().info("Скачан язык " + lt.getName() + " для версии " + this.version);
                }
            }
            catch (Exception e) {
                this.javaPlugin.getLogger().severe("Не удалось скачать язык " + lt.getName() + " для версии " + this.version);
                e.printStackTrace();
            }
        }
    }

    public void loadConfigData() {
        this.javaPlugin.reloadConfig();

        List<LangType> tmpEnabledLanguages = new ArrayList<>();
        List<String> configLangs = this.javaPlugin.getConfig().getStringList("enabledLanguages");
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

                this.javaPlugin.getLogger().info("Используем язык: " + parsedType.getName());
                tmpEnabledLanguages.add(parsedType);
            }
        }

        this.enabledLanguages = Collections.unmodifiableList(tmpEnabledLanguages);
    }

    @Override
    public Collection<TranslatedMessages> getAllTranslate() {
        return this.types.values();
    }

    @Override
    public TranslatedMessages getTranslate(LangType type) {
        return this.types.get(type);
    }

    @Override
    public String getTranslate(String key, LangType langType) {
        TranslatedMessages tr = this.types.get(langType);
        if (tr != null) {
            return tr.getTranslate(key.toLowerCase().replace("_", ""));
        }
        return null;
    }

    @Override
    public String getItemNameSafe(ItemStack itemStack, LangType langType) {
        Preconditions.checkArgument(itemStack != null);
        Preconditions.checkArgument(langType != null);

        String name = getItemName(itemStack, langType);
        if (name == null) {
            name = getItemName(itemStack, LangType.EN_US);
            if (name == null) {
                short dur = itemStack.getDurability();
                name = capitalize(itemStack.getType().name()) + (dur > 0 ? ":" + dur : "");
            }
        }
        return name;
    }

    @Override
    public String getEntityNameSafe(EntityType entityType, LangType langType) {
        Preconditions.checkArgument(entityType != null);
        Preconditions.checkArgument(langType != null);

        String name = getEntityName(entityType, langType);
        if (name == null) {
            name = getEntityName(entityType, LangType.EN_US);
            if (name == null) {
                name = capitalize(entityType.name());
            }
        }
        return name;
    }

    @Override
    public String getEnchantmentNameSafe(Enchantment enchant, LangType langType) {
        Preconditions.checkArgument(enchant != null);
        Preconditions.checkArgument(langType != null);

        String name = getEnchantmentName(enchant, langType);
        if (name == null) {
            name = getEnchantmentName(enchant, LangType.EN_US);
            if (name == null) {
                name = capitalize(enchant.getName());
            }
        }
        return name;
    }

    @Override
    public String getEnchantmentLevelNameSafe(int level, LangType langType) {
        Preconditions.checkArgument(langType != null);

        String name = getEnchantmentLevelName(level, langType);
        if (name == null) {
            name = getEnchantmentLevelName(level, LangType.EN_US);
            if (name == null) {
                name = "" + level;
            }
        }
        return name;
    }

    @Override
    public String getPotionEffectNameSafe(PotionEffectType potionEffectType, LangType langType) {
        Preconditions.checkArgument(potionEffectType != null);
        Preconditions.checkArgument(langType != null);

        String name = getPotionEffectName(potionEffectType, langType);
        if (name == null) {
            name = getPotionEffectName(potionEffectType, LangType.EN_US);
            if (name == null) {
                name = capitalize(potionEffectType.getName());
            }
        }
        return name;
    }

    @Override
    public String getBiomeNameSafe(Biome biome, LangType langType) {
        Preconditions.checkArgument(biome != null);
        Preconditions.checkArgument(langType != null);

        String name = getBiomeName(biome, langType);
        if (name == null) {
            name = getBiomeName(biome, LangType.EN_US);
            if (name == null) {
                name = capitalize(biome.name());
            }
        }
        return name;
    }

    @Override
    public abstract String getItemName(ItemStack itemStack, LangType langType);

    @Override
    public abstract String getEntityName(EntityType entityType, LangType langType);

    @Override
    public abstract String getEnchantmentName(Enchantment enchant, LangType langType);

    @Override
    public abstract String getEnchantmentLevelName(int level, LangType langType);

    @Override
    public abstract String getPotionEffectName(PotionEffectType potionEffectType, LangType langType);

    @Override
    public abstract String getBiomeName(Biome biome, LangType langType);

    /**
     * Парсер строк, использующий InputStream
     */
    protected abstract ConcurrentMap<String, String> parseTranslate(InputStream stream);

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
