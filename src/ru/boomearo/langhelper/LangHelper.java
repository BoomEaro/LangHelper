package ru.boomearo.langhelper;

import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.managers.LangManager;

public class LangHelper extends JavaPlugin {

	public void onEnable() {
		instance = this;
		
		LangManager.load();
		
		getCommand("langhelper").setExecutor(new CmdExecutorLangHelper());
		
		this.getLogger().info("Плагин успешно запущен!");
	}
	
	public void onDisable() {
		this.getLogger().info("Плагин успешно выключен!");
	}

	private static LangHelper instance = null;
	public static LangHelper getInstance() { 
		if (instance != null) return instance; return null; 
	}
}
