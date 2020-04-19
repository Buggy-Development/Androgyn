package de.androgyn.gui.tabgui.mods.combat;

import de.androgyn.Androgyn;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawnMod extends Module {

	public AutoRespawnMod(String name, Category category) {
		super(name, category);
	}

	@SubscribeEvent
	public void playerDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			((EntityPlayer)event.getEntity()).respawnPlayer();
			Androgyn.getMinecraft().displayGuiScreen(null);
		}
	}
	
}
