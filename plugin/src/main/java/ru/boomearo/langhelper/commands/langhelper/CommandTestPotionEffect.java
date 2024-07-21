package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.LangType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestPotionEffect extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestPotionEffect(LangHelper plugin,
                                   ConfigManager configManager,
                                   DefaultTranslateManager defaultTranslateManager,
                                   CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "potioneffect", "langhelper.command.test.potioneffect");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test potioneffect <language> <potion> §8-§6 test potion effect");
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

        PotionEffectType efType = null;
        try {
            efType = PotionEffectType.getByName(args[1].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }
        if (efType == null) {
            sender.sendMessage("Invalid potion!");
            return;
        }

        sender.sendMessage("Potion: " + this.defaultTranslateManager.getPotionEffectName(efType, laType));
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
            for (PotionEffectType pet : PotionEffectType.values()) {
                if (pet == null) {
                    continue;
                }
                if (pet.getName().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(pet.getName());
                }
            }
            return matches;
        }

        return List.of();
    }

}
