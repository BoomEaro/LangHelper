package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.ItemLingeringPotion;
import net.minecraft.server.v1_13_R2.ItemPotion;
import net.minecraft.server.v1_13_R2.ItemSplashPotion;
import net.minecraft.server.v1_13_R2.PotionUtil;

public class Translate1_13_R2 extends AbstractJsonTranslate {

    public Translate1_13_R2() {
        super("1.13.2");
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        if (item == null) {
            throw new IllegalArgumentException("item является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }

        try {
            net.minecraft.server.v1_13_R2.ItemStack itemStack = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.getItem();
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
                name = i.getName();
            }

            return getTranslate(name, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEntityName(EntityType entity, LangType type) {
        if (entity == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }

        return getTranslate("entity.minecraft." + entity.getName(), type);
    }

    @Override
    public String getEnchantName(Enchantment enchant, LangType type) {
        if (enchant == null) {
            throw new IllegalArgumentException("enchant является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }

        return getTranslate("enchantment.minecraft." + enchant.getKey().getKey(), type);
    }

    @Override
    public String getEnchantLevelName(int level, LangType type) {
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("enchantment.level." + level, type);
    }

    @Override
    public String getPotionEffectName(PotionEffectType effect, LangType type) {
        if (effect == null) {
            throw new IllegalArgumentException("effect является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        String effectName = effect.getName().toLowerCase();
        switch (effectName) {
            case "fast_digging": effectName = "haste"; break;
            case "harm": effectName = "instant_damage"; break;
            case "heal": effectName = "instant_health"; break;
            case "jump": effectName = "jump_boost"; break;
            case "slow_digging": effectName = "mining_fatigue"; break;
            case "confusion": effectName = "nausea"; break;
            case "damage_resistance": effectName = "resistance"; break;
            case "slow": effectName = "slowness"; break;
            case "increase_damage": effectName = "strength"; break;
        }
        return getTranslate("effect.minecraft." + effectName, type);
    }

    @Override
    public String getBiomeName(Biome biome, LangType type) {
        if (biome == null) {
            throw new IllegalArgumentException("biome является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("biome.minecraft." + biome.name().toLowerCase(), type);
    }
}
