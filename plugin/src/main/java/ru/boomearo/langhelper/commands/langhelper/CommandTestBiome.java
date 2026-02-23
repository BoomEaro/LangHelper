package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.api.LangType;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestBiome extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestBiome(LangHelper plugin,
                            ConfigManager configManager,
                            DefaultTranslateManager defaultTranslateManager,
                            CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "biome", "langhelper.command.test.biome");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test biome <language> <biome> §8-§6 test biome");
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

        Biome biome = null;
        try {
            biome = Biome.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }
        if (biome == null) {
            sender.sendMessage("Invalid biome!");
            return;
        }

        sender.sendMessage("Biome: " + this.defaultTranslateManager.getBiomeName(biome, laType));
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
        } else if (args.length == 2) {
            List<String> matches = new ArrayList<>();
            String search = args[1].toLowerCase(Locale.ROOT);
            for (Biome bi : Biome.values()) {
                if (bi.name().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(bi.name());
                }
            }
            return matches;
        }

        return List.of();
    }

}
