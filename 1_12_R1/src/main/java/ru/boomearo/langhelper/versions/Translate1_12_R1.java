package ru.boomearo.langhelper.versions;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.EnumColor;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemBanner;
import net.minecraft.server.v1_12_R1.ItemLingeringPotion;
import net.minecraft.server.v1_12_R1.ItemPotion;
import net.minecraft.server.v1_12_R1.ItemSplashPotion;
import net.minecraft.server.v1_12_R1.ItemTippedArrow;
import net.minecraft.server.v1_12_R1.PotionUtil;

public class Translate1_12_R1 extends AbstractTranslateManager {

    public Translate1_12_R1(File file) {
        super(file);
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);

        String name;
        Item i = itemStack.getItem();
        if (i instanceof ItemBanner) {
            String str = "item.banner.";
            EnumColor enumColor = EnumColor.fromInvColorIndex(itemStack.getData() & 0xF);
            name = str + enumColor.d() + ".name";
        }
        else if (i instanceof ItemSplashPotion) {
            name = PotionUtil.d(itemStack).b("splash_potion.effect.");
        }
        else if (i instanceof ItemLingeringPotion) {
            name = PotionUtil.d(itemStack).b("lingering_potion.effect.");
        }
        else if (i instanceof ItemPotion) {
            name = PotionUtil.d(itemStack).b("potion.effect.");
        }
        else if (i instanceof ItemTippedArrow) {
            name = PotionUtil.d(itemStack).b("tipped_arrow.effect.");
        }
        else {
            name = itemStack.getItem().a(itemStack) + ".name";
        }

        return getTranslate(name, type);
    }

    @Override
    public String getEntityName(EntityType entity, LangType type) {
        String name = "entity." + entity.name() + ".name";
        return getTranslate(name,  type);
    }

    //Похоже что на 1.12 название в файле и на сервере отличаются, поэтому до 1.13 не рекомендуется юзать
    @Override
    public String getEnchantName(Enchantment enchant,  LangType type) {
        String name = "enchantment." + enchant.getName();
        return getTranslate(name, type);
    }

    @Override
    public String getEnchantLevelName(int level, LangType type) {
        String name = "enchantment.level." + level;
        return getTranslate(name, type);
    }



    @Override
    public ConcurrentMap<LangType, Translate> loadTranslateFromDisk(File file) {
        ConcurrentMap<LangType, Translate> types = new ConcurrentHashMap<LangType, Translate>();
        try {
            File folders = file;
            if (!folders.exists()) {
                folders.getParentFile().mkdirs();
            }

            if (!folders.isDirectory()) {
                return types;
            }
            File ver = new File(folders, "1_12_R1");
            if (ver.isDirectory()) {
                File[] type = ver.listFiles();
                if (type != null) {
                    for (File t : type) {
                        if (t.isFile()) {
                            LangType lt = null;
                            try {
                                lt = LangType.valueOf(t.getName());
                            }
                            catch (Exception e) {}
                            if (lt != null) {
                                ConcurrentMap<String, String> translates = new ConcurrentHashMap<String, String>();
                                for (String line : com.google.common.io.Files.readLines(t, StandardCharsets.UTF_8)) {
                                    if (line.isEmpty()) {
                                        continue;
                                    }

                                    String[] args = line.split("=");
                                    if (args.length >= 2) {
                                        translates.put(args[0].toLowerCase().replace("_", ""), args[1]);
                                    }
                                }
                                types.put(lt, new Translate(lt, translates));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return types;
    }
}
