package ru.boomearo.langhelper.api;

import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Представляет языковые строки
 */
public interface TranslatedMessages {

    @NonNull
    LangType getLangType();

    @Nullable
    String getTranslate(String name);

    @NonNull
    Collection<String> getAllTranslate();
}
