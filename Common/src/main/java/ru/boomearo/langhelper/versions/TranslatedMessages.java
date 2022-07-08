package ru.boomearo.langhelper.versions;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

/**
 * Представляет языковые строки
 */
public class TranslatedMessages {

    private final LangType langType;
    private final ConcurrentMap<String, String> translate;

    public TranslatedMessages(LangType langType, ConcurrentMap<String, String> translate) {
        this.langType = langType;
        this.translate = translate;
    }

    public LangType getLangType() {
        return this.langType;
    }

    public String getTranslate(String name) {
        return this.translate.get(name);
    }

    public Collection<String> getAllTranslate() {
        return this.translate.values();
    }

}
