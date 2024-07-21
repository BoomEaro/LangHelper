package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.LangType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestEnchantLevel extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestEnchantLevel(LangHelper plugin,
                                   ConfigManager configManager,
                                   DefaultTranslateManager defaultTranslateManager,
                                   CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "enchantlevel", "langhelper.command.test.enchantlevel");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test enchantlevel <language> <level> §8-§6 test enchant level");
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

        Integer enType = null;
        try {
            enType = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
        }
        if (enType == null) {
            sender.sendMessage("Invalid number!");
            return;
        }

        sender.sendMessage("Enchantment level: " + this.defaultTranslateManager.getEnchantmentLevelName(enType, laType));
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
            for (int i = 0; i <= 10; i++) {
                String value = "" + i;
                if (value.toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(value);
                }
            }
            return matches;
        }

        return List.of();
    }

}
