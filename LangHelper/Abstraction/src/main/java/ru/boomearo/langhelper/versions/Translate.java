package ru.boomearo.langhelper.versions;

import java.util.concurrent.ConcurrentMap;

public class Translate {

	private final ConcurrentMap<String, String> tr;
	
	public Translate(ConcurrentMap<String, String> tr) {
		this.tr = tr;
	}
	
	public String getTranstale(String name) {
		return this.tr.get(name);
	}
	
	
}
