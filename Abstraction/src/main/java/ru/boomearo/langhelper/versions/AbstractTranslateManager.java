package ru.boomearo.langhelper.versions;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
        this.types = loadTranslateFromDisk(file, enabledLanguages);
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

    protected abstract ConcurrentMap<LangType, Translate> loadTranslateFromDisk(File file, Collection<LangType> enabledLanguages);
}
