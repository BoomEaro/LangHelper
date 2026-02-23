package ru.boomearo.langhelper.versions;

import lombok.NonNull;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.managers.ConfigManager;

import javax.annotation.Nullable;
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

    public JsonTranslateManager(String version, Plugin plugin, ConfigManager configManager) {
        super(version, plugin, configManager);
    }

    @Nullable
    @Override
    public abstract String getItemName(@NonNull ItemStack itemStack, @NonNull LangType langType);

    @Nullable
    @Override
    public abstract String getEntityName(@NonNull EntityType entityType, @NonNull LangType langType);

    @Nullable
    @Override
    public abstract String getEnchantmentName(@NonNull Enchantment enchant, @NonNull LangType langType);

    @Nullable
    @Override
    public abstract String getEnchantmentLevelName(int level, @NonNull LangType langType);

    @Nullable
    @Override
    public abstract String getPotionEffectName(@NonNull PotionEffectType potionEffectType, @NonNull LangType langType);

    @Nullable
    @Override
    public abstract String getBiomeName(@NonNull Biome biome, @NonNull LangType langType);

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
