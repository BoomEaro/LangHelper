package ru.boomearo.langhelper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.AbstractTranslateManager;
import ru.boomearo.langhelper.versions.Translate1_12_R1;
import ru.boomearo.langhelper.versions.Translate1_16_R3;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;
    
    private static LangHelper instance = null;
    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
    
    @Override
	public void onEnable() {
		instance = this;
		
		File folders = new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
		
		this.version = matchVersion(folders);
		
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
	
    private final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_16_R3.class
    );
    
    public AbstractTranslateManager matchVersion(File file) {
        try {
            return this.versions.stream()
                    .filter(version -> version.getSimpleName().substring(7).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new RuntimeException("Your server version isn't supported in LangHelper!")).
                    getConstructor(File.class).
                    newInstance(file);
        } 
        catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }
}
