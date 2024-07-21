package ru.boomearo.langhelper.commands;

import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.managers.ConfigManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public abstract class CommandNodeBukkit extends CommandNode<CommandSender, String> {

    protected final LangHelper plugin;
    protected final ConfigManager configManager;
    protected final String permission;

    public CommandNodeBukkit(LangHelper plugin, ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases, String permission) {
        super(root, name, aliases);
        this.plugin = plugin;
        this.configManager = configManager;
        this.permission = permission;
    }

    public CommandNodeBukkit(LangHelper plugin, ConfigManager configManager, CommandNodeBukkit root, String name, String permission) {
        this(plugin, configManager, root, name, Collections.emptyList(), permission);
    }

    public CommandNodeBukkit(LangHelper plugin, ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases) {
        this(plugin, configManager, root, name, aliases, null);
    }

    public CommandNodeBukkit(LangHelper plugin, ConfigManager configManager, CommandNodeBukkit root, String name) {
        this(plugin, configManager, root, name, Collections.emptyList());
    }

    @Override
    public void onExecuteException(CommandSender sender, String[] args, Exception e) {
        this.plugin.getLogger().log(Level.SEVERE, "Failed to execute command", e);
    }

    @Override
    public Collection<String> onSuggestException(CommandSender sender, String[] args, Exception e) {
        this.plugin.getLogger().log(Level.SEVERE, "Failed to execute tab complete", e);
        return Collections.emptyList();
    }

    @Override
    public void onPermissionFailedExecute(CommandSender sender, String[] args) {
        sender.sendMessage(this.configManager.getMessage("do_not_have_permissions"));
    }

    @Override
    public Collection<String> onPermissionFailedSuggest(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        if (this.permission == null) {
            return true;
        }
        return sender.hasPermission(this.permission);
    }

    public void sendCurrentHelp(CommandSender sender) {
        List<String> description = getDescription(sender);
        if (description != null) {
            for (String text : description) {
                sender.sendMessage(text);
            }
        }
    }

    public void sendHelp(CommandSender sender) {
        for (String text : getDescriptionListFromRoot(sender)) {
            sender.sendMessage(text);
        }
    }
}
