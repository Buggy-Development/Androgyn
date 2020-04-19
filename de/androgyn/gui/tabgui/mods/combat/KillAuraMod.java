package de.androgyn.gui.tabgui.mods.combat;

import java.io.File;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.androgyn.Androgyn;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.util.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class KillAuraMod extends Module {

	private int range, delay, weaponSlot;
	
	public KillAuraMod(String name, Category category) {
		super(name, category);
	}
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		delay = getConfiguration().getInt("delay", 1000);
		range = getConfiguration().getInt("range", 5);
		weaponSlot = getConfiguration().getInt("weaponSlot", 0);
	}
	
	private long lastattack = 0;
	@Override
	public void run() {
		try {
		if (isEnabled()) {
			List<Entity> entities = Androgyn.getMinecraft().world.loadedEntityList;
			EntityPlayer player = Androgyn.getMinecraft().player;

			if (lastattack+delay > System.currentTimeMillis()) return;
			if (player == null) return;
			float yaw = player.rotationYaw;
			float pitch = player.rotationPitch;
			lastattack = System.currentTimeMillis();
				for (Entity en : entities) {
					if (en == null || en instanceof EntityPlayer) continue;
					if (en instanceof EntityLivingBase) {
						EntityLivingBase entity = (EntityLivingBase) en;
						if (player.getDistance(en) < range) {
							if (!entity.isDead) {
								if (player.canEntityBeSeen(entity)) {
									int lastSlot = player.inventory.currentItem;
									player.inventory.currentItem = weaponSlot;
//									render(entity);
//									faceEntity(entity);
									Androgyn.getMinecraft().playerController.attackEntity(player, entity);

									player.swingArm(EnumHand.MAIN_HAND);
								}
							}
						}
					}
				}
				player.rotationYaw = yaw;
			player.prevRotationPitch = pitch;
		}
		}catch(Exception e) {}
	}
	
	public static synchronized void faceEntity(EntityLivingBase entity) {
		final float[] rotations = getRoationsNeeded(entity);
		if (rotations != null) {
			Androgyn.getMinecraft().player.rotationYaw = rotations[0];
			Androgyn.getMinecraft().player.rotationPitch = rotations[1];
		}
	}
	
	public static float[] getRoationsNeeded(Entity entity) {
		final EntityPlayerSP player = Androgyn.getMinecraft().player;
		final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
		
		final double diffX = entity.posX - player.posX;
		final double diffZ = entity.posZ - player.posZ;
		final double diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (player.posY + player.getEyeHeight());
		
		final double dist = MathHelper.sqrt(diffX*diffX + diffZ*diffZ);
		final float yaw = (float) (Math.atan2(diffZ, diffX)*180.0D/Math.PI)-90.0F;
		final float pitch = (float) -(Math.atan2(diffY, dist)*180.0D/Math.PI);
		return new float[] {player.rotationYaw+wrapAngleTo180_float(yaw-player.rotationYaw),player.prevRotationPitch+wrapAngleTo180_float(pitch-player.rotationPitch)};
	}
	
	public static float wrapAngleTo180_float(float f) {
        f %= 360.0F;
        if (f >= 180.0F) f -= 360.0F;
        if (f < -180.0F) f += 360.0F;
        return f;
    }
	
	public void render(EntityLivingBase target) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL11.glPushMatrix();
		GL11.glTranslated(
			-Androgyn.getMinecraft().getRenderManager().viewerPosX,
			-Androgyn.getMinecraft().getRenderManager().viewerPosY,
			-Androgyn.getMinecraft().getRenderManager().viewerPosZ
		);
		
		AxisAlignedBB box = new AxisAlignedBB(BlockPos.ORIGIN);
		float p = (target.getMaxHealth() - target.getHealth())
			/ target.getMaxHealth();
		float red = p * 2F;
		float green = 2 - red;
		
		GL11.glTranslated(target.posX, target.posY, target.posZ);
		GL11.glTranslated(0, 0.05, 0);
		GL11.glScaled(target.width, target.height, target.width);
		GL11.glTranslated(-0.5, 0, -0.5);
		
		if(p < 1)
		{
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glScaled(p, p, p);
			GL11.glTranslated(-0.5, -0.5, -0.5);
		}
		
		GL11.glColor4f(red, green, 0, 0.25F);
		RenderUtils.drawSolidBox(box);
		
		GL11.glColor4f(red, green, 0, 0.5F);
		RenderUtils.drawOutlinedBox(box);
		
		GL11.glPopMatrix();
		
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	
	public int getRange() {
		return range;
	}

}
