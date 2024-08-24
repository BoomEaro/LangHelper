package ru.boomearo.langhelper;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.boomearo.langhelper.commands.langhelper.LangHelperCommandExecutor;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.*;
import ru.boomearo.langhelper.versions.exceptions.LangException;
import ru.boomearo.langhelper.versions.exceptions.LangVersionException;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@Getter
public class LangHelper extends JavaPlugin {

    @Getter
    private static LangHelper instance = null;

    private ConfigManager configManager;

    private DefaultTranslateManager translateManager = null;

    private static final List<TranslationVersionWrapper> VERSIONS = Arrays.asList(
            new TranslationVersionWrapper("1.12.2", Translate1_12_R1.class),
            new TranslationVersionWrapper("1.13.2", Translate1_13_R2.class),
            new TranslationVersionWrapper("1.14.4", Translate1_14_R1.class),
            new TranslationVersionWrapper("1.15.2", Translate1_15_R1.class),
            new TranslationVersionWrapper("1.16.5", Translate1_16_R3.class),
            new TranslationVersionWrapper("1.17.1", Translate1_17_R1.class),
            new TranslationVersionWrapper("1.18.2", Translate1_18_R2.class),
            new TranslationVersionWrapper("1.19.4", Translate1_19_R3.class),
            new TranslationVersionWrapper("1.20.6", Translate1_20_R4.class),
            new TranslationVersionWrapper("1.21", Translate1_21_R1.class)
    );

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.configManager.load();

        try {
            this.translateManager = matchVersion(this, this.configManager);

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
                this.getLogger().info("Language '" + tra.getLangType().name() + " [" + languageName + "-" + languageRegion + "]' successfully loaded. Translation keys: " + tra.getAllTranslate().size());
            }

            this.getCommand("langhelper").setExecutor(new LangHelperCommandExecutor(
                    this,
                    this.configManager,
                    this.translateManager
            ));

            this.getLogger().info("Plugin successfully enabled!");
        } catch (LangVersionException e) {
            this.getLogger().warning("Failed to get server version: " + e.getMessage());
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to load translate manager", e);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin successfully disabled!");
    }

    private DefaultTranslateManager matchVersion(Plugin plugin, ConfigManager configManager) throws LangVersionException {
        try {
            String bukkitVersion = Bukkit.getServer().getBukkitVersion();
            return VERSIONS.stream()
                    .filter(translationVersionWrapper -> bukkitVersion.startsWith(translationVersionWrapper.version()))
                    .findFirst().orElseThrow(() -> new LangException("LangHelper does not support this minecraft version!"))
                    .clazz()
                    .getConstructor(Plugin.class, ConfigManager.class).
                    newInstance(plugin, configManager);
        } catch (Exception e) {
            throw new LangVersionException(e.getMessage());
        }
    }

}
