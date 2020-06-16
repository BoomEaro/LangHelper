package ru.boomearo.langhelper.objects;

public enum LangVersion {

	V1_12_2("1.12.2", 340),
	V1_15_2("1.15.2", 578);
	
	private String name;
	private int protocol;
	
	LangVersion(String name, int protocol) {
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getProtocol() {
		return this.protocol;
	}
}
