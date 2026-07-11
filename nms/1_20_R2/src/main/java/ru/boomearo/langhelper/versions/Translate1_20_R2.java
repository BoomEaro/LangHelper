package ru.boomearo.langhelper.versions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemLingeringPotion;
import net.minecraft.world.item.ItemPotion;
import net.minecraft.world.item.ItemSplashPotion;
import net.minecraft.world.item.alchemy.PotionUtil;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.managers.ConfigManager;

import java.util.Locale;
import java.util.logging.Level;

public class Translate1_20_R2 extends JsonTranslateManager {

    public Translate1_20_R2(Plugin plugin, ConfigManager configManager) {
        super("1.20.2", plugin, configManager);
    }

    @Override
    public String getItemName(@NonNull ItemStack item, @NonNull LangType langType) {
        try {
            net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.d();
            if (i instanceof ItemSplashPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("splash_potion.effect.");
            } else if (i instanceof ItemLingeringPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("lingering_potion.effect.");
            } else if (i instanceof ItemPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("potion.effect.");
            } else {
                name = i.a();
            }

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
        String effectName = switch (potionEffectType.getName().toLowerCase(Locale.ROOT)) {
            case "fast_digging" -> "haste";
            case "harm" -> "instant_damage";
            case "heal" -> "instant_health";
            case "jump" -> "jump_boost";
            case "slow_digging" -> "mining_fatigue";
            case "confusion" -> "nausea";
            case "damage_resistance" -> "resistance";
            case "slow" -> "slowness";
            case "increase_damage" -> "strength";
            default -> potionEffectType.getName().toLowerCase(Locale.ROOT);
        };
        String name = "effect.minecraft." + effectName;
        return getTranslate(name, langType);
    }

    @Override
    public String getBiomeName(@NonNull Biome biome, @NonNull LangType langType) {
        return getTranslate("biome.minecraft." + biome.name().toLowerCase(Locale.ROOT), langType);
    }
}
