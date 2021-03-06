package ru.boomearo.langhelper.versions;

import java.io.*;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractTranslateManager {

    private final String version;
    private ConcurrentMap<LangType, Translate> types;

    public AbstractTranslateManager(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void loadLanguages(File file, Collection<LangType> enabledLanguages) {
        ConcurrentMap<LangType, Translate> types = new ConcurrentHashMap<LangType, Translate>();
        try {
            ClassLoader classLoader = Bukkit.getServer().getClass().getClassLoader();

            InputStream stream = classLoader.getResourceAsStream("assets/minecraft/lang/" + LangType.EN_US.getName() + ".lang");
            if (stream == null) {
                stream = classLoader.getResourceAsStream("assets/minecraft/lang/" + LangType.EN_US.getName() + ".json");
                if (stream == null) {
                    throw new IllegalArgumentException("Не найден языковый файл по умолчанию!");
                }
            }

            ConcurrentMap<String, String> translates = parseTranslate(stream);
            if (translates != null) {
                types.put(LangType.EN_US, new Translate(LangType.EN_US, translates));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File folders = file;
            if (!folders.exists()) {
                folders.getParentFile().mkdirs();
            }

            if (folders.isDirectory()) {
                File ver = new File(folders, getVersion());
                if (ver.isDirectory()) {
                    File[] type = ver.listFiles();
                    if (type != null) {
                        for (File t : type) {
                            if (t.isFile()) {
                                LangType lt = null;
                                try {
                                    lt = LangType.valueOf(t.getName().toUpperCase());
                                }
                                catch (Exception e) {
                                }
                                if (lt != null) {
                                    if (lt.isExternal()) {
                                        if (enabledLanguages.contains(lt)) {
                                            InputStream stream = new FileInputStream(t);
                                            ConcurrentMap<String, String> translate = parseTranslate(stream);
                                            if (translate != null) {
                                                types.put(lt, new Translate(lt, translate));
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

    public Translate getTranslate(LangType type) {
        return this.types.get(type);
    }

    public String getTranslate(String name, LangType type) {
        Translate tr = this.types.get(type);
        if (tr != null) {
            return tr.getTranstale(name.toLowerCase().replace("_", ""));
        }
        return null;
    }

    public Collection<Translate> getAllTranslate() {
        return this.types.values();
    }

    public Set<LangType> getAllTranslateLang() {
        return this.types.keySet();
    }

    public abstract String getItemName(ItemStack item, LangType type);

    public abstract String getEntityName(EntityType entity, LangType type);

    public abstract String getEnchantName(Enchantment enchant, LangType type);

    public abstract String getEnchantLevelName(int level, LangType type);

    protected abstract ConcurrentMap<String, String> parseTranslate(InputStream stream);
}
