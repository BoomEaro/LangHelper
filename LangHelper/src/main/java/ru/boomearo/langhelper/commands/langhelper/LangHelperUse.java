package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CmdInfo;
import ru.boomearo.langhelper.versions.LangType;
import ru.boomearo.langhelper.versions.exceptions.LangException;

public class LangHelperUse {

    @CmdInfo(name = "reload", description = "Перезагрузить конфигурацию.", usage = "/langhelper reload", permission = "langhelper.admin")
    public boolean reload(CommandSender cs, String[] args) {
        if (args.length != 0) {
            return false;
        }

        try {
            LangHelper lh = LangHelper.getInstance();
            lh.loadConfigData();
            lh.checkAndDownloadLanguages();

            lh.getAbstractTranslateManager().loadLanguages(LangHelper.getLanguageFolder(), lh.getEnabledLanguages());

            cs.sendMessage("Конфигурация успешно перезагружена!");
        }
        catch (LangException e) {
            cs.sendMessage("Ошибка: " + e.getMessage());
        }

        return true;
    }

    @CmdInfo(name = "testitem", description = "Протестировать название указанного предмета.", usage = "/langhelper testitem <язык>", permission = "langhelper.admin")
    public boolean testitem(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("Данная команда только для игроков.");
            return true;
        }
        if (args.length != 1) {
            return false;
        }

        LangType type = null;
        try {
            type = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception e) {}

        if (type == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Player pl = (Player) cs;

        ItemStack item = pl.getInventory().getItemInMainHand();
        if (item == null) {
            cs.sendMessage("В основной руке нет предметов!");
            return true;
        }

        cs.sendMessage("Предмет: " + LangHelper.getInstance().getAbstractTranslateManager().getItemName(item, type));
        return true;
    }

    @CmdInfo(name = "testentity", description = "Протестировать название сущности.", usage = "/langhelper testentity <язык> <сущность>", permission = "langhelper.admin")
    public boolean testentity(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }


        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception e) {}

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        EntityType enType = null;
        try {
            enType = EntityType.valueOf(args[1].toUpperCase());
        }
        catch (Exception e) {
        }
        if (enType == null) {
            cs.sendMessage("Указанная сущность не найдена!");
            return true;
        }

        cs.sendMessage("Сущность: " + LangHelper.getInstance().getAbstractTranslateManager().getEntityName(enType, laType));
        return true;
    }

    @CmdInfo(name = "testenchant", description = "Протестировать название зачарования.", usage = "/langhelper testenchant <язык> <зачарование>", permission = "langhelper.admin")
    public boolean testenchant(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }

        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception e) {}

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Enchantment enType = null;
        try {
            enType = Enchantment.getByName(args[1].toUpperCase());
        }
        catch (Exception e) {
        }
        if (enType == null) {
            cs.sendMessage("Указанное зачарование не найдено!");
            return true;
        }

        cs.sendMessage("Зачарование: " + LangHelper.getInstance().getAbstractTranslateManager().getEnchantName(enType, laType));
        return true;
    }

    @CmdInfo(name = "testenchantlevel", description = "Протестировать название уровней зачарования.", usage = "/langhelper testenchantlevel <язык> <уровень>", permission = "langhelper.admin")
    public boolean testenchantlevel(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }

        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception e) {}

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Integer enType = null;
        try {
            enType = Integer.parseInt(args[1]);
        }
        catch (Exception e) {
        }
        if (enType == null) {
            cs.sendMessage("Указанный уровень зачарования не найден!");
            return true;
        }

        cs.sendMessage("Зачарование: " + LangHelper.getInstance().getAbstractTranslateManager().getEnchantLevelName(enType, laType));
        return true;
    }

    @CmdInfo(name = "testpotioneffect", description = "Протестировать название эффекта зелья.", usage = "/langhelper testpotioneffect <язык> <зачарование>", permission = "langhelper.admin")
    public boolean testpotioneffect(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }

        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception e) {}

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        PotionEffectType efType = null;
        try {
            efType = PotionEffectType.getByName(args[1].toUpperCase());
        }
        catch (Exception e) {
        }
        if (efType == null) {
            cs.sendMessage("Указанный тип зелья не найден!");
            return true;
        }

        cs.sendMessage("Тип зелья: " + LangHelper.getInstance().getAbstractTranslateManager().getPotionEffectName(efType, laType));
        return true;
    }
}
