package ru.boomearo.langhelper.versions;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

/**
 * Представляет целый перевод строк.
 */
public class Translate {

    private final LangType langType;
    private final ConcurrentMap<String, String> translate;

    public Translate(LangType lang, ConcurrentMap<String, String> translate) {
        this.langType = lang;
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
