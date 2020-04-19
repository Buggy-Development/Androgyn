package de.androgyn.gui.tabgui;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.androgyn.managers.ModuleManager;

public abstract class Category {
	
	
	private final String name;
	
	private Set<Module> mods = ConcurrentHashMap.newKeySet();
	
	public Category(String name) {
		this.name = name;
		
		
		
		
		for (Category category : ModuleManager.categories) {
			if (category.name.equalsIgnoreCase(name)) ModuleManager.categories.remove(category);
		}
		ModuleManager.categories.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public void addMod(Module mod) {
		mods.add(mod);
	}
	
	public Set<Module> getMods() {
		return mods;
	}

}
