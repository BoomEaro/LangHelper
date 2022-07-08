package ru.boomearo.langhelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.TranslatedMessages;
import ru.boomearo.langhelper.versions.Translate1_12_R1;
import ru.boomearo.langhelper.versions.Translate1_13_R2Manager;
import ru.boomearo.langhelper.versions.Translate1_14_R1Manager;
import ru.boomearo.langhelper.versions.Translate1_15_R1Manager;
import ru.boomearo.langhelper.versions.Translate1_16_R3Manager;
import ru.boomearo.langhelper.versions.Translate1_17_R1Manager;
import ru.boomearo.langhelper.versions.Translate1_18_R2Manager;
import ru.boomearo.langhelper.versions.Translate1_19_R1Manager;
import ru.boomearo.langhelper.versions.TranslateManager;
import ru.boomearo.langhelper.versions.exceptions.LangException;
import ru.boomearo.langhelper.versions.exceptions.LangParseException;
import ru.boomearo.langhelper.versions.exceptions.LangVersionException;

public class LangHelper extends JavaPlugin {

    private DefaultTranslateManager defaultTranslateManager = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private static LangHelper instance = null;
    private static final List<Class<? extends DefaultTranslateManager>> VERSIONS = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2Manager.class,
            Translate1_14_R1Manager.class,
            Translate1_15_R1Manager.class,
            Translate1_16_R3Manager.class,
            Translate1_17_R1Manager.class,
            Translate1_18_R2Manager.class,
            Translate1_19_R1Manager.class
    );

    @Override
    public void onEnable() {
        instance = this;

        try {
            File configFile = new File(getDataFolder() + File.separator + "config.yml");
            if (!configFile.exists()) {
                getLogger().info("Конфиг не найден, создаю новый...");
                saveDefaultConfig();
            }

            //Вычисляем версию сервера и создаем соответствующий экземпляр
            this.defaultTranslateManager = matchVersion(this);

            //Загружаем информацию из конфига
            this.defaultTranslateManager.loadConfigData();

            //Проверяем, на месте ли включенные языки
            this.defaultTranslateManager.checkAndDownloadLanguages();

            //Подгружаем языки с диска
            this.defaultTranslateManager.loadLanguages();

            //Просто оповещаем о том, сколько строк и какие языки были загружены
            for (TranslatedMessages tra : this.defaultTranslateManager.getAllTranslate()) {
                String languageName = tra.getTranslate("language.name");
                if (languageName == null) {
                    languageName = "Unknown-name";
                }
                String languageRegion = tra.getTranslate("language.region");
                if (languageRegion == null) {
                    languageRegion = "Unknown-region";
                }
                this.getLogger().info("Язык '" + tra.getLangType().getName() + " [" + languageName + "-" + languageRegion + "]' успешно загружен. Количество строк: " + tra.getAllTranslate().size());
            }
        }
        catch (LangParseException e) {
            this.getLogger().warning("Ошибка при получении языков от mojang: " + e.getMessage());
        }
        catch (LangVersionException e) {
            this.getLogger().warning("Ошибка при получении версии сервера: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getCommand("langhelper").setExecutor(new CmdExecutorLangHelper(this.defaultTranslateManager));

        this.getLogger().info("Плагин успешно запущен!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Плагин успешно выключен!");
    }

    public TranslateManager getTranslateManager() {
        return this.defaultTranslateManager;
    }

    private DefaultTranslateManager matchVersion(JavaPlugin javaPlugin) throws LangVersionException {
        try {
            return VERSIONS.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new LangException("Плагин не поддерживает данную версию сервера!")).
                    getConstructor(JavaPlugin.class).
                    newInstance(javaPlugin);
        }
        catch (Exception e) {
            //Вызываем новое, но свое
            throw new LangVersionException(e.getMessage());
        }
    }

    public static LangHelper getInstance() {
        return instance;
    }

}
