package ru.boomearo.langhelper.commands;

import java.lang.reflect.Method;

import org.bukkit.command.CommandSender;

public class Cmd implements Comparable<Cmd> {

    private final String name;
    private final String description;
    private final String usage;
    private final String permission;
    private final String[] aliases;

    private final Object cmdMainObject;
    private final Method method;

    public Cmd(Object cmdMainObject, Method method, CmdInfo cmdinfo) {
        this.cmdMainObject = cmdMainObject;
        this.method = method;
        this.name = cmdinfo.name();
        this.description = cmdinfo.description();
        this.usage = cmdinfo.usage();
        this.permission = cmdinfo.permission();
        this.aliases = cmdinfo.aliases();
    }

    public boolean execute(CommandSender cs, String[] args) {
        try {
            return (boolean) this.method.invoke(this.cmdMainObject, cs, args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    @Override
    public int compareTo(Cmd o) {
        return (this.name + this.description).compareTo((o.getName() + o.getDescription()));
    }

}