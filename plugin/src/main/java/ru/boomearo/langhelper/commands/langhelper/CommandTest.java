package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;

import java.util.List;

public class CommandTest extends CommandNodeBukkit {

    public CommandTest(LangHelper plugin,
                       ConfigManager configManager,
                       DefaultTranslateManager defaultTranslateManager,
                       CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "test", "langhelper.command.test");

        addNode(new CommandTestAll(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestBiome(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestEnchant(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestEnchantLevel(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestEntity(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestItem(plugin, configManager, defaultTranslateManager, root));
        addNode(new CommandTestPotionEffect(plugin, configManager, defaultTranslateManager, root));
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test §8-§6 test translations");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sendHelp(sender);
    }

}
