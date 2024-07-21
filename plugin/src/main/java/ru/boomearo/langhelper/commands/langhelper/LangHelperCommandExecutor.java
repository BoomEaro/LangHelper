package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;

import java.util.ArrayList;
import java.util.List;

public class LangHelperCommandExecutor implements CommandExecutor, TabCompleter {

    private final CommandNodeBukkit node;

    public LangHelperCommandExecutor(LangHelper plugin,
                                     ConfigManager mainConfig,
                                     DefaultTranslateManager defaultTranslateManager) {
        CommandMain root = new CommandMain(plugin, mainConfig);
        root.addNode(new CommandReload(plugin,
                mainConfig,
                defaultTranslateManager,
                root
        ));
        root.addNode(new CommandTest(
                plugin,
                mainConfig,
                defaultTranslateManager,
                root)
        );
        this.node = root;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.node.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>(this.node.suggest(sender, args));
    }

}
