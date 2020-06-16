package ru.boomearo.langhelper.objects;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Translate {

	private ConcurrentMap<String, String> tr = new ConcurrentHashMap<String, String>();
	
	public Translate(ConcurrentMap<String, String> tr) {
		this.tr = tr;
	}
	
	public String getTranstale(String name) {
		return this.tr.get(name);
	}
	
	
}
