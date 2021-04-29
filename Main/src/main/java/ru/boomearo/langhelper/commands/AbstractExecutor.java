package ru.boomearo.langhelper.commands;

import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class AbstractExecutor implements CommandExecutor, TabCompleter {
    private CmdList cmds = null;

    private Object cmdMain = null;

    //Мне нужно получить обьект класса с которого я беру методы
    public AbstractExecutor(Object cmd) {
        this.cmdMain = cmd;
        registerCmd();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!this.cmds.hasPermsUse(sender)) {
            sender.sendMessage("§cУ вас не достаточно прав.");
            return true;
        }
        int len = args.length;
        if (len == 0) {
            return zeroArgument(sender, this.cmds);
        }
        String[] argsCopy = new String[len-1];
        System.arraycopy(args, 1, argsCopy, 0, len-1);
        if (!this.cmds.execute(args[0].toLowerCase(), sender, argsCopy)) {
            this.cmds.sendUsageCmds(sender);
        }
        return true;
    }


    private void registerCmd() {
        this.cmds = new CmdList();
        for (Method method : this.cmdMain.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(CmdInfo.class)) {
                continue;
            }
            Cmd cmd = new Cmd(this.cmdMain, method, method.getAnnotation(CmdInfo.class));
            this.cmds.put(cmd.getName(), cmd, getPrefix(), getSuffix());
            //Пытаемся зарегать алиасы.
            for (String aliases : cmd.getAliases()) {
                if (aliases != null && !aliases.isEmpty()) {
                    this.cmds.put(aliases, cmd, getPrefix(), getSuffix());
                }
            }
        }
    }

    public abstract boolean zeroArgument(CommandSender sender, CmdList cmds);

    public abstract String getPrefix();
    public abstract String getSuffix();

}
