package ru.boomearo.langhelper.api;

import lombok.NonNull;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public interface TranslateManager {

    /**
     * Получить все доступные языки с переведенными строками
     *
     * @return Язык с переведенными строками
     */
    @NonNull
    Collection<TranslatedMessages> getAllTranslate();

    /**
     * Получить все переведенные строки языка указанного типа
     *
     * @param type Тип языка
     * @return Все переведенные строки этого языка
     */
    @Nullable
    TranslatedMessages getTranslate(@NonNull LangType type);

    /**
     * Получить переведенную строку на основе ключа строки перевода и типа языка
     *
     * @param key      Ключ строки перевода
     * @param langType Тип языка
     * @return Переведенная строка. Может вернуть null если перевода нет.
     */
    @Nullable
    String getTranslate(@NonNull String key, @NonNull LangType langType);

    /**
     * Получить перевод предмета.
     *
     * @param itemStack Предмет для перевода
     * @param langType  Тип языка
     * @return Строка с переводом этого предмета. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getItemNameSafe(@NonNull ItemStack itemStack, @NonNull LangType langType);

    /**
     * Получить перевод сущности.
     *
     * @param entityType Сущность для перевода
     * @param langType   Тип языка
     * @return Строка с переводом этой сущности. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getEntityNameSafe(@NonNull EntityType entityType, @NonNull LangType langType);

    /**
     * Получить перевод зачарования.
     *
     * @param enchant  Зачарования для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого зачарования. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getEnchantmentNameSafe(@NonNull Enchantment enchant, @NonNull LangType langType);

    /**
     * Получить перевод уровня зачарования.
     *
     * @param level    Уровень зачарования
     * @param langType Тип языка
     * @return Строка с переводом уровня зачарования. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getEnchantmentLevelNameSafe(int level, @NonNull LangType langType);

    /**
     * Получить перевод типа зелий
     *
     * @param potionEffectType Тип зелья
     * @param langType         Тип языка
     * @return Строка с переводом этого типа зелья. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getPotionEffectNameSafe(@NonNull PotionEffectType potionEffectType, @NonNull LangType langType);

    /**
     * Получить перевод биома
     *
     * @param biome    Тип биома
     * @param langType Тип языка
     * @return Строка с переводом этого биома. Если перевода нет, вернет английский перевод по умолчанию.
     */
    @NonNull
    String getBiomeNameSafe(@NonNull Biome biome, @NonNull LangType langType);


    /**
     * Получить перевод предмета.
     *
     * @param itemStack Предмет для перевода
     * @param langType  Тип языка
     * @return Строка с переводом этого предмета. Может вернуть null если перевода нет.
     */
    @Nullable
    String getItemName(@NonNull ItemStack itemStack, @NonNull LangType langType);

    /**
     * Получить перевод сущности.
     *
     * @param entityType Сущность для перевода
     * @param langType   Тип языка
     * @return Строка с переводом этой сущности. Может вернуть null если перевода нет.
     */
    @Nullable
    String getEntityName(@NonNull EntityType entityType, @NonNull LangType langType);

    /**
     * Получить перевод зачарования.
     *
     * @param enchant  Зачарования для перевода
     * @param langType Тип языка
     * @return Строка с переводом этого зачарования. Может вернуть null если перевода нет.
     */
    @Nullable
    String getEnchantmentName(@NonNull Enchantment enchant, @NonNull LangType langType);

    /**
     * Получить перевод уровня зачарования.
     *
     * @param level    Уровень зачарования
     * @param langType Тип языка
     * @return Строка с переводом уровня зачарования. Может вернуть null если перевода нет.
     */
    @Nullable
    String getEnchantmentLevelName(int level, @NonNull LangType langType);

    /**
     * Получить перевод типа зелий
     *
     * @param potionEffectType Тип зелья
     * @param langType         Тип языка
     * @return Строка с переводом этого типа зелья. Может вернуть null если перевода нет.
     */
    @Nullable
    String getPotionEffectName(@NonNull PotionEffectType potionEffectType, @NonNull LangType langType);

    /**
     * Получить перевод биома
     *
     * @param biome    Тип биома
     * @param langType Тип языка
     * @return Строка с переводом этого биома. Может вернуть null если перевода нет.
     */
    @Nullable
    String getBiomeName(@NonNull Biome biome, @NonNull LangType langType);


    /**
     * Скачивает и загружает недостающие языки.
     * Полезно если вы зарегистрировали языки, которые не были указаны в конфиге.
     */
    void reloadLanguages();

    /**
     * Зарегистрировать язык
     *
     * @param langType тип языка
     */
    void registerLanguageType(@NonNull LangType langType);

    /**
     * Удалить регистрацию языка
     *
     * @param langType тип языка
     */
    void unregisterLanguageType(@NonNull LangType langType);

    /**
     * Получить все зарегистрированные языки
     *
     * @return зарегистрированные языки
     */
    @NonNull
    Set<LangType> getRegisteredLanguages();

}
