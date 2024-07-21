package ru.boomearo.langhelper.versions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * Представляет языковые строки
 */
@RequiredArgsConstructor
public class TranslatedMessages {

    @Getter
    private final LangType langType;
    private final Map<String, String> translate;

    public String getTranslate(String name) {
        return this.translate.get(name);
    }

    public Collection<String> getAllTranslate() {
        return this.translate.values();
    }

}
