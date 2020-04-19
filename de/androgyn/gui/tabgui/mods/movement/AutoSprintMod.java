package de.androgyn.gui.tabgui.mods.movement;

import de.androgyn.Androgyn;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import net.minecraft.entity.player.EntityPlayer;

public class AutoSprintMod extends Module{

	public AutoSprintMod(String name, Category category) {
		super(name, category);
	}
	
	@Override
	public void run() {
		super.run();
		if (isEnabled()) {
			EntityPlayer player = Androgyn.getMinecraft().player;
			if (player.collidedHorizontally || player.isSneaking()) return;
			if (player.moveForward > 0 && !player.isSprinting()) player.setSprinting(true);
		}
	}
	
}
