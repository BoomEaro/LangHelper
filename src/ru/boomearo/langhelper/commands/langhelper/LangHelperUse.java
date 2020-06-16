package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;

import ru.boomearo.langhelper.commands.CmdInfo;
import ru.boomearo.langhelper.managers.LangManager;

public class LangHelperUse {
	
	@CmdInfo(name = "reload", description = "Перезагрузить конфигурацию", usage = "/langhelper reload", permission = "")
	public boolean reload(CommandSender cs, String[] args) {
		if (args.length < 0 || args.length > 0) {
			return false;
		}
		
		LangManager.load();
		
		cs.sendMessage("Конфигурация успешно перезагружена!");
		
		return true;
	}
	
	
	
}
