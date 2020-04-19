package de.androgyn.gui.tabgui.mods.player;

import java.util.List;

import de.androgyn.Androgyn;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.gui.tabgui.mods.combat.KillAuraMod;
import de.androgyn.managers.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoEatMod extends Module {

	public AutoEatMod(String name, Category category) {
		super(name, category);
		mc = Androgyn.getMinecraft();
		
	}
	
	private boolean blockEatInFight;
	private int minFoodLevel, saveDistance;
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		blockEatInFight = getConfiguration().getBoolean("blockEatInFight", false);
		minFoodLevel = getConfiguration().getInt("minFoodLevel", 10);
		saveDistance = getConfiguration().getInt("saveDistance", 3);
	}
	
	private int oldSlot = -1;
	private Minecraft mc;
	
	long i = 0;
	@Override
	public void run() {
		super.run();
		i = System.currentTimeMillis();
		
		if (isEnabled()) {

			if (!shouldEat()) {
				stopIfEating();
				return;
			}
			
			int bestSlot = -1;
			float bestSaturation = -1;
			for (int i = 0; i < 9; i ++) {
				ItemStack stack = mc.player.inventory.getStackInSlot(i);
				if (stack == null || !(stack.getItem() instanceof ItemFood)) continue;
				float saturation = ((ItemFood)stack.getItem()).getSaturationModifier(stack);
				if (saturation > bestSaturation) {
					bestSaturation = saturation;
					bestSlot = i;
				}
			}
			
			if (bestSlot == -1) {
				stopIfEating();
				return;
			}
			
			if (!isEating()) oldSlot = mc.player.inventory.currentItem;
			mc.player.inventory.currentItem = bestSlot;
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
			mc.playerController.processRightClick(mc.player, mc.player.world, EnumHand.MAIN_HAND);	
		}
	}
	
	private boolean shouldEat() {
		EntityPlayer player = mc.player;
		
		if (!player.canEat(false)) return false;
		if (blockEatInFight && (areEntitesNearby() && (player.getFoodStats().getFoodLevel()>minFoodLevel))) return false;
		
		
		
		if (mc.currentScreen != null) return false;
		if (mc.objectMouseOver != null) {
			Entity entity = mc.objectMouseOver.entityHit;
			if (entity instanceof EntityVillager || entity instanceof EntityTameable) return false;
			
			BlockPos pos = mc.objectMouseOver.getBlockPos();
			if (pos!=null) {
				Block block = mc.world.getBlockState(pos).getBlock();
				if (block instanceof BlockContainer || block instanceof BlockWorkbench) return false;
			}
		}
			
		return true;
	}
	
	public boolean isEating() {
		return oldSlot != -1;
	}
	
	public void stopIfEating() {
		if (!isEating()) return;
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		mc.player.inventory.currentItem = oldSlot;
		oldSlot = -1;		
	}
	
	public boolean areEntitesNearby() {
		List<Entity> entities = Androgyn.getMinecraft().world.loadedEntityList;
		for (Entity en : entities) {
			if (en instanceof EntityLivingBase && !(en instanceof EntityPlayer)) {
				if (Androgyn.getMinecraft().player.getDistance(en) < ((KillAuraMod)ModuleManager.getModule("killaura")).getRange()+saveDistance) return true;
			}
		}
		return false;
	}

}
