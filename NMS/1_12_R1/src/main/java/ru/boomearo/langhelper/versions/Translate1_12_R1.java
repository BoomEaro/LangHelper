package ru.boomearo.langhelper.versions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Translate1_12_R1 extends AbstractTranslateManager {

    public Translate1_12_R1() {
        super("1.12.2");
    }

    @Override
    public String getItemName(ItemStack item, LangType type) {
        if (item == null) {
            throw new IllegalArgumentException("item является null!");
        }
        if (type == null) {
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
                name = itemStack.getItem().a(itemStack) + ".name";
            }

            return getTranslate(name, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEntityName(EntityType entity, LangType type) {
        if (entity == null) {
            throw new IllegalArgumentException("entity является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("entity." + entity.name() + ".name", type);
    }

    @Override
    public String getEnchantName(Enchantment enchant, LangType type) {
        if (enchant == null) {
            throw new IllegalArgumentException("enchant является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        try {
            String name = org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment.getRaw(enchant).a();
            return getTranslate(name, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEnchantLevelName(int level, LangType type) {
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        return getTranslate("enchantment.level." + level, type);
    }

    @Override
    public String getPotionEffectName(PotionEffectType effect, LangType type) {
        if (effect == null) {
            throw new IllegalArgumentException("effect является null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("type является null!");
        }
        String effectName = effect.getName().toLowerCase();
        switch (effectName) {
            case "speed": effectName = "moveSpeed"; break;
            case "fast_digging": effectName = "digSpeed"; break;
            case "slow_digging": effectName = "digSlowDown"; break;
            case "damage_resistance": effectName = "resistance"; break;
            case "slow": effectName = "moveSlowdown"; break;
            case "increase_damage": effectName = "damageBoost"; break;
        }
        String name = "effect." + effectName;
        return getTranslate(name, type);
    }

    //TODO К сожалению 1.12.2 не имеет строк для перевода биомов.
    @Override
    public String getBiomeName(Biome biome, LangType type) {
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
