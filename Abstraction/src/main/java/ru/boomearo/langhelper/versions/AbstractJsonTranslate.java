package ru.boomearo.langhelper.versions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractJsonTranslate extends AbstractTranslateManager {

    public AbstractJsonTranslate(String version) {
        super(version);
    }

    @Override
    public abstract String getItemName(ItemStack item, LangType type);

    @Override
    public abstract String getEntityName(EntityType entity, LangType type);

    @Override
    public abstract String getEnchantName(Enchantment enchant, LangType type);

    @Override
    public abstract String getEnchantLevelName(int level, LangType type);

    @Override
    protected ConcurrentMap<LangType, Translate> loadTranslateFromDisk(File file, Collection<LangType> enabledLanguages) {
        ConcurrentMap<LangType, Translate> types = new ConcurrentHashMap<LangType, Translate>();
        try {
            File folders = file;
            if (!folders.exists()) {
                folders.getParentFile().mkdirs();
            }

            if (!folders.isDirectory()) {
                return types;
            }
            File ver = new File(folders, getVersion());
            if (ver.isDirectory()) {
                File[] type = ver.listFiles();
                if (type != null) {
                    for (File t : type) {
                        if (t.isFile()) {
                            LangType lt = null;
                            try {
                                lt = LangType.valueOf(t.getName());
                            }
                            catch (Exception e) {
                            }
                            if (lt != null) {
                                if (enabledLanguages.contains(lt)) {
                                    ConcurrentMap<String, String> translates = new ConcurrentHashMap<String, String>();
                                    JSONParser jsonParser = new JSONParser();
                                    try (FileReader reader = new FileReader(t)) {

                                        Object obj = jsonParser.parse(reader);

                                        if (obj instanceof JSONObject) {
                                            JSONObject o = (JSONObject) obj;


                                            @SuppressWarnings("unchecked")
                                            Set<Map.Entry<Object, Object>> s = (Set<Map.Entry<Object, Object>>) o.entrySet();
                                            for (Map.Entry<Object, Object> f : s) {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return types;
    }
}
