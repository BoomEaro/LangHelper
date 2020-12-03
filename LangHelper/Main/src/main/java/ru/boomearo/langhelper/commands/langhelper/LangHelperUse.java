package ru.boomearo.langhelper.commands.langhelper;

import java.io.File;

import org.bukkit.command.CommandSender;

import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CmdInfo;

public class LangHelperUse {
	
	@CmdInfo(name = "reload", description = "Перезагрузить конфигурацию", usage = "/langhelper reload", permission = "")
	public boolean reload(CommandSender cs, String[] args) {
		if (args.length < 0 || args.length > 0) {
			return false;
		}
		
	    File folders = new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
	    
		LangHelper.getInstance().getAbstractTranslateManager().loadTranslateFromDisk(folders);
		
		cs.sendMessage("Конфигурация успешно перезагружена!");
		
		return true;
	}
	
	
	
}
