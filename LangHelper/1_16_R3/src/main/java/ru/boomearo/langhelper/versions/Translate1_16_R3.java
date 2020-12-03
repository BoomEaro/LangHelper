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

public class Translate1_16_R3 extends AbstractTranslateManager {

    public Translate1_16_R3(File file) {
        super(file);
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);

        return getTranslate(itemStack.getItem().getName(), type);
    }
    
    @Override
    public String getEntityName(EntityType entity, LangType type) {
        String name = "entity." + entity.name() + ".name";
        return getTranslate(name,  type);
    }

    @Override
    public String getEnchantName(Enchantment enchant,  LangType type) {
        @SuppressWarnings("deprecation")
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
                return null;
            }
            File ver = new File(folders, "1_16_R3");
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
                                            translates.put(f.getKey().toString(), f.getValue().toString());
                                        }
                                    }

                                } 
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                types.put(lt, new Translate(translates));
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
