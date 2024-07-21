package ru.boomearo.langhelper.versions;

public record TranslationVersionWrapper(String version, Class<? extends DefaultTranslateManager> clazz) {

}
