package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestItem extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestItem(LangHelper plugin,
                           ConfigManager configManager,
                           DefaultTranslateManager defaultTranslateManager,
                           CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "item", "langhelper.command.test.item");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test item <language> §8-§6 test current holding item");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sendCurrentHelp(sender);
            return;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only for players.");
            return;
        }

        LangType type = null;
        try {
            type = LangType.valueOf(args[0].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }

        if (type == null) {
            sender.sendMessage("Invalid language type!");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null) {
            sender.sendMessage("Item is not found!");
            return;
        }

        sender.sendMessage("Item: " + this.defaultTranslateManager.getItemName(item, type));
    }

    @Override
    public List<String> onSuggest(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            String search = args[0].toLowerCase(Locale.ROOT);
            for (LangType lt : this.defaultTranslateManager.getAllTranslateLang()) {
                if (lt.name().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(lt.name().toLowerCase(Locale.ROOT));
                }
            }
            return matches;
        }

        return List.of();
    }

}
