package de.androgyn.gui.tabgui.mods.movement;

import org.lwjgl.input.Keyboard;

import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.util.BlockPointer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;

public class AutoSwim extends Module {

	public AutoSwim(String name, Category category) {
		super(name, category);
		
	}
	
	private boolean isSwimming;
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) && isSwimming) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
			isSwimming = false;
		}
	}
	
	long tick = 0;
	
	@Override
	public void run() {
		if (isEnabled()) {
			EntityPlayerSP player = mc.player;
			
			if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(player.posX,player.posY,player.posZ)).getMaterial().equals(Material.WATER)) {
				if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
					isSwimming = true;
				}
			} else {
				if (isSwimming) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
					isSwimming = false;
				}
			}
		}
	}
}
