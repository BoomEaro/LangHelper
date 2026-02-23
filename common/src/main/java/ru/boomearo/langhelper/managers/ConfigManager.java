package ru.boomearo.langhelper.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import ru.boomearo.langhelper.api.LangType;

import java.io.File;
import java.util.*;

@RequiredArgsConstructor
@Getter
public class ConfigManager {

    private final Plugin plugin;

    private Set<LangType> enabledLanguages = new HashSet<>();

    private Map<String, String> messages = new HashMap<>();

    public void load() {
        File configFile = new File(this.plugin.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            this.plugin.getLogger().info("Config is not found, creating a new one...");
            this.plugin.saveDefaultConfig();
        }

        this.plugin.reloadConfig();

        FileConfiguration configuration = this.plugin.getConfig();

        Set<LangType> tmpEnabledLanguages = new HashSet<>();
        List<String> configLanguages = configuration.getStringList("enabledLanguages");
        if (configLanguages != null) {
            for (String t : configLanguages) {
                LangType parsedType = null;
                try {
                    parsedType = LangType.valueOf(t.toUpperCase(Locale.ROOT));
                } catch (Exception ignored) {
                }
                if (parsedType == null) {
                    continue;
                }

                this.plugin.getLogger().info("Using language: " + parsedType.name());
                tmpEnabledLanguages.add(parsedType);
            }
        }

        this.enabledLanguages = Collections.unmodifiableSet(tmpEnabledLanguages);

        Map<String, String> tmp = new HashMap<>();
        ConfigurationSection messagesSection = configuration.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                String message = messagesSection.getString(key);
                if (message == null) {
                    continue;
                }
                tmp.put(key, ChatColor.translateAlternateColorCodes('&', message));
            }
        }
        this.messages = tmp;
    }

    public String getMessage(String key) {
        String message = this.messages.get(key);
        if (message == null) {
            return "<invalid message translate key '" + key + "'>";
        }

        return message;
    }

}
