package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;

import java.util.List;

public class CommandReload extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandReload(LangHelper plugin,
                         ConfigManager configManager,
                         DefaultTranslateManager defaultTranslateManager,
                         CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "reload", "langhelper.command.reload");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper reload §8-§6 reload configuration");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            sendCurrentHelp(sender);
            return;
        }

        this.configManager.load();
        this.defaultTranslateManager.checkAndDownloadLanguages();
        this.defaultTranslateManager.loadLanguages();

        sender.sendMessage("Configuration successfully reloaded!");
    }

}
