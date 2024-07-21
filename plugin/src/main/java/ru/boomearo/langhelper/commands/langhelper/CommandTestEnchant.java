package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.LangType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestEnchant extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestEnchant(LangHelper plugin,
                              ConfigManager configManager,
                              DefaultTranslateManager defaultTranslateManager,
                              CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "enchant", "langhelper.command.test.enchant");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test enchant <language> <type> §8-§6 test enchant type");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendCurrentHelp(sender);
            return;
        }

        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }

        if (laType == null) {
            sender.sendMessage("Invalid language!");
            return;
        }

        Enchantment enType = null;
        try {
            enType = Enchantment.getByName(args[1].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }
        if (enType == null) {
            sender.sendMessage("Invalid enchantment!");
            return;
        }

        sender.sendMessage("Enchantment: " + this.defaultTranslateManager.getEnchantmentName(enType, laType));
    }

    @Override
    public List<String> onSuggest(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> matches = new ArrayList<>();
            String search = args[0].toLowerCase(Locale.ROOT);
            for (LangType lt : this.defaultTranslateManager.getAllTranslateLang()) {
                if (lt.getName().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(lt.getName());
                }
            }
            return matches;
        } else if (args.length == 2) {
            List<String> matches = new ArrayList<>();
            String search = args[1].toLowerCase(Locale.ROOT);
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.getName().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(enchantment.getName());
                }
            }
            return matches;
        }

        return List.of();
    }

}
