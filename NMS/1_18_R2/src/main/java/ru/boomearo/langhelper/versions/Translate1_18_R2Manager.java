package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
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

public class Translate1_18_R2Manager extends JsonTranslateManager {

    public Translate1_18_R2Manager(JavaPlugin javaPlugin) {
        super("1.18.2", javaPlugin);
    }

    @Override
    public String getItemName(ItemStack item, LangType langType) {
        if (item == null) {
            throw new IllegalArgumentException("item является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }

        try {
            net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.c();
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
        if (entityType == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }

        return getTranslate("entity.minecraft." + entityType.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantName(Enchantment enchant, LangType langType) {
        if (enchant == null) {
            throw new IllegalArgumentException("enchant является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }

        return getTranslate("enchantment.minecraft." + enchant.getKey().getKey(), langType);
    }

    @Override
    public String getEnchantLevelName(int level, LangType langType) {
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("enchantment.level." + level, langType);
    }

    @Override
    public String getPotionEffectName(PotionEffectType potionEffectType, LangType langType) {
        if (potionEffectType == null) {
            throw new IllegalArgumentException("effect является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        String effectName = potionEffectType.getName().toLowerCase();
        switch (effectName) {
            case "fast_digging":
                effectName = "haste";
                break;
            case "harm":
                effectName = "instant_damage";
                break;
            case "heal":
                effectName = "instant_health";
                break;
            case "jump":
                effectName = "jump_boost";
                break;
            case "slow_digging":
                effectName = "mining_fatigue";
                break;
            case "confusion":
                effectName = "nausea";
                break;
            case "damage_resistance":
                effectName = "resistance";
                break;
            case "slow":
                effectName = "slowness";
                break;
            case "increase_damage":
                effectName = "strength";
                break;
        }
        String name = "effect.minecraft." + effectName;
        return getTranslate(name, langType);
    }

    @Override
    public String getBiomeName(Biome biome, LangType langType) {
        if (biome == null) {
            throw new IllegalArgumentException("biome является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("biome.minecraft." + biome.name().toLowerCase(), langType);
    }
}
