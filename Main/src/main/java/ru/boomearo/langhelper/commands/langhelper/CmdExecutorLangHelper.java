package ru.boomearo.langhelper.commands.langhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.AbstractExecutor;
import ru.boomearo.langhelper.commands.CmdList;
import ru.boomearo.langhelper.versions.LangType;

public class CmdExecutorLangHelper extends AbstractExecutor {

    private static final List<String> empty = new ArrayList<>();

    public CmdExecutorLangHelper() {
        super(new LangHelperUse());
    }

    @Override
    public boolean zeroArgument(CommandSender sender, CmdList cmds) {
        cmds.sendUsageCmds(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!sender.hasPermission("langhelper.admin")) {
            return empty;
        }
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            String search = args[0].toLowerCase();
            for (String se : Arrays.asList("reload", "testitem", "testentity", "testenchant", "testenchantlevel")) {
                if (se.toLowerCase().startsWith(search)) {
                    matches.add(se);
                }
            }
            return matches;
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("testitem") ||
                    args[0].equalsIgnoreCase("testentity") ||
                    args[0].equalsIgnoreCase("testenchant") ||
                    args[0].equalsIgnoreCase("testenchantlevel")) {

                List<String> matches = new ArrayList<>();
                String search = args[1].toLowerCase();
                for (LangType lt : LangHelper.getInstance().getAbstractTranslateManager().getAllTranslateLang()) {
                    if (lt.getName().toLowerCase().startsWith(search)) {
                        matches.add(lt.getName());
                    }
                }
                return matches;
            }
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("testentity")) {
                if (!args[1].isEmpty()) {
                    List<String> matches = new ArrayList<>();
                    String search = args[2].toLowerCase();
                    for (EntityType et : EntityType.values()) {
                        if (et.name().toLowerCase().startsWith(search)) {
                            matches.add(et.name());
                        }
                    }
                    return matches;
                }
            }
            else if (args[0].equalsIgnoreCase("testenchant")) {
                if (!args[1].isEmpty()) {
                    List<String> matches = new ArrayList<>();
                    String search = args[2].toLowerCase();
                    for (Enchantment ench : Enchantment.values()) {
                        if (ench.getName().toLowerCase().startsWith(search)) {
                            matches.add(ench.getName());
                        }
                    }
                    return matches;
                }
            }
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

