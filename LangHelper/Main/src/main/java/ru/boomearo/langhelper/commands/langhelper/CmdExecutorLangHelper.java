package ru.boomearo.langhelper.commands.langhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ru.boomearo.langhelper.commands.AbstractExecutor;
import ru.boomearo.langhelper.commands.CmdList;

public class CmdExecutorLangHelper extends AbstractExecutor {

	public CmdExecutorLangHelper() {
		super(new LangHelperUse());
	}

	@Override
	public boolean zeroArgument(CommandSender sender, CmdList cmds) {
		cmds.sendUsageCmds(sender);
		return true;
	}

	private static final List<String> empty = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender cs, Command arg1, String arg2, String[] arg3) {
		if (!cs.hasPermission("langhelper.admin")) {
			return empty;
		}
		if (arg3.length == 1) {
			List<String> matches = new ArrayList<>();
			String search = arg3[0].toLowerCase();
			for (String se : Arrays.asList("reload"))
			{
				if (se.toLowerCase().startsWith(search))
				{
					matches.add(se);
				}
			}
			return matches;
		}
		return empty;
	}

	@Override
	public String getPrefix() {
		return "§f ";
	}

	@Override
	public String getSuffix() {
		return " §8-§6 ";
	}

}

