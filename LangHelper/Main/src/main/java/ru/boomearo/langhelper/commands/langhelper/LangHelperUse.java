package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CmdInfo;
import ru.boomearo.langhelper.versions.LangType;

public class LangHelperUse {

    @CmdInfo(name = "reload", description = "Перезагрузить конфигурацию.", usage = "/langhelper reload", permission = "langhelper.reload")
    public boolean reload(CommandSender cs, String[] args) {
        if (args.length < 0 || args.length > 0) {
            return false;
        }

        LangHelper.getInstance().getAbstractTranslateManager().loadTranslateFromDisk(LangHelper.getLanguageFolder());

        cs.sendMessage("Конфигурация успешно перезагружена!");

        return true;
    }

    @CmdInfo(name = "testitem", description = "Перезагрузить конфигурацию", usage = "/langhelper testitem", permission = "langhelper.testitem")
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


}
