package ru.boomearo.langhelper.versions;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
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

    private static final Map<Material, Material> BLOCK_ALIASES;
    private static final Map<Material, String> MISSING_KEYS;

    static {
        Map<Material, Material> BLOCK_ALIASES_TMP = new HashMap<>();

        BLOCK_ALIASES_TMP.put(Material.STATIONARY_WATER, Material.WATER);
        BLOCK_ALIASES_TMP.put(Material.STATIONARY_LAVA, Material.LAVA);

        BLOCK_ALIASES_TMP.put(Material.BED_BLOCK, Material.BED);

        BLOCK_ALIASES_TMP.put(Material.PISTON_MOVING_PIECE, Material.PISTON_BASE);
        BLOCK_ALIASES_TMP.put(Material.PISTON_EXTENSION, Material.PISTON_BASE);

        BLOCK_ALIASES_TMP.put(Material.DOUBLE_STEP, Material.STEP);

        BLOCK_ALIASES_TMP.put(Material.REDSTONE_WIRE, Material.REDSTONE);

        BLOCK_ALIASES_TMP.put(Material.CROPS, Material.WHEAT);

        BLOCK_ALIASES_TMP.put(Material.BURNING_FURNACE, Material.FURNACE);

        BLOCK_ALIASES_TMP.put(Material.SIGN_POST, Material.SIGN);

        BLOCK_ALIASES_TMP.put(Material.WOODEN_DOOR, Material.WOOD_DOOR);

        BLOCK_ALIASES_TMP.put(Material.WALL_SIGN, Material.SIGN);

        BLOCK_ALIASES_TMP.put(Material.IRON_DOOR_BLOCK, Material.IRON_DOOR);

        BLOCK_ALIASES_TMP.put(Material.GLOWING_REDSTONE_ORE, Material.REDSTONE_ORE);

        BLOCK_ALIASES_TMP.put(Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON);

        BLOCK_ALIASES_TMP.put(Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE);

        BLOCK_ALIASES_TMP.put(Material.CAKE_BLOCK, Material.CAKE);

        BLOCK_ALIASES_TMP.put(Material.DIODE_BLOCK_OFF, Material.DIODE);
        BLOCK_ALIASES_TMP.put(Material.DIODE_BLOCK_ON, Material.DIODE);

        BLOCK_ALIASES_TMP.put(Material.PUMPKIN_STEM, Material.PUMPKIN_SEEDS);

        BLOCK_ALIASES_TMP.put(Material.MELON_STEM, Material.MELON_SEEDS);

        BLOCK_ALIASES_TMP.put(Material.NETHER_WARTS, Material.NETHER_STALK);

        BLOCK_ALIASES_TMP.put(Material.BREWING_STAND, Material.BREWING_STAND_ITEM);

        BLOCK_ALIASES_TMP.put(Material.CAULDRON, Material.CAULDRON_ITEM);

        BLOCK_ALIASES_TMP.put(Material.ENDER_PORTAL, Material.ENDER_PORTAL_FRAME);

        BLOCK_ALIASES_TMP.put(Material.REDSTONE_LAMP_ON, Material.REDSTONE_LAMP_OFF);

        BLOCK_ALIASES_TMP.put(Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP);

        BLOCK_ALIASES_TMP.put(Material.COCOA, Material.INK_SACK);

        BLOCK_ALIASES_TMP.put(Material.TRIPWIRE, Material.TRIPWIRE_HOOK);

        BLOCK_ALIASES_TMP.put(Material.FLOWER_POT, Material.FLOWER_POT_ITEM);

        BLOCK_ALIASES_TMP.put(Material.CARROT, Material.CARROT_ITEM);

        BLOCK_ALIASES_TMP.put(Material.POTATO, Material.POTATO_ITEM);

        BLOCK_ALIASES_TMP.put(Material.SKULL, Material.SKULL_ITEM);

        BLOCK_ALIASES_TMP.put(Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR);
        BLOCK_ALIASES_TMP.put(Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR);

        BLOCK_ALIASES_TMP.put(Material.STANDING_BANNER, Material.BANNER);
        BLOCK_ALIASES_TMP.put(Material.WALL_BANNER, Material.BANNER);

        BLOCK_ALIASES_TMP.put(Material.DAYLIGHT_DETECTOR_INVERTED, Material.DAYLIGHT_DETECTOR);

        BLOCK_ALIASES_TMP.put(Material.DOUBLE_STONE_SLAB2, Material.STONE_SLAB2);

        BLOCK_ALIASES_TMP.put(Material.SPRUCE_DOOR, Material.SPRUCE_DOOR_ITEM);
        BLOCK_ALIASES_TMP.put(Material.BIRCH_DOOR, Material.BIRCH_DOOR_ITEM);
        BLOCK_ALIASES_TMP.put(Material.JUNGLE_DOOR, Material.JUNGLE_DOOR_ITEM);
        BLOCK_ALIASES_TMP.put(Material.ACACIA_DOOR, Material.ACACIA_DOOR_ITEM);
        BLOCK_ALIASES_TMP.put(Material.DARK_OAK_DOOR, Material.DARK_OAK_DOOR_ITEM);

        BLOCK_ALIASES_TMP.put(Material.PURPUR_DOUBLE_SLAB, Material.PURPUR_SLAB);

        BLOCK_ALIASES_TMP.put(Material.BEETROOT_BLOCK, Material.BEETROOT);

        BLOCK_ALIASES_TMP.put(Material.FROSTED_ICE, Material.ICE);

        BLOCK_ALIASES = BLOCK_ALIASES_TMP;

        Map<Material, String> MISSING_KEYS_TMP = new HashMap<>();

        MISSING_KEYS_TMP.put(Material.WATER, "tile.water.name");
        MISSING_KEYS_TMP.put(Material.LAVA, "tile.lava.name");
        MISSING_KEYS_TMP.put(Material.FIRE, "tile.fire.name");
        MISSING_KEYS_TMP.put(Material.PORTAL, "tile.portal.name");

        MISSING_KEYS = MISSING_KEYS_TMP;

    }

    public Translate1_12_R1(Plugin plugin) {
        super("1.12.2", plugin);
    }

    @Override
    public String getItemName(ItemStack item, LangType langType) {
        Preconditions.checkArgument(item != null);
        Preconditions.checkArgument(langType != null);

        Material newType = BLOCK_ALIASES.get(item.getType());
        if (newType != null) {
            item = item.clone();
            item.setType(newType);
        }

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

            if (item.getType() != Material.AIR) {
                if (name.equals("tile.air.name")) {
                    name = MISSING_KEYS.get(item.getType());
                    if (name == null) {
                        return null;
                    }
                }
            }

            // TODO Items without keys:
            // END_GATEWAY

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

        try {
            String originalName = entityType.getName();
            if (originalName == null) {
                originalName = entityType.name().toLowerCase(Locale.ROOT);
            }

            String name = EntityTypes.a(new MinecraftKey(originalName));
            if (name == null) {
                return null;
            }

            // TODO Entities without keys:
            // AREA_EFFECT_CLOUD,
            // EGG,
            // LEASH_HITCH,
            // ENDER_PEARL,
            // ENDER_SIGNAL,
            // THROWN_EXP_BOTTLE,
            // ITEM_FRAME,
            // WITHER_SKULL,
            // FIREWORK,
            // SPECTRAL_ARROW,
            // SHULKER_BULLET,
            // EVOKER_FANGS,
            // MINECART_COMMAND,
            // MINECART,
            // MINECART_FURNACE,
            // MINECART_TNT,
            // MINECART_MOB_SPAWNER,
            // LLAMA_SPIT,
            // ENDER_CRYSTAL,
            // LINGERING_POTION,
            // FISHING_HOOK,
            // LIGHTNING,
            // WEATHER,
            // PLAYER,
            // COMPLEX_PART,
            // TIPPED_ARROW

            return getTranslate("entity." + name + ".name", langType);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get entity name for " + entityType, e);
            return null;
        }
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

    @Override
    public String getBiomeName(Biome biome, LangType langType) {
        // TODO Biomes without keys:
        // OCEAN,
        // PLAINS,
        // DESERT,
        // EXTREME_HILLS,
        // FOREST,
        // TAIGA,
        // SWAMPLAND,
        // RIVER,
        // HELL,
        // SKY,
        // FROZEN_OCEAN,
        // FROZEN_RIVER,
        // ICE_FLATS,
        // ICE_MOUNTAINS,
        // MUSHROOM_ISLAND,
        // MUSHROOM_ISLAND_SHORE,
        // BEACHES,
        // DESERT_HILLS,
        // FOREST_HILLS,
        // TAIGA_HILLS,
        // SMALLER_EXTREME_HILLS,
        // JUNGLE,
        // JUNGLE_HILLS,
        // JUNGLE_EDGE,
        // DEEP_OCEAN,
        // STONE_BEACH,
        // COLD_BEACH,
        // BIRCH_FOREST,
        // BIRCH_FOREST_HILLS,
        // ROOFED_FOREST,
        // TAIGA_COLD,
        // TAIGA_COLD_HILLS,
        // REDWOOD_TAIGA,
        // REDWOOD_TAIGA_HILLS,
        // EXTREME_HILLS_WITH_TREES,
        // SAVANNA,
        // SAVANNA_ROCK,
        // MESA,
        // MESA_ROCK,
        // MESA_CLEAR_ROCK,
        // VOID,
        // MUTATED_PLAINS,
        // MUTATED_DESERT,
        // MUTATED_EXTREME_HILLS,
        // MUTATED_FOREST,
        // MUTATED_TAIGA,
        // MUTATED_SWAMPLAND,
        // MUTATED_ICE_FLATS,
        // MUTATED_JUNGLE,
        // MUTATED_JUNGLE_EDGE,
        // MUTATED_BIRCH_FOREST,
        // MUTATED_BIRCH_FOREST_HILLS,
        // MUTATED_ROOFED_FOREST,
        // MUTATED_TAIGA_COLD,
        // MUTATED_REDWOOD_TAIGA,
        // MUTATED_REDWOOD_TAIGA_HILLS,
        // MUTATED_EXTREME_HILLS_WITH_TREES,
        // MUTATED_SAVANNA,
        // MUTATED_SAVANNA_ROCK,
        // MUTATED_MESA,
        // MUTATED_MESA_ROCK,
        // MUTATED_MESA_CLEAR_ROCK
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
