package ru.boomearo.langhelper.versions;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

/**
 * Представляет целый перевод строк.
 */
public class Translate {

    private final LangType lang;
    private final ConcurrentMap<String, String> tr;

    public Translate(LangType lang, ConcurrentMap<String, String> tr) {
        this.lang = lang;
        this.tr = tr;
    }

    public LangType getLangType() {
        return this.lang;
    }

    public String getTranslate(String name) {
        return this.tr.get(name);
    }

    public Collection<String> getAllTranslate() {
        return this.tr.values();
    }

}
