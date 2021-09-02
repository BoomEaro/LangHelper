package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    public abstract String getPotionEffectName(PotionEffectType effect, LangType type);

    @Override
    public abstract String getBiomeName(Biome biome, LangType type);

    @Override
    protected ConcurrentMap<String, String> parseTranslate(InputStream stream) {
        ConcurrentMap<String, String> translates = new ConcurrentHashMap<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

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

        return translates;
    }
}
