package ru.boomearo.langhelper.objects;

public enum LangType {

	EN("en", "English"),
	RU("ru", "Русский");
	
	private String name;
	private String displayName;
	
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
