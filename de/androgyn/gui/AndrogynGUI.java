package de.androgyn.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class AndrogynGUI extends GuiScreen {
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		this.drawRect(75, 20, 5, 40, Integer.valueOf("606060",16));
		super.drawScreen(mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
