package ru.boomearo.langhelper.versions;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class LangData {

	private final ConcurrentMap<LangType, Translate> types;
	
	public LangData(ConcurrentMap<LangType, Translate> types) {
		this.types = types;
	}
	
	public Translate getTranslate(LangType type) {
		return this.types.get(type);
	}
	
	public Collection<Translate> getAllTranslate() {
		return this.types.values();
	}
	
}
