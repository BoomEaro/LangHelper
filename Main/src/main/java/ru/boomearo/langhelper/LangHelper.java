package ru.boomearo.langhelper;

import java.io.File;
import java.io.InputStream;
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
import ru.boomearo.langhelper.versions.Translate1_14_R1;
import ru.boomearo.langhelper.versions.Translate1_15_R1;
import ru.boomearo.langhelper.versions.Translate1_16_R3;
import ru.boomearo.langhelper.versions.exceptions.LangException;

public class LangHelper extends JavaPlugin {

    private AbstractTranslateManager version = null;

    private static LangHelper instance = null;

    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

    private final List<Class<? extends AbstractTranslateManager>> versions = Arrays.asList(
            Translate1_12_R1.class,
            Translate1_13_R2.class,
            Translate1_14_R1.class,
            Translate1_15_R1.class,
            Translate1_16_R3.class
            );

    
    @Override
    public void onEnable() {
        instance = this;

        try {
            //Вычисляем версию сервера и создаем соответсвующий экземпляр
            this.version = matchVersion();

            //Проверяем, существует ли дефолтная папка (первый раз включается плагин?)
            checkDefaultTranslate(this.serverVersion);

            //Подгружаем языки с диска
            this.version.loadLanguages(getLanguageFolder());
            
            if (this.version != null) {
                for (Translate tra : this.version.getAllTranslate()) {
                    this.getLogger().info("Язык '" + tra.getLangType().name() + "' успешно загружен. Количество строк: " + tra.getAllTranslate().size());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getCommand("langhelper").setExecutor(new CmdExecutorLangHelper());

        this.getLogger().info("Плагин успешно запущен!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Плагин успешно выключен!");
    }
    
    private void checkDefaultTranslate(String version) {
        //Убеждаемся что папки этой версии нет
        File currentTranFolder = new File(this.getDataFolder(), "languages" + File.separator + version + File.separator);
        if (currentTranFolder.exists()) {
            return;
        }

        //Выгружаем все языки по умолчанию которые есть в плагине
        for (LangType type : LangType.values()) {
            String res = ("languages" + File.separator + version + File.separator + type.name()).replace('\\', '/'); // Кто знал что надо заменять символ пути..
            InputStream is = this.getResource(res);
            //Сохраняем только те что есть
            if (is != null) {
                saveResource(res, false);
            }
        }
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

    private AbstractTranslateManager matchVersion() throws LangException {
        try {
            return this.versions.stream()
                    .filter(version -> version.getSimpleName().substring(9).equals(this.serverVersion))
                    .findFirst().orElseThrow(() -> new LangException("Плагин не поддерживает данную версию сервера!")).
                    getConstructor().
                    newInstance();
        } 
        catch (Exception e) {
            //Вызываем новое но свое
            throw new LangException(e.getMessage());
        } 
    }
    
    public static LangHelper getInstance() { 
        return instance;
    }

    //TEST
    //TEST
    //TODO
    
    public static File getLanguageFolder() {
        return new File(LangHelper.getInstance().getDataFolder(), "languages" + File.separator);
    }

}
