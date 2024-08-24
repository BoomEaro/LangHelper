package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.LangType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandTestEntity extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestEntity(LangHelper plugin,
                             ConfigManager configManager,
                             DefaultTranslateManager defaultTranslateManager,
                             CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "entity", "langhelper.command.test.entity");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test entity <language> <type> §8-§6 test entity type");
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

        EntityType enType = null;
        try {
            enType = EntityType.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
        }
        if (enType == null) {
            sender.sendMessage("Invalid entity type!");
            return;
        }

        sender.sendMessage("Entity: " + this.defaultTranslateManager.getEntityName(enType, laType));
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
            for (EntityType et : EntityType.values()) {
                if (et.name().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(et.name());
                }
            }
            return matches;
        }

        return List.of();
    }
}
