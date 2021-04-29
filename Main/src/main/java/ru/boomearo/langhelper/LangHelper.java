package ru.boomearo.langhelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ru.boomearo.langhelper.commands.langhelper.CmdExecutorLangHelper;
import ru.boomearo.langhelper.versions.AbstractTranslateManager;
import ru.boomearo.langhelper.versions.LangType;
import ru.boomearo.langhelper.versions.Translate;
import ru.boomearo.langhelper.versions.Translate1_12_R1;
import ru.boomearo.langhelper.versions.Translate1_13_R2;
import ru.boomearo.langhelper.versions.Translate1_16_R3;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;

    private static LangHelper instance = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    @Override
    public void onEnable() {
        instance = this;

        this.version = matchVersion(getLanguageFolder());

        if (this.version != null) {
            for (Translate tra : this.version.getAllTranslate()) {
                this.getLogger().info("Язык '" + tra.getLangType().name() + "' успешно загружен. Количество строк: " + tra.getAllTranslate().size());
            }
        }

        getCommand("langhelper").setExecutor(new CmdExecutorLangHelper());

        this.getLogger().info("Плагин успешно запущен!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Плагин успешно выключен!");
    }

    public AbstractTranslateManager getAbstractTranslateManager() {
        return this.version;
    }

    private static String capitalize(String name) {
        String material = name.replace("_", " ").toLowerCase();

        StringBuilder sb = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = material.split(" ")).length;
        for (int i = 0; i < j; i++) {
            String word = arrayOfString[i];
            sb.append(" ");
            if (i == 0) {
                sb.append(word.toUpperCase().charAt(0));
                sb.append(word.substring(1));
            }
            else {
                sb.append(word.substring(0));  
            }
        }
        return sb.toString().substring(1);
    }

    public String getEnchantmentName(Enchantment enc, LangType lang) {
        String name = this.version.getEnchantName(enc, lang);
        if (name == null) {
            name = capitalize(enc.getName());
        }
        return name;
    }

    public String getItemTranslate(ItemStack item, LangType lang) {
        String name = this.version.getItemName(item, lang);
        if (name == null) {
            short dur = item.getDurability();
            name = capitalize(item.getType().name()) + (dur > 0 ? ":" + dur : "");
        }
        return name;
    }

    public String getEnchantLevelTranslate(int level, LangType lang) {
        String name = this.version.getEnchantLevelName(level, lang);
        if (name == null) {
            name = "" + level;
        }
        return name;
    }

    public String getEntityTranslate(EntityType entity, LangType lang) {
        String name = this.version.getEntityName(entity, lang);
        if (name == null) {
            name = capitalize(entity.name());
        }
        return name;
    }

    public static LangHelper getInstance() { 
        return instance;
    }

    public static File getLanguageFolder() {
        return new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
    }

    private final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2.class,
            Translate1_16_R3.class
            );

    public AbstractTranslateManager matchVersion(File file) {
        try {
            return this.versions.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new Exception("Плагин не поддерживает данную версию сервера!")).
                    getConstructor(File.class).
                    newInstance(file);
        } 
        catch (Exception ex) {
            this.getLogger().severe(ex.getMessage());
        }
        return null;
    }
}
