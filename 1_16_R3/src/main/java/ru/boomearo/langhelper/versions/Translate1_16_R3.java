package ru.boomearo.langhelper.versions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.Item;
import net.minecraft.server.v1_16_R3.ItemLingeringPotion;
import net.minecraft.server.v1_16_R3.ItemPotion;
import net.minecraft.server.v1_16_R3.ItemSplashPotion;
import net.minecraft.server.v1_16_R3.PotionUtil;

public class Translate1_16_R3 extends AbstractJsonTranslate {

    public Translate1_16_R3() {
        super("1.16.5");
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);

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
}
