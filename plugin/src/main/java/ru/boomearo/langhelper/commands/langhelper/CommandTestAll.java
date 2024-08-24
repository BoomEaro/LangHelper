package ru.boomearo.langhelper.commands.langhelper;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.commands.CommandNodeBukkit;
import ru.boomearo.langhelper.managers.ConfigManager;
import ru.boomearo.langhelper.versions.DefaultTranslateManager;
import ru.boomearo.langhelper.versions.LangType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class CommandTestAll extends CommandNodeBukkit {

    private final DefaultTranslateManager defaultTranslateManager;

    public CommandTestAll(LangHelper plugin,
                          ConfigManager configManager,
                          DefaultTranslateManager defaultTranslateManager,
                          CommandNodeBukkit root
    ) {
        super(plugin, configManager, root, "all", "langhelper.command.test.all");
        this.defaultTranslateManager = defaultTranslateManager;
    }

    @Override
    public List<String> getDescription(CommandSender sender) {
        return List.of("/langhelper test all <language> <debug> §8-§6 test all languages");
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendCurrentHelp(sender);
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

        Boolean debug = null;
        try {
            debug = Boolean.parseBoolean(args[1]);
        } catch (Exception ignored) {
        }

        if (debug == null) {
            sender.sendMessage("Expected argument true or false!");
            return;
        }

        sender.sendMessage("Checking translation " + type.name() + "..");

        {
            List<Material> failed = new ArrayList<>();
            for (Material mat : Material.values()) {
                if (!mat.isItem()) {
                    continue;
                }

                try {
                    String name = this.defaultTranslateManager.getItemName(new ItemStack(mat, 1), type);
                    if (name == null) {
                        failed.add(mat);
                    }

                    if (debug) {
                        sender.sendMessage(mat + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on item getting " + mat + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get item", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All items are present in translation." : "The following items are not translated: " + failed));
        }
        {
            List<EntityType> failed = new ArrayList<>();
            for (EntityType en : EntityType.values()) {
                try {
                    if (en == EntityType.UNKNOWN) {
                        continue;
                    }
                    String name = this.defaultTranslateManager.getEntityName(en, type);
                    if (name == null) {
                        failed.add(en);
                    }

                    if (debug) {
                        sender.sendMessage(en + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on entity getting " + en + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get entity", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All entities are present in the translation." : "The following entities are not translated: " + failed));
        }
        {
            List<Enchantment> failed = new ArrayList<>();
            for (Enchantment en : Enchantment.values()) {
                try {
                    String name = this.defaultTranslateManager.getEnchantmentName(en, type);
                    if (name == null) {
                        failed.add(en);
                    }

                    if (debug) {
                        sender.sendMessage(en + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on getting enchantment " + en + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get enchantment", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All the enchantments are present in the translation." : "The following enchantments are not translated: " + failed));
        }
        {
            List<Integer> failed = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                try {
                    String name = this.defaultTranslateManager.getEnchantmentLevelName(i, type);
                    if (name == null) {
                        failed.add(i);
                    }

                    if (debug) {
                        sender.sendMessage(i + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on getting enchantment level " + i + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get enchantment level", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All levels of enchantment are present in the translation." : "The following enchantment levels are not translated: " + failed));
        }
        {
            List<PotionEffectType> failed = new ArrayList<>();
            for (PotionEffectType pet : PotionEffectType.values()) {
                // Да. По каким то причинам в 1.12.2 в массиве енумов есть нулл запись...
                if (pet == null) {
                    continue;
                }

                try {
                    String name = this.defaultTranslateManager.getPotionEffectName(pet, type);
                    if (name == null) {
                        failed.add(pet);
                    }

                    if (debug) {
                        sender.sendMessage(pet + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on getting potion " + pet + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get potion", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All potion effects are present in the translation." : "The following potion effects have not been translated: " + failed));
        }
        {
            List<Biome> failed = new ArrayList<>();
            for (Biome b : Biome.values()) {
                try {
                    String name = this.defaultTranslateManager.getBiomeName(b, type);
                    if (name == null) {
                        failed.add(b);
                    }

                    if (debug) {
                        sender.sendMessage(b + " == " + name);
                    }
                } catch (Exception e) {
                    sender.sendMessage("Error on getting biome " + b + ": " + e.getMessage());

                    this.plugin.getLogger().log(Level.SEVERE, "Failed to get biome", e);
                }
            }

            sender.sendMessage((failed.isEmpty() ? "All biomes are present in the translation." : "The following biomes are not translated: " + failed));
        }

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
            for (String type : List.of("true", "false")) {
                if (type.toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(type);
                }
            }
            return matches;
        }

        return List.of();
    }

}
