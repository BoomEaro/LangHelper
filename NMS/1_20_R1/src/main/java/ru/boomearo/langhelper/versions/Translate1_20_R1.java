package ru.boomearo.langhelper.versions;

import com.google.common.base.Preconditions;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemLingeringPotion;
import net.minecraft.world.item.ItemPotion;
import net.minecraft.world.item.ItemSplashPotion;
import net.minecraft.world.item.alchemy.PotionUtil;

public class Translate1_20_R1 extends JsonTranslateManager {

    public Translate1_20_R1(JavaPlugin javaPlugin) {
        super("1.20.1", javaPlugin);
    }

    @Override
    public String getItemName(ItemStack item, LangType langType) {
        Preconditions.checkArgument(item != null);
        Preconditions.checkArgument(langType != null);

        try {
            net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.d();
            if (i instanceof ItemSplashPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("splash_potion.effect.");
            }
            else if (i instanceof ItemLingeringPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("lingering_potion.effect.");
            }
            else if (i instanceof ItemPotion) {
                name = "item.minecraft." + PotionUtil.d(itemStack).b("potion.effect.");
            }
            else {
                name = i.a();
            }

            return getTranslate(name, langType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEntityName(EntityType entityType, LangType langType) {
        Preconditions.checkArgument(entityType != null);
        Preconditions.checkArgument(langType != null);

        return getTranslate("entity.minecraft." + entityType.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantmentName(Enchantment enchant, LangType langType) {
        Preconditions.checkArgument(enchant != null);
        Preconditions.checkArgument(langType != null);

        return getTranslate("enchantment.minecraft." + enchant.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantmentLevelName(int level, LangType langType) {
        Preconditions.checkArgument(langType != null);

        return getTranslate("enchantment.level." + level, langType);
    }

    @Override
    public String getPotionEffectName(PotionEffectType potionEffectType, LangType langType) {
        Preconditions.checkArgument(potionEffectType != null);
        Preconditions.checkArgument(langType != null);

        String effectName = switch (potionEffectType.getName().toLowerCase()) {
            case "fast_digging" -> "haste";
            case "harm" -> "instant_damage";
            case "heal" -> "instant_health";
            case "jump" -> "jump_boost";
            case "slow_digging" -> "mining_fatigue";
            case "confusion" -> "nausea";
            case "damage_resistance" -> "resistance";
            case "slow" -> "slowness";
            case "increase_damage" -> "strength";
            default -> potionEffectType.getName().toLowerCase();
        };
        String name = "effect.minecraft." + effectName;
        return getTranslate(name, langType);
    }

    @Override
    public String getBiomeName(Biome biome, LangType langType) {
        Preconditions.checkArgument(biome != null);
        Preconditions.checkArgument(langType != null);

        return getTranslate("biome.minecraft." + biome.name().toLowerCase(), langType);
    }
}
