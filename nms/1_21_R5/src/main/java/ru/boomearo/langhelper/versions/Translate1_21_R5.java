package ru.boomearo.langhelper.versions;

import lombok.NonNull;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.managers.ConfigManager;

import java.util.Locale;
import java.util.logging.Level;

public class Translate1_21_R5 extends JsonTranslateManager {

    public Translate1_21_R5(Plugin plugin, ConfigManager configManager) {
        super("1.21.6", plugin, configManager);
    }

    @Override
    public String getItemName(@NonNull ItemStack item, @NonNull LangType langType) {
        try {
            net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.h();

            name = i.j();

            if (item.getType() != Material.AIR) {
                if (name.equals("block.minecraft.air")) {
                    return null;
                }
            }

            return getTranslate(name, langType);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get item name for " + item, e);
            return null;
        }
    }

    @Override
    public String getEntityName(@NonNull EntityType entityType, @NonNull LangType langType) {
        return getTranslate("entity.minecraft." + entityType.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantmentName(@NonNull Enchantment enchant, @NonNull LangType langType) {
        return getTranslate("enchantment.minecraft." + enchant.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantmentLevelName(int level, @NonNull LangType langType) {
        return getTranslate("enchantment.level." + level, langType);
    }

    @Override
    public String getPotionEffectName(@NonNull PotionEffectType potionEffectType, @NonNull LangType langType) {
        return getTranslate(potionEffectType.getTranslationKey(), langType);
    }

    @Override
    public String getBiomeName(@NonNull Biome biome, @NonNull LangType langType) {
        return getTranslate("biome.minecraft." + biome.name().toLowerCase(Locale.ROOT), langType);
    }
}
