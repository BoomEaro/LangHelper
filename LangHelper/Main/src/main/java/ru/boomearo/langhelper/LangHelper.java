package ru.boomearo.langhelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.AbstractTranslateManager;
import ru.boomearo.langhelper.versions.Translate;
import ru.boomearo.langhelper.versions.Translate1_12_R1;
import ru.boomearo.langhelper.versions.Translate1_16_R3;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;
    
    private static LangHelper instance = null;
    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
    
    @Override
	public void onEnable() {
		instance = this;

		this.version = matchVersion(getLanguageFolder());
		
		if (this.version != null) {
		    for (Translate tra : this.version.getAllTranslate()) {
		        this.getLogger().info("Язык '" + tra.getLangType().name() + "' успешно загружен. Количество строк: " + tra.getAllTranslate().size());
		    }
		}
		
		getCommand("langhelper").setExecutor(new CmdExecutorLangHelper());
		
		this.getLogger().info("Плагин успешно запущен!");
	}
	
    @Override
	public void onDisable() {
		this.getLogger().info("Плагин успешно выключен!");
	}

    public AbstractTranslateManager getAbstractTranslateManager() {
        return this.version;
    }
    
	public static LangHelper getInstance() { 
		return instance;
	}
	
	public static File getLanguageFolder() {
	    return new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
	}
	
    private final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_16_R3.class
    );
    
    public AbstractTranslateManager matchVersion(File file) {
        try {
            return this.versions.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new Exception("Плагин не поддерживает данную версию сервера!")).
                    getConstructor(File.class).
                    newInstance(file);
        } 
        catch (Exception ex) {
            this.getLogger().severe(ex.getMessage());
        }
        return null;
    }
}
