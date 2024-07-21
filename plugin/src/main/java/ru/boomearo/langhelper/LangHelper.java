package ru.boomearo.langhelper;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.*;
import ru.boomearo.langhelper.versions.exceptions.LangException;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;
import ru.boomearo.langhelper.versions.exceptions.LangVersionException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@Getter
public class LangHelper extends JavaPlugin {

    @Getter
    private static LangHelper instance = null;

    private DefaultTranslateManager translateManager = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private static final List<Class<? extends DefaultTranslateManager>> VERSIONS = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2.class,
            Translate1_14_R1.class,
            Translate1_15_R1.class,
            Translate1_16_R3.class,
            Translate1_17_R1.class,
            Translate1_18_R2.class,
            Translate1_19_R3.class,
            // TODO Does not work due to mappings changes
            Translate1_20_R4.class,
            Translate1_21_R1.class
    );

    @Override
    public void onEnable() {
        instance = this;

        try {
            // TODO Move to own class
            File configFile = new File(getDataFolder() + File.separator + "config.yml");
            if (!configFile.exists()) {
                getLogger().info("Config is not found, creating a new one...");
                saveDefaultConfig();
            }

            this.translateManager = matchVersion(this);

            this.translateManager.loadConfigData();

            this.translateManager.checkAndDownloadLanguages();

            this.translateManager.loadLanguages();

            for (TranslatedMessages tra : this.translateManager.getAllTranslate()) {
                String languageName = tra.getTranslate("language.name");
                if (languageName == null) {
                    languageName = "Unknown-name";
                }
                String languageRegion = tra.getTranslate("language.region");
                if (languageRegion == null) {
                    languageRegion = "Unknown-region";
                }
                this.getLogger().info("Language '" + tra.getLangType().getName() + " [" + languageName + "-" + languageRegion + "]' successfully loaded. Translation keys: " + tra.getAllTranslate().size());
            }
        } catch (LangParseException e) {
            this.getLogger().warning("Failed to get translations from mojang: " + e.getMessage());
        } catch (LangVersionException e) {
            this.getLogger().warning("Failed to get server version: " + e.getMessage());
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to load translate manager", e);
        }

        getCommand("langhelper").setExecutor(new CmdExecutorLangHelper(this.translateManager));

        this.getLogger().info("Plugin successfully enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin successfully disabled!");
    }

    private DefaultTranslateManager matchVersion(Plugin plugin) throws LangVersionException {
        try {
            return VERSIONS.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new LangException("LangHelper does not support this minecraft version!")).
                    getConstructor(Plugin.class).
                    newInstance(plugin);
        } catch (Exception e) {
            throw new LangVersionException(e.getMessage());
        }
    }

}
