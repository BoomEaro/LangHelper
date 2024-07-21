package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

/**
 * Более продвинутая абстракция менеджера перевода, использующая парсинг json
 */
public abstract class JsonTranslateManager extends DefaultTranslateManager {

    public JsonTranslateManager(String version, Plugin plugin) {
        super(version, plugin);
    }

    @Override
    public abstract String getItemName(ItemStack itemStack, LangType langType);

    @Override
    public abstract String getEntityName(EntityType entityType, LangType langType);

    @Override
    public abstract String getEnchantmentName(Enchantment enchant, LangType langType);

    @Override
    public abstract String getEnchantmentLevelName(int level, LangType langType);

    @Override
    public abstract String getPotionEffectName(PotionEffectType potionEffectType, LangType langType);

    @Override
    public abstract String getBiomeName(Biome biome, LangType langType);

    @Override
    protected Map<String, String> parseTranslate(InputStream stream) {
        Map<String, String> translates = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            Object obj = jsonParser.parse(reader);

            if (obj instanceof JSONObject o) {
                Set<Map.Entry<Object, Object>> s = (Set<Map.Entry<Object, Object>>) o.entrySet();
                for (Map.Entry<Object, Object> f : s) {
                    translates.put(f.getKey().toString().toLowerCase(Locale.ROOT).replace("_", ""), f.getValue().toString());
                }
            }

        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to parse translation", e);
        }

        return Collections.unmodifiableMap(translates);
    }
}
