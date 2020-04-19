package de.androgyn.gui.tabgui;

import java.awt.Color;

import de.androgyn.Androgyn;
import de.androgyn.managers.ModuleManager;
import de.androgyn.util.JSONReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TabGUI extends Module {

	private int posX,posY;
	
	public TabGUI(String name, Category category) {
		super(name, category);
		JSONReader configuration = getConfiguration();
		posX = configuration.getInt("posX", 200);
		posY = configuration.getInt("posY", 100);
	}

	public int selected = 0;
	public int selectedModule = 0;
	public boolean categorySelected = false;
	
}
