package de.androgyn.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.gui.tabgui.TabGUI;
import de.androgyn.gui.tabgui.categorys.CombatCategory;
import de.androgyn.gui.tabgui.categorys.RenderCategory;
import de.androgyn.gui.tabgui.mods.combat.AutoRespawnMod;
import de.androgyn.gui.tabgui.mods.combat.KillAuraMod;
import de.androgyn.gui.tabgui.mods.movement.AutoJump;
import de.androgyn.gui.tabgui.mods.movement.AutoSprintMod;
import de.androgyn.gui.tabgui.mods.movement.AutoSwim;
import de.androgyn.gui.tabgui.mods.movement.WalkToLocationMod;
import de.androgyn.gui.tabgui.mods.player.AutoEatMod;
import net.minecraftforge.common.MinecraftForge;

public class ModuleManager {
	
	private Category combat,render,player,movement;
	public static Set<Module> modules = ConcurrentHashMap.newKeySet();
	public static Set<Module> activeMods = ConcurrentHashMap.newKeySet();
	public static Set<Category> categories = ConcurrentHashMap.newKeySet();
	
	
	public static Module getModule(String name) {
		for (Module module : modules) {
			if (module.getName().equalsIgnoreCase(name)) return module;
		}
		return null;
	}
 	
	
	public ModuleManager() {
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				Set<Module> n_activeMods = ConcurrentHashMap.newKeySet();
				for (Category cat : categories) {
					for (Module mod : cat.getMods()) {
						if (mod.isEnabled()) n_activeMods.add(mod);
					}
				}
				activeMods = n_activeMods;
			}
		}, 100, 100);
		
		//Register Categories
        combat = new CombatCategory("Combat");
        render = new RenderCategory("Render");
        player = new RenderCategory("Player");
        movement = new RenderCategory("Movement");
        
        
        new KillAuraMod("KillAura", combat);
        new AutoRespawnMod("AutoRespawn", combat);
        
		new AutoEatMod("AutoEat", player);
		
		new AutoSprintMod("AutoSprint", movement);
		new WalkToLocationMod("WalkToLocation", movement);
		new AutoJump("AutoJump", movement);
		new AutoSwim("AutoSwim", movement);
		
		new TabGUI("TabGUI", render);
		
	}
	
	public static Set<Module> getActiveMods() {
		return activeMods;
	}

}
