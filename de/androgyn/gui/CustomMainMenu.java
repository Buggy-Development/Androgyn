package de.androgyn.gui;

import net.minecraft.client.gui.GuiScreen;

public class CustomMainMenu extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "test", 20, 20, 0xc80000);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	

}
