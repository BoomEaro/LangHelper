package ru.boomearo.langhelper.versions;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public class Translate1_12_R1 extends DefaultTranslateManager {

    public Translate1_12_R1(Plugin plugin) {
        super("1.12.2", plugin);
    }

    @Override
    public String getItemName(ItemStack item, LangType langType) {
        Preconditions.checkArgument(item != null);
        Preconditions.checkArgument(langType != null);

        try {
            net.minecraft.server.v1_12_R1.ItemStack itemStack = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.getItem();
            if (i instanceof ItemBanner) {
                String str = "item.banner.";
                EnumColor enumColor = EnumColor.fromInvColorIndex(itemStack.getData() & 0xF);
                name = str + enumColor.d() + ".name";
            } else if (i instanceof ItemSplashPotion) {
                name = PotionUtil.d(itemStack).b("splash_potion.effect.");
            } else if (i instanceof ItemLingeringPotion) {
                name = PotionUtil.d(itemStack).b("lingering_potion.effect.");
            } else if (i instanceof ItemPotion) {
                name = PotionUtil.d(itemStack).b("potion.effect.");
            } else if (i instanceof ItemTippedArrow) {
                name = PotionUtil.d(itemStack).b("tipped_arrow.effect.");
            } else {
                name = i.a(itemStack) + ".name";
            }

            return getTranslate(name, langType);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get item name for " + item, e);
            return null;
        }
    }

    @Override
    public String getEntityName(EntityType entityType, LangType langType) {
        Preconditions.checkArgument(entityType != null);
        Preconditions.checkArgument(langType != null);

        // TODO Нужно ли править?

        // Следующие ниже сущности имеют СОВСЕМ другой ключ перевода.. Похоже в 1.12.2 нет nms енумов и все типы сущностей это классы.
        // К тому же, ключ перевода реализован у каждой реализации сущности свой, а это значит придется передавать сюда обьект entity что не очень удобно.

        // AREA_EFFECT_CLOUD, EGG, LEASH_HITCH, ENDER_PEARL, ENDER_SIGNAL, SPLASH_POTION, THROWN_EXP_BOTTLE, ITEM_FRAME, WITHER_SKULL, PRIMED_TNT, FALLING_BLOCK, FIREWORK,
        // SPECTRAL_ARROW, SHULKER_BULLET, EVOKER_FANGS, MINECART_COMMAND, MINECART_CHEST, MINECART_FURNACE, MINECART_TNT, MINECART_HOPPER, MINECART_MOB_SPAWNER, PIG_ZOMBIE,
        // MAGMA_CUBE, WITHER, MUSHROOM_COW, OCELOT, LLAMA_SPIT, ENDER_CRYSTAL, LINGERING_POTION, FISHING_HOOK, LIGHTNING, WEATHER, PLAYER, COMPLEX_PART, TIPPED_ARROW

        // Мне лень делать switch/case для каждого перевода, да и нужно ли оно?
        return getTranslate("entity." + entityType.name() + ".name", langType);
    }

    @Override
    public String getEnchantmentName(Enchantment enchant, LangType langType) {
        Preconditions.checkArgument(enchant != null);
        Preconditions.checkArgument(langType != null);

        try {
            String name = org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment.getRaw(enchant).a();
            return getTranslate(name, langType);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get enchantment " + enchant, e);
            return null;
        }
    }

    @Override
    public String getEnchantmentLevelName(int level, LangType langType) {
        Preconditions.checkArgument(langType != null);

        return getTranslate("enchantment.level." + level, langType);
    }

    @Override
    public String getPotionEffectName(PotionEffectType potionEffectType, LangType langType) {
        Preconditions.checkArgument(potionEffectType != null);
        Preconditions.checkArgument(langType != null);

        String effectName = switch (potionEffectType.getName().toLowerCase(Locale.ROOT)) {
            case "speed" -> "moveSpeed";
            case "fast_digging" -> "digSpeed";
            case "slow_digging" -> "digSlowDown";
            case "damage_resistance" -> "resistance";
            case "slow" -> "moveSlowdown";
            case "increase_damage" -> "damageBoost";
            default -> potionEffectType.getName().toLowerCase(Locale.ROOT);
        };

        String name = "effect." + effectName;
        return getTranslate(name, langType);
    }

    //TODO К сожалению 1.12.2 не имеет строк для перевода биомов.
    @Override
    public String getBiomeName(Biome biome, LangType langType) {
        return null;
    }

    @Override
    protected Map<String, String> parseTranslate(InputStream stream) {
        Map<String, String> translates = new HashMap<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                String[] args = line.split("=");
                if (args.length >= 2) {
                    translates.put(args[0].toLowerCase(Locale.ROOT).replace("_", ""), args[1]);
                }
            }


        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to parse translation", e);
        }

        return Collections.unmodifiableMap(translates);
    }

}
