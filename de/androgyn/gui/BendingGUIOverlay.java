package de.androgyn.gui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.androgyn.Androgyn;
import de.androgyn.PathFinder.PathFinder;
import de.androgyn.PathFinder.PathFinderV2;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.gui.tabgui.TabGUI;
import de.androgyn.gui.tabgui.mods.movement.WalkToLocationMod;
import de.androgyn.managers.ModuleManager;
import de.androgyn.util.BlockPointer;
import de.androgyn.util.JSONReader;
import de.androgyn.util._3d.Face3D;
import de.androgyn.util._3d.Object3D;
import de.androgyn.util._3d.Vector3D;
import de.androgyn.util.glBuilder.GLFactory3D;
import de.androgyn.util.glBuilder.GLFace3D;
import de.androgyn.util.glBuilder.GLVector3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BendingGUIOverlay extends Gui {
	
	private boolean renderAndrogynGUI = false;
	private Minecraft mc;
	private int rainbowAnimationTick, zerotick;
	private ScaledResolution sr;
	
	
	public BendingGUIOverlay() {
		mc = Minecraft.getMinecraft();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				rainbowAnimationTick+=1;
				if (rainbowAnimationTick>=360) rainbowAnimationTick=0;
				zerotick+=1;
				if (zerotick>20) zerotick=0;
			}
		}, 100, 50);
	}
	

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent event) {

		//ArrayList<BlockPointer> remoteList = (ArrayList<BlockPointer>) de.androgyn.gui.tabgui.mods.movement.WalkToLocationMod.currentWalkList;
		int color = Color.HSBtoRGB((float)rainbowAnimationTick/360f, 1.0f, 1.0f);

//		DrawEngine.drawRect3D	(20, 230, 255, 0, new Vec3d(-100,3,0));
//		DrawEngine.drawRect3D(20*((double)rainbowAnimationTick/10d), 230, 255, 0, new Vec3d(0,10,0));
//		DrawEngine.drawRect3D(255, 0, 30, 0.8, 8, 4, 2, new Vec3d(5,5,0), false);
//		DrawEngine.drawRect3D(1, ((color>>16)&255), ((color>>8)&255), ((color)&255), new Vec3d(20,10,20), 0.2f, 1, 20);
//		DrawEngine.drawArrow3D(1, 255, 0, 0, new Vec3d(0,20,0), 1, 1, 1, 0);
		
		DrawEngine.drawTest(false, 200, 0, 100, new Vec3d(0,4,0));
		DrawEngine.drawArea(true, 30, 0, 255, 0.6, new Vec3d(0,4,0));
//		DrawEngine.drawBlockOutline(0, 255, 125, 0.5, new Vec3d(1,3,0), true);
		
		
		
		if (PathFinderV2.currentProgessMirror != null) {
			List<BlockPointer> objLink = PathFinderV2.currentProgessMirror;
			try {
			Vec3d[] arr = new Vec3d[objLink.size()];
			int i=0;
			for(BlockPointer b : objLink) {
				arr[i++] = b.toVector();
			}
			DrawEngine.drawLine(true, 0, 200, 255, arr);
			if(arr.length > 0) {
				Vec3d a = arr[arr.length-1];
				DrawEngine.drawBlockOutline(0, 200, 255, 1, a);
			}
			}catch(Exception e) {}
		}
		
		
		if (PathFinderV2.currentProgessAllEntrysMirror != null) {
			for (BlockPointer block : PathFinderV2.currentProgessAllEntrysMirror) {
				//if (block.pos_y == 3) continue;
				if(block.recoursive_way_cost == 0) continue;
				float cost = (float)(block.recoursive_way_cost / 5000d);
				if(cost > 1) {
					cost = 1;
				}
				int b = (int)WalkToLocationMod.lerp(0, 255, 1f-cost);
				int r = 255 - b;	
//				DrawEngine.drawBlockOutline(r, 0, b, 0.8d, block.toVector(),false);
				
				if (block.last != null) {
//					DrawEngine.drawLine(false, 0, 255, 0, block.toVector(), block.last.toVector());
				}
				DrawEngine.drawArea(false, r, 0, b, 0.7, block.toVector().addVector(0, 0.01, 0));
			}
			
			
		}		
		
//		DrawEngine.drawBlockOutline(255, 0, 0, new Vec3d(0, 5, 18));
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		
		if (WalkToLocationMod.currentWalkList != null) {
			try {
			Vec3d[] arr = new Vec3d[WalkToLocationMod.currentWalkList.size()];
			int i=0;
			for(BlockPointer b : WalkToLocationMod.currentWalkList) {
				arr[i++] = b.toVector();
			}
			DrawEngine.drawLine(true, 0, 255, 0, arr);
			if(arr.length > 0) {
				Vec3d a = arr[arr.length-1];
				//Vec3d b
//				DrawEngine.drawRect3D(20, 255, 0, 0, a);
				DrawEngine.drawBlockOutline(0, 255, 0, 1, a);
			}
			}catch(Exception e) {}
		//} else {
//			System.out.println("remotelist is null");
		}
		
//		DrawEngine.drawLine(false, ((color>>16)&255), ((color>>8)&255), ((color)&255), new Vec3d(3, 0, 20), new Vec3d(4, 0, 20), new Vec3d(7, 4.6, 22), new Vec3d(7, 4.6, 24), new Vec3d(8, 4.6, 27), new Vec3d(7, 4.6, 28), new Vec3d(7, 4.6, 32));
		
	}
	
	private long lasttick = 0;
	private double mspt,tps = 0;
	private int tickCounter, lastValue;

	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().player != null) {
			if(tickCounter++ % 20 == 0) {
				mspt = (double) (System.currentTimeMillis()-lasttick) / 20;
				lasttick = System.currentTimeMillis();
			}
		}
	}
	
	DecimalFormat format = new DecimalFormat("#0.000");
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGameOverlayEvent event) {
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE) return; 
		EntityPlayer player = mc.player;
		if (mc.player == null) return;
		int color = Color.HSBtoRGB((float)rainbowAnimationTick/360f, 1.0f, 1.0f);
		Vec3d pos = mc.player.getPositionVector();

		sr = new ScaledResolution(mc);
		FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
		fontRenderer.drawStringWithShadow("Androgyn v"+Androgyn.VERSION+" by TheHolyException & KaiGermany - [" + this.mc.displayWidth+"/"+this.mc.displayHeight+"]", 2, 2, color);
		fontRenderer.drawStringWithShadow("§bFPS: §f"+mc.getDebugFPS(), 5, 20, 1);
		fontRenderer.drawStringWithShadow("§bMSPT: §f"+mspt, 5, 30, 1);
		try {fontRenderer.drawStringWithShadow("§bPing: §f"+mc.player.connection.getPlayerInfo(player.getGameProfile().getId()).getResponseTime(), 5, 40, 1);}catch (Exception ex) {}
		fontRenderer.drawStringWithShadow("§bHealth: §f"+player.getHealth(), 5, 50, 1);
		fontRenderer.drawStringWithShadow("§bHunger: §f"+player.getFoodStats().getFoodLevel(), 5, 60, 1);
		fontRenderer.drawStringWithShadow("§bSaturation: §f"+player.getFoodStats().getSaturationLevel(), 5, 70, 1);
		fontRenderer.drawStringWithShadow("§bCurrent State: §f"+((WalkToLocationMod)ModuleManager.getModule("walktolocation")).getState(), 5, 80, 1);
		
		
		String texta = "§bXYZ: §f"+format.format(pos.x)+", "+format.format(pos.y)+", "+format.format(pos.z);
		fontRenderer.drawStringWithShadow(texta, sr.getScaledWidth()-fontRenderer.getStringWidth(texta)-2, sr.getScaledHeight()-30, 1);
//		drawRect(75, 20, 5, 40, 0xff0d0d0d);
//		DrawEngine.drawRect(20, 20, 100, 100, ((color>>16)&255), ((color>>8)&255), ((color)&255));
//		mc.draw(75, 20, 11*16, 13*16, 16, 16, 75, 75, 75, 255);
//		drawVerticalLine(200, 20, 70, color)
//		DrawEngine.rect(50, 20, 70, 20, ((color>>16)&255), ((color>>8)&255), ((color)&255), 255);
//		RenderUtils.drawOutlinedBox(new AxisAlignedBB(new BlockPos(248, 4, 757)));
		Set<Module> activeMode = ModuleManager.getActiveMods();
		int index = 0;
		
		for (Module mod : activeMode) {
			int color2 = Color.HSBtoRGB((float)(rainbowAnimationTick+((index)*20)+10)/360f, 1.0f, 1.0f);
			fontRenderer.drawString(mod.getName(), sr.getScaledWidth()-fontRenderer.getStringWidth(mod.getName())-2, 5+(index*10), color2, true);
			index ++;
		}
		
		if (ModuleManager.getModule("tabgui").isEnabled()) {
			TabGUI tabGui = (TabGUI) ModuleManager.getModule("tabgui");
			JSONReader data = tabGui.getConfiguration();
			int posX = data.getInt("posX", 5);
			int posY = data.getInt("posY", 200);
			index = 0;
			for (Category category : ModuleManager.categories) {
//				drawRect(posX, posY+(index*20), 100, 20, new Color(80,  80, 80).getRGB());
				mc.fontRenderer.drawString((tabGui.categorySelected?"§8":"§b")+(tabGui.selected == index ? "§l":"")+category.getName(), posX, posY+(index*12)-5, new Color(240,240,240).getRGB(), true);
				index ++;
			}
			if (tabGui.categorySelected) {
				index = 0;
				Category cat = (Category) ModuleManager.categories.toArray()[tabGui.selected];
				for (Module mod : cat.getMods()) {
					mc.fontRenderer.drawString((mod.isEnabled()?"§a":"§c")+(tabGui.selectedModule == index ? "§l":"")+mod.getName(), posX+60, posY+(index*12)-5, new Color(240,240,240).getRGB(), true);
					index ++;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			ModuleManager.getModule("walktolocation").toggle();
		}
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			ModuleManager.getModule("tabgui").toggle();
		}
		
		else if (ModuleManager.getModule("tabgui").isEnabled()) {
			TabGUI tabgui = (TabGUI) ModuleManager.getModule("tabgui");
			if (tabgui.categorySelected) {
				try {
					int current = tabgui.selectedModule;
					int newvalue = -1;
					Category cat = (Category) ModuleManager.categories.toArray()[tabgui.selected];
					if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
						if (current <= 0) newvalue = cat.getMods().size()-1;
						else newvalue = current-1;
					}
				
					if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
						if (current >= cat.getMods().size()-1) newvalue = 0;
						else newvalue = current+1;
					}

					if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
						tabgui.categorySelected = false;
						tabgui.selectedModule = 0;
					}
					
					if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
						((Module)cat.getMods().toArray()[tabgui.selectedModule]).toggle();
					}
					
					if (newvalue != -1) {
						tabgui.selectedModule = newvalue;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			} else {
				int current = tabgui.selected;
				int newvalue = -1;
				
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					if (current <= 0) newvalue = ModuleManager.categories.size()-1;
					else newvalue = current-1;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					if (current >= ModuleManager.categories.size()-1) newvalue = 0;
					else newvalue = current+1;
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					Category selected = (Category) ModuleManager.categories.toArray()[current];
					tabgui.categorySelected = true;
				}
				
				if (newvalue != -1) {
					tabgui.selected = newvalue;
				}
			}
		}
	}
}
