package ru.boomearo.langhelper.versions;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.cached.UrlManifestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

/**
 * Абстракция, представляет версию игры со всеми видами переводов
 */
@RequiredArgsConstructor
public abstract class DefaultTranslateManager implements TranslateManager {

    private static final String TRANSLATION_FILE_URL = "https://resources.download.minecraft.net/%s/%s";

    private final UrlManifestManager urlManifestManager = new UrlManifestManager();

    @Getter
    protected final String version;
    protected final Plugin plugin;
    protected final ConfigManager configManager;

    protected Map<LangType, TranslatedMessages> types;

    private final Set<LangType> registeredLanguages = new HashSet<>();

    /**
     * Загружает языки из файла в менеджере, учитывая включенные языки.
     */
    private void loadLanguages() {
        Map<LangType, TranslatedMessages> types = new HashMap<>();
        try {
            InputStream stream = getFileInputStream();

            Map<String, String> translates = parseTranslate(stream);
            if (translates != null) {
                types.put(LangType.EN_US, new TranslatedMessages(LangType.EN_US, translates));
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load default languages", e);
        }

        File file = new File(this.plugin.getDataFolder(), "languages" + File.separator);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }

            Set<LangType> languages = new HashSet<>(this.configManager.getEnabledLanguages());
            languages.addAll(this.registeredLanguages);

            if (file.isDirectory()) {
                File ver = new File(file, getVersion());
                if (ver.isDirectory()) {
                    File[] type = ver.listFiles();
                    if (type != null) {
                        for (File t : type) {
                            if (!t.isFile()) {
                                continue;
                            }

                            LangType lt = null;
                            try {
                                lt = LangType.valueOf(t.getName().toUpperCase(Locale.ROOT));
                            } catch (Exception ignored) {
                            }

                            if (lt == null) {
                                continue;
                            }

                            if (!lt.isExternal()) {
                                continue;
                            }

                            if (!languages.contains(lt)) {
                                continue;
                            }

                            InputStream stream = new FileInputStream(t);
                            Map<String, String> translate = parseTranslate(stream);

                            if (translate == null) {
                                continue;
                            }

                            types.put(lt, new TranslatedMessages(lt, translate));
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load languages", e);
        }

        this.types = Collections.unmodifiableMap(types);
    }

    public Set<LangType> getAllTranslateLang() {
        return this.types.keySet();
    }

    /**
     * Метод проверяет и скачивает с серверов mojang нужный язык для нужной версии.
     * Сам бы я не узнал как именно скачивать языки. Спасибо автору который реализовал утилиту: https://gist.github.com/Mystiflow/c2b8838688e3215bb5492041046e458e
     **/
    private void checkAndDownloadLanguages() {
        File currentTranFolder = new File(this.plugin.getDataFolder(), "languages" + File.separator + this.version + File.separator);

        Set<LangType> languages = new HashSet<>(this.configManager.getEnabledLanguages());
        languages.addAll(this.registeredLanguages);

        for (LangType lt : languages) {
            if (!lt.isExternal()) {
                continue;
            }

            // Убеждаемся что файл языка существует.
            // Нам на самом деле не важно, пустой или модифицирован, главное, что он есть.
            File langFile = new File(currentTranFolder, lt.name());
            if (langFile.exists()) {
                continue;
            }

            try {
                // Пытаемся получить хэш для скачивания этого языка
                String hash = this.urlManifestManager.getLanguageHash(this.version, lt.name().toLowerCase(Locale.ROOT));

                // Пытаемся скачать язык, используя хэш.
                URL url = new URL(String.format(TRANSLATION_FILE_URL, hash.substring(0, 2), hash));
                try (InputStream stream = url.openStream()) {
                    String pat = currentTranFolder.getAbsolutePath() + File.separator + lt.name();
                    // Странно что методу copy требуется чтобы директория существовала...
                    File tmp = new File(pat);
                    tmp.getParentFile().mkdirs();

                    Path outputPath = Paths.get(pat);
                    Files.copy(stream, outputPath);
                    this.plugin.getLogger().info("Language " + lt.name() + " successfully downloaded for version " + this.version);
                }
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to download language " + lt.name() + " for " + this.version, e);
            }
        }
    }

    @Override
    public void reloadLanguages() {
        checkAndDownloadLanguages();
        loadLanguages();
    }

    @Override
    public void registerLanguageType(LangType langType) {
        this.registeredLanguages.add(langType);
        this.plugin.getLogger().log(Level.INFO, "Registering language " + langType.name());
    }

    @Override
    public void unregisterLanguageType(LangType langType) {
        this.registeredLanguages.remove(langType);
        this.plugin.getLogger().log(Level.INFO, "Unregistering language " + langType.name());
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
            return tr.getTranslate(key.toLowerCase(Locale.ROOT).replace("_", ""));
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
    protected abstract Map<String, String> parseTranslate(InputStream stream);

    private static String capitalize(String name) {
        String material = name.replace("_", " ").toLowerCase(Locale.ROOT);

        StringBuilder sb = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = material.split(" ")).length;
        for (int i = 0; i < j; i++) {
            String word = arrayOfString[i];
            sb.append(" ");
            if (i == 0) {
                sb.append(word.toUpperCase(Locale.ROOT).charAt(0));
                sb.append(word.substring(1));
            } else {
                sb.append(word);
            }
        }
        return sb.substring(1);
    }

    private static InputStream getFileInputStream() {
        ClassLoader classLoader = Bukkit.getServer().getClass().getClassLoader();

        String defaultEnPath = "assets/minecraft/lang/" + LangType.EN_US.name().toLowerCase(Locale.ROOT);

        InputStream stream = classLoader.getResourceAsStream(defaultEnPath + ".lang");
        if (stream == null) {
            stream = classLoader.getResourceAsStream(defaultEnPath + ".json");
            if (stream == null) {
                throw new IllegalArgumentException("Default language file is not found!");
            }
        }
        return stream;
    }
}
