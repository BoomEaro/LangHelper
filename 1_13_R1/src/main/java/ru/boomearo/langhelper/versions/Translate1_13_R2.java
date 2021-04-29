package ru.boomearo.langhelper.versions;

import java.io.File;
import java.io.FileReader;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.ItemLingeringPotion;
import net.minecraft.server.v1_13_R2.ItemPotion;
import net.minecraft.server.v1_13_R2.ItemSplashPotion;
import net.minecraft.server.v1_13_R2.PotionUtil;

public class Translate1_13_R2 extends AbstractTranslateManager {
    
    @Override
    public String getItemName(ItemStack item, LangType type) {
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
            name = itemStack.getItem().getName();
        }

        return getTranslate(name, type);
    }

    @Override
    public String getEntityName(EntityType entity, LangType type) {
        String name = "entity.minecraft." + entity.name();
        return getTranslate(name,  type);
    }

    @Override
    public String getEnchantName(Enchantment enchant,  LangType type) {

        String name = "enchantment.minecraft." + enchant.getKey().getKey();
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
            File ver = new File(folders, "1_13_R2");
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
                                JSONParser jsonParser = new JSONParser();
                                try (FileReader reader = new FileReader(t)) {

                                    Object obj = jsonParser.parse(reader);

                                    if (obj instanceof JSONObject) {
                                        JSONObject o = (JSONObject) obj;


                                        @SuppressWarnings("unchecked")
                                        Set<Entry<Object, Object>> s = (Set<Entry<Object, Object>>) o.entrySet();
                                        for (Entry<Object, Object> f : s) {
                                            translates.put(f.getKey().toString().toLowerCase().replace("_", ""), f.getValue().toString());
                                        }
                                    }

                                } 
                                catch (Exception e) {
                                    e.printStackTrace();
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
