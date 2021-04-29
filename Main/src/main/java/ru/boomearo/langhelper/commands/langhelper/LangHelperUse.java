package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CmdInfo;
import ru.boomearo.langhelper.versions.LangType;

public class LangHelperUse {

    @CmdInfo(name = "reload", description = "Перезагрузить конфигурацию.", usage = "/langhelper reload", permission = "langhelper.admin")
    public boolean reload(CommandSender cs, String[] args) {
        if (args.length < 0 || args.length > 0) {
            return false;
        }

        LangHelper.getInstance().getAbstractTranslateManager().loadTranslateFromDisk(LangHelper.getLanguageFolder());

        cs.sendMessage("Конфигурация успешно перезагружена!");

        return true;
    }

    @CmdInfo(name = "testitem", description = "Протестировать название указанного предмета.", usage = "/langhelper testitem", permission = "langhelper.admin")
    public boolean testitem(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("Данная команда только для игроков.");
            return true;
        }
        if (args.length < 0 || args.length > 0) {
            return false;
        }

        Player pl = (Player) cs;

        ItemStack item = pl.getInventory().getItemInMainHand();
        if (item == null) {
            cs.sendMessage("В основной руке нет предметов!");
            return true;
        }

        cs.sendMessage("Предмет: " + LangHelper.getInstance().getAbstractTranslateManager().getItemName(item, LangType.RU));
        return true;
    }

    @CmdInfo(name = "testentity", description = "Протестировать название сущности.", usage = "/langhelper testentity <сущность>", permission = "langhelper.admin")
    public boolean testentity(CommandSender cs, String[] args) {
        if (args.length < 1 || args.length > 1) {
            return false;
        }

        String arg = args[0].toUpperCase();

        EntityType type = null;
        try {
            type = EntityType.valueOf(arg);
        }
        catch (Exception e) {}
        if (type == null) {
            cs.sendMessage("Указанная сущность не найдена!");
            return true;
        }

        cs.sendMessage("Сущность: " + LangHelper.getInstance().getAbstractTranslateManager().getEntityName(type, LangType.RU));
        return true;
    }

    @CmdInfo(name = "testenchant", description = "Протестировать название зачарования.", usage = "/langhelper testenchant <зачарование>", permission = "langhelper.admin")
    public boolean testenchant(CommandSender cs, String[] args) {
        if (args.length < 1 || args.length > 1) {
            return false;
        }

        String arg = args[0].toUpperCase();

        Enchantment type = null;
        try {
            type = Enchantment.getByName(arg);
        }
        catch (Exception e) {}
        if (type == null) {
            cs.sendMessage("Указанное зачарование не найдено!");
            return true;
        }

        cs.sendMessage("Зачарование: " + LangHelper.getInstance().getAbstractTranslateManager().getEnchantName(type, LangType.RU));
        return true;
    }

    @CmdInfo(name = "testenchantlevel", description = "Протестировать название уровней зачарования.", usage = "/langhelper testenchantlevel <уровни>", permission = "langhelper.admin")
    public boolean testenchantlevel(CommandSender cs, String[] args) {
        if (args.length < 1 || args.length > 1) {
            return false;
        }

        String arg = args[0].toUpperCase();

        Integer type = null;
        try {
            type = Integer.parseInt(arg);
        }
        catch (Exception e) {}
        if (type == null) {
            cs.sendMessage("Указанный уровень зачарования не найден!");
            return true;
        }

        cs.sendMessage("Зачарование: " + LangHelper.getInstance().getAbstractTranslateManager().getEnchantLevelName(type, LangType.RU));
        return true;
    }
}
