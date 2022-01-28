package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.Material;
import org.bukkit.block.Biome;
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

import java.util.ArrayList;
import java.util.List;

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

    @CmdInfo(name = "testall", description = "Протестировать все названия и типы переводов.", usage = "/langhelper testall <язык> <дебаг>", permission = "langhelper.admin")
    public boolean testall(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }

        LangType type = null;
        try {
            type = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception ignored) {
        }

        if (type == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        boolean debug;

        if (args[1].equalsIgnoreCase("yes")) {
            debug = true;
        }
        else if (args[1].equalsIgnoreCase("no")) {
            debug = false;
        }
        else {
            cs.sendMessage("Ожидается аргумент yes или no!");
            return true;
        }

        cs.sendMessage("Проверяем перевод " + type.getName() + "..");

        {
            List<Material> failed = new ArrayList<>();
            for (Material mat : Material.values()) {
                try {
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getItemName(new ItemStack(mat, 1), type);
                    if (name == null) {
                        failed.add(mat);
                    }

                    if (debug) {
                        cs.sendMessage(mat + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении предмета " + mat);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все предметы присутствует в переводе." : "Следующие предметы не переведены: " + failed.toString()));
        }
        {
            List<EntityType> failed = new ArrayList<>();
            for (EntityType en : EntityType.values()) {
                try {
                    if (en == EntityType.UNKNOWN) {
                        continue;
                    }
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getEntityName(en, type);
                    if (name == null) {
                        failed.add(en);
                    }

                    if (debug) {
                        cs.sendMessage(en + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении сущности " + en);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все сущности присутствует в переводе." : "Следующие сущности не переведены: " + failed.toString()));
        }
        {
            List<Enchantment> failed = new ArrayList<>();
            for (Enchantment en : Enchantment.values()) {
                try {
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getEnchantName(en, type);
                    if (name == null) {
                        failed.add(en);
                    }

                    if (debug) {
                        cs.sendMessage(en + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении зачарования " + en);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все зачарования присутствует в переводе." : "Следующие зачарования не переведены: " + failed.toString()));
        }
        {
            List<Integer> failed = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                try {
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getEnchantLevelName(i, type);
                    if (name == null) {
                        failed.add(i);
                    }

                    if (debug) {
                        cs.sendMessage(i + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении уровня зачарования " + i);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все уровни зачарования присутствует в переводе." : "Следующие уровни зачарования не переведены: " + failed.toString()));
        }
        {
            List<PotionEffectType> failed = new ArrayList<>();
            for (PotionEffectType pet : PotionEffectType.values()) {
                //Да. По каким то причинам в 1.12.2 в массиве енумов есть нулл запись..
                if (pet == null) {
                    continue;
                }
                try {
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getPotionEffectName(pet, type);
                    if (name == null) {
                        failed.add(pet);
                    }

                    if (debug) {
                        cs.sendMessage(pet + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении эффекта зелья " + pet);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все эффекты зелий присутствует в переводе." : "Следующие эффекты зелий не переведены: " + failed.toString()));
        }
        {
            List<Biome> failed = new ArrayList<>();
            for (Biome b : Biome.values()) {
                try {
                    String name = LangHelper.getInstance().getAbstractTranslateManager().getBiomeName(b, type);
                    if (name == null) {
                        failed.add(b);
                    }

                    if (debug) {
                        cs.sendMessage(b + " == " + name);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    cs.sendMessage("Ошибка при получении биома " + b);
                }
            }

            cs.sendMessage((failed.isEmpty() ? "Все биомы присутствует в переводе." : "Следующие биомы не переведены: " + failed.toString()));
        }


        return true;
    }

    @CmdInfo(name = "testitem", description = "Протестировать название указанного предмета.", usage = "/langhelper testitem <язык>", permission = "langhelper.admin")
    public boolean testitem(CommandSender cs, String[] args) {
        if (!(cs instanceof Player pl)) {
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
        catch (Exception ignored) {
        }

        if (type == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

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
        catch (Exception ignored) {
        }

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        EntityType enType = null;
        try {
            enType = EntityType.valueOf(args[1].toUpperCase());
        }
        catch (Exception ignored) {
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
        catch (Exception ignored) {
        }

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Enchantment enType = null;
        try {
            enType = Enchantment.getByName(args[1].toUpperCase());
        }
        catch (Exception ignored) {
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
        catch (Exception ignored) {
        }

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Integer enType = null;
        try {
            enType = Integer.parseInt(args[1]);
        }
        catch (Exception ignored) {
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
        catch (Exception ignored) {
        }

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        PotionEffectType efType = null;
        try {
            efType = PotionEffectType.getByName(args[1].toUpperCase());
        }
        catch (Exception ignored) {
        }
        if (efType == null) {
            cs.sendMessage("Указанный тип зелья не найден!");
            return true;
        }

        cs.sendMessage("Тип зелья: " + LangHelper.getInstance().getAbstractTranslateManager().getPotionEffectName(efType, laType));
        return true;
    }

    @CmdInfo(name = "testbiome", description = "Протестировать название биома.", usage = "/langhelper testbiome <язык> <биом>", permission = "langhelper.admin")
    public boolean testbiome(CommandSender cs, String[] args) {
        if (args.length != 2) {
            return false;
        }

        LangType laType = null;
        try {
            laType = LangType.valueOf(args[0].toUpperCase());
        }
        catch (Exception ignored) {
        }

        if (laType == null) {
            cs.sendMessage("Не верный тип языка!");
            return true;
        }

        Biome biome = null;
        try {
            biome = Biome.valueOf(args[1].toUpperCase());
        }
        catch (Exception ignored) {
        }
        if (biome == null) {
            cs.sendMessage("Указанный тип биома не найден!");
            return true;
        }

        cs.sendMessage("Тип биома: " + LangHelper.getInstance().getAbstractTranslateManager().getBiomeName(biome, laType));
        return true;
    }
}
