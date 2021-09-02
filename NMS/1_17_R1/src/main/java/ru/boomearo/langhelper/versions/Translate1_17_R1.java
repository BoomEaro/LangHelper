package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemLingeringPotion;
import net.minecraft.world.item.ItemPotion;
import net.minecraft.world.item.ItemSplashPotion;
import net.minecraft.world.item.alchemy.PotionUtil;
import org.bukkit.potion.PotionEffectType;

public class Translate1_17_R1 extends AbstractJsonTranslate {

    public Translate1_17_R1() {
        super("1.17.1");
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

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
            name = itemStack.getItem().getName();
        }

        return getTranslate(name, type);
    }

    @Override
    public String getEntityName(EntityType entity, LangType type) {
        String name = "entity.minecraft." + entity.getKey().getKey();
        return getTranslate(name, type);
    }

    @Override
    public String getEnchantName(Enchantment enchant, LangType type) {
        String name = "enchantment.minecraft." + enchant.getKey().getKey();
        return getTranslate(name, type);
    }

    @Override
    public String getEnchantLevelName(int level, LangType type) {
        String name = "enchantment.level." + level;
        return getTranslate(name, type);
    }

    @Override
    public String getPotionEffectName(PotionEffectType effect, LangType type) {
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
        String name = "effect.minecraft." + effectName;
        return getTranslate(name, type);
    }

    @Override
    public String getBiomeName(Biome biome, LangType type) {
        return getTranslate("biome.minecraft." + biome.name().toLowerCase(), type);
    }
}
