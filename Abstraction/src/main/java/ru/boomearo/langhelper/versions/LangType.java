package ru.boomearo.langhelper.versions;

public enum LangType {

    //Для поддержки остальных языков стоит просто добавлять еще енумов
    RU("ru_ru", "Русский");

    private final String name;
    private final String displayName;

    LangType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
