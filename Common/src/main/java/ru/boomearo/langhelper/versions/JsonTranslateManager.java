package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Более продвинутая абстракция менеджера перевода, использующая парсинг json
 */
public abstract class JsonTranslateManager extends DefaultTranslateManager {

    public JsonTranslateManager(String version, JavaPlugin javaPlugin) {
        super(version, javaPlugin);
    }

    @Override
    public abstract String getItemName(ItemStack itemStack, LangType langType);

    @Override
    public abstract String getEntityName(EntityType entityType, LangType langType);

    @Override
    public abstract String getEnchantName(Enchantment enchant, LangType langType);

    @Override
    public abstract String getEnchantLevelName(int level, LangType langType);

    @Override
    public abstract String getPotionEffectName(PotionEffectType potionEffectType, LangType langType);

    @Override
    public abstract String getBiomeName(Biome biome, LangType langType);

    @Override
    protected ConcurrentMap<String, String> parseTranslate(InputStream stream) {
        ConcurrentMap<String, String> translates = new ConcurrentHashMap<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            Object obj = jsonParser.parse(reader);

            if (obj instanceof JSONObject o) {
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
