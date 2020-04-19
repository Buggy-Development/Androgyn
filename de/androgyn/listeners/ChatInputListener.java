package de.androgyn.listeners;

import de.androgyn.Androgyn;
import de.androgyn.gui.tabgui.mods.movement.WalkToLocationMod;
import de.androgyn.managers.ModuleManager;
import de.androgyn.util.BlockPointer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatInputListener {
	
	private static final String prefix = "#";
	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		String msg = event.getMessage();
		if (msg.startsWith(prefix)) {
			event.setCanceled(true);
			msg = msg.replace(prefix, "");
			String cmd = msg.split(" ")[0];
			String[] args = msg.replace(cmd+" ","").split(" ");
			
			if (cmd.equalsIgnoreCase("reload")) {
				ModuleManager.modules.forEach(module -> {
					module.reloadConfiguration();
				});
				Androgyn.getMinecraft().player.sendMessage(new TextComponentString("§aReloaded Configurations!"));
				
			}
			/*
			if (cmd.equalsIgnoreCase("killaura")) {
				ModuleManager.killaura.toggle();
			}
			
			if (cmd.equalsIgnoreCase("autosprint")) {
				ModuleManager.autosprint.toggle();
			}
			
			if (cmd.equalsIgnoreCase("autoeat")) {
				ModuleManager.autoeat.toggle();
			}
			
			if (cmd.equalsIgnoreCase("autorespawn")) {
				ModuleManager.autorespawn.toggle();
			}
			
			if (cmd.equalsIgnoreCase("walktolocation")) {
				ModuleManager.walktolocation.toggle();
			}
			*/
		}
	}

}
