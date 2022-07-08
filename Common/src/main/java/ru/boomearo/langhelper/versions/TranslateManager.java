package ru.boomearo.langhelper.versions;

import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public interface TranslateManager {

    /**
     * Получить все доступные языки с переведенными строками
     * @return Язык с переведенными строками
     */
    Collection<TranslatedMessages> getAllTranslate();

    /**
     * Получить все переведенные строки языка указанного типа
     * @param type Тип языка
     * @return Все переведенные строки этого языка
     */
    TranslatedMessages getTranslate(LangType type);

    /**
     * Получить переведенную строку на основе ключа строки перевода и типа языка
     * @param key Ключ строки перевода
     * @param langType Тип языка
     * @return Переведенная строка. Может вернуть null если перевода нет.
     */
    String getTranslate(String key, LangType langType);

    /**
     * Получить перевод предмета.
     * @param itemStack Предмет для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого предмета. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getItemNameSafe(ItemStack itemStack, LangType langType);

    /**
     * Получить перевод сущности.
     * @param entityType Сущность для перевода
     * @param langType Тип языка
     * @return Строка с переводом этой сущности. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getEntityNameSafe(EntityType entityType, LangType langType);

    /**
     * Получить перевод зачарования.
     * @param enchant Зачарования для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого зачарования. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getEnchantNameSafe(Enchantment enchant, LangType langType);

    /**
     * Получить перевод уровня зачарования.
     * @param level Уровень зачарования
     * @param langType Тип языка
     * @return Строка с переводом уровня зачарования. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getEnchantLevelNameSafe(int level, LangType langType);

    /**
     * Получить перевод типа зельий
     * @param potionEffectType Тип зелья
     * @param langType Тип языка
     * @return Строка с переводом этого типа зелья. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getPotionEffectNameSafe(PotionEffectType potionEffectType, LangType langType);

    /**
     * Получить перевод биома
     * @param biome Тип биома
     * @param langType Тип языка
     * @return Строка с переводом этого биома. Если перевода нет, вернет английский перевод по умолчанию.
     */
    String getBiomeNameSafe(Biome biome, LangType langType);


    /**
     * Получить перевод предмета.
     * @param itemStack Предмет для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого предмета. Может вернуть null если перевода нет.
     */
    String getItemName(ItemStack itemStack, LangType langType);

    /**
     * Получить перевод сущности.
     * @param entityType Сущность для перевода
     * @param langType Тип языка
     * @return Строка с переводом этой сущности. Может вернуть null если перевода нет.
     */
    String getEntityName(EntityType entityType, LangType langType);

    /**
     * Получить перевод зачарования.
     * @param enchant Зачарования для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого зачарования. Может вернуть null если перевода нет.
     */
    String getEnchantName(Enchantment enchant, LangType langType);

    /**
     * Получить перевод уровня зачарования.
     * @param level Уровень зачарования
     * @param langType Тип языка
     * @return Строка с переводом уровня зачарования. Может вернуть null если перевода нет.
     */
    String getEnchantLevelName(int level, LangType langType);

    /**
     * Получить перевод типа зельий
     * @param potionEffectType Тип зелья
     * @param langType Тип языка
     * @return Строка с переводом этого типа зелья. Может вернуть null если перевода нет.
     */
    String getPotionEffectName(PotionEffectType potionEffectType, LangType langType);

    /**
     * Получить перевод биома
     * @param biome Тип биома
     * @param langType Тип языка
     * @return Строка с переводом этого биома. Может вернуть null если перевода нет.
     */
    String getBiomeName(Biome biome, LangType langType);

}
