package ru.boomearo.langhelper.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

public class CmdList {

    private static final String sep = "=============================================";

    private final Map<String, Cmd> cmd = new HashMap<>();

    private String prefix;
    private String suffix;

    public void put(String name, Cmd cmd, String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.cmd.put(name, cmd);
    }

    public boolean execute(String name, CommandSender cs, String[] args) {
        Cmd res = this.cmd.get(name);
        if (res == null) {
            return false;
        }

        if (res.getPermission().length() != 0) {
            if (!cs.hasPermission(res.getPermission())) {
                cs.sendMessage("§cУ вас нет прав.");
                return true;
            }
        }
        if (!res.execute(cs, args)) {
            if (res.getUsage().length() != 0) {
                cs.sendMessage(getUsage(res));
            }
        }
        return true;
    }

    public Set<String> getListName() {
        return this.cmd.keySet();
    }

    public Map<String, Cmd> getListCmd() {
        return this.cmd;
    }

    public boolean hasPermsUse(CommandSender cs) {
        for (Cmd cmd : getCmds()) {
            if (cmd.getPermission().length() != 0) {
                if (cs.hasPermission(cmd.getPermission())) {
                    return true;
                }
                continue;
            }
            return true;
        }
        return false;
    }

    public Set<Cmd> getCmds() {
        return new TreeSet<Cmd>(this.cmd.values());
    }

    public void sendUsageCmds(CommandSender cs) {
        cs.sendMessage(this.prefix + sep);
        for (Cmd cmd : getCmds()) {
            if (cmd.getPermission().length() != 0) {
                if (cs.hasPermission(cmd.getPermission())) {
                    cs.sendMessage(getUsage(cmd));
                }
                continue;
            }
            cs.sendMessage(getUsage(cmd));
        }
        cs.sendMessage(this.prefix + sep);
    }

    private String getUsage(Cmd res) {
        return this.prefix + res.getUsage() + this.suffix + res.getDescription();
    }
}
