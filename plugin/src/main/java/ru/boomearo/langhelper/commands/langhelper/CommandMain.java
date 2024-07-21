package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;

import java.util.List;

public class CommandMain extends CommandNodeBukkit {

    public CommandMain(LangHelper plugin, ConfigManager configManager) {
        super(plugin, configManager, null, "root", "langhelper.command");
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper §8-§6 current page");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sendHelp(sender);
    }

}
