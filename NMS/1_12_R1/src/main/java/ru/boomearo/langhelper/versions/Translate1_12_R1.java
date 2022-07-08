package ru.boomearo.langhelper.versions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_12_R1.*;

public class Translate1_12_R1 extends DefaultTranslateManager {

    public Translate1_12_R1(JavaPlugin javaPlugin) {
        super("1.12.2", javaPlugin);
    }

    @Override
    public String getItemName(ItemStack item, LangType langType) {
        if (item == null) {
            throw new IllegalArgumentException("item является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        try {
            net.minecraft.server.v1_12_R1.ItemStack itemStack = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);

            String name;
            Item i = itemStack.getItem();
            if (i instanceof ItemBanner) {
                String str = "item.banner.";
                EnumColor enumColor = EnumColor.fromInvColorIndex(itemStack.getData() & 0xF);
                name = str + enumColor.d() + ".name";
            }
            else if (i instanceof ItemSplashPotion) {
                name = PotionUtil.d(itemStack).b("splash_potion.effect.");
            }
            else if (i instanceof ItemLingeringPotion) {
                name = PotionUtil.d(itemStack).b("lingering_potion.effect.");
            }
            else if (i instanceof ItemPotion) {
                name = PotionUtil.d(itemStack).b("potion.effect.");
            }
            else if (i instanceof ItemTippedArrow) {
                name = PotionUtil.d(itemStack).b("tipped_arrow.effect.");
            }
            else {
                name = i.a(itemStack) + ".name";
            }

            return getTranslate(name, langType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEntityName(EntityType entityType, LangType langType) {
        if (entityType == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
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
    public String getEnchantName(Enchantment enchant, LangType langType) {
        if (enchant == null) {
            throw new IllegalArgumentException("enchant является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        try {
            String name = org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment.getRaw(enchant).a();
            return getTranslate(name, langType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEnchantLevelName(int level, LangType langType) {
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("enchantment.level." + level, langType);
    }

    @Override
    public String getPotionEffectName(PotionEffectType potionEffectType, LangType langType) {
        if (potionEffectType == null) {
            throw new IllegalArgumentException("effect является null!");
        }
        if (langType == null) {
            throw new IllegalArgumentException("type является null!");
        }
        String effectName = potionEffectType.getName().toLowerCase();
        switch (effectName) {
            case "speed":
                effectName = "moveSpeed";
                break;
            case "fast_digging":
                effectName = "digSpeed";
                break;
            case "slow_digging":
                effectName = "digSlowDown";
                break;
            case "damage_resistance":
                effectName = "resistance";
                break;
            case "slow":
                effectName = "moveSlowdown";
                break;
            case "increase_damage":
                effectName = "damageBoost";
                break;
        }
        String name = "effect." + effectName;
        return getTranslate(name, langType);
    }

    //TODO К сожалению 1.12.2 не имеет строк для перевода биомов.
    @Override
    public String getBiomeName(Biome biome, LangType langType) {
        return null;
    }

    @Override
    protected ConcurrentMap<String, String> parseTranslate(InputStream stream) {
        ConcurrentMap<String, String> translates = new ConcurrentHashMap<>();
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
                    translates.put(args[0].toLowerCase().replace("_", ""), args[1]);
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return translates;
    }

}
