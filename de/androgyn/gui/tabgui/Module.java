package de.androgyn.gui.tabgui;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import de.androgyn.Androgyn;
import de.androgyn.managers.ModuleManager;
import de.androgyn.util.JSONReader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Module {
	
	private String name;
	private Category category;
	private boolean enabled;
	private JSONReader configuration;
	
	public Module(String name, Category category) {
		this.name = name;
		this.category = category;
		enabled = false;
		
		MinecraftForge.EVENT_BUS.register(this);
		ModuleManager.modules.add(this);
		category.addMod(this);
		reloadConfiguration();
		enabled = configuration.getBoolean("enabled", false);
	}
	
	public JSONReader getConfiguration() {
		if (configuration == null) reloadConfiguration();
		return configuration;
	}
	
	public void reloadConfiguration() {
		configuration = JSONReader.getJSONReader(Androgyn.moduleConfigLocation+"/"+getName()+".json");
	}
	
	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().player != null) run();
	}
	
	public void run() {}
	
	public String getName() {
		return name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		getConfiguration().set("enabled", enabled);
		this.enabled = enabled;
		if (!enabled) onDisable();
	}
	
	public void onEnable() {}
	public void onDisable() {}
	
	public void toggle() {
		System.out.println(enabled);
		setEnabled(!enabled);
		if (enabled) onEnable();
		else onDisable();
	}
	
}
