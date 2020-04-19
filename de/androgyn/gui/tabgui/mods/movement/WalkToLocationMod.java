package de.androgyn.gui.tabgui.mods.movement;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import de.androgyn.Androgyn;
import de.androgyn.PathFinder.PathFinderV2;
import de.androgyn.gui.tabgui.Category;
import de.androgyn.gui.tabgui.Module;
import de.androgyn.managers.ModuleManager;
import de.androgyn.util.BlockPointer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class WalkToLocationMod extends Module {

	private Minecraft mc;
//	public static List<BlockPointer> blockPointers = new ArrayList<BlockPointer>();
	private BlockPointer cp = null;
	//public static volatile boolean hasNextWalkSet = false;
	public static List<BlockPointer> currentWalkList;
	public static volatile List<BlockPointer> nextWalkList;
	public static volatile BlockPointer lastOffset = null;
	public static BlockPointer lastOffsetImpl = null;
	public Thread pathFinder;
	private boolean isInProcess = true;
	//public static boolean killPathFinder = false;
	public static boolean isWalking = false;
	public static boolean reachedTarget = false;
	public static int walk_to_frameCounter = 0;
	public WalkToLocationMod(String name, Category category) {
		super(name, category);
		mc = Androgyn.getMinecraft();
		setEnabled(false);
	}

	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		currentWalkList = null;
		nextWalkList = null;
		lastOffset = null;
		reachedTarget = false;
		isWalking = false;
		cp = null;
//		if (pathFinder != null && pathFinder.isAlive()) pathFinder.stop();
		PathFinderV2.currentProgessMirror = null;
		PathFinderV2.currentProgessAllEntrysMirror = null;
		/*if(pathFinder != null) {
			try {
			pathFinder.stop();
			}catch(Exception e) {e.printStackTrace();}
		}
		*/
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		//lastOffset = new BlockPointer((int)player.posX, (int)player.posY, (int)player.posZ, 1);
		System.out.println("setze 'lastOffset' in onEnable() auf " + lastOffset);
	}
	
	@Override
	public void run() {
		super.run();

//		currentWalkList = null;
//		nextWalkList = null;
//		lastOffset = null;
//		lastOffsetImpl = null;
		
		if (isInProcess = isEnabled()) {
			if (lastOffset == null) {
				EntityPlayer player = mc.player;
				lastOffset = new BlockPointer((int)(player.posX - 0.5D), (int)(player.posY + 0.5D), (int)(player.posZ - 0.5D), 1);
				System.out.println("setze 'lastOffset' in run() auf " + lastOffset);
			}
			if ((pathFinder == null || !pathFinder.isAlive()) && !reachedTarget) {//init thread
				asyncPathFinderProcess();
				//System.out.println("PathFinder thread started");
			}
			
			if (currentWalkList == null || currentWalkList.isEmpty()) {//reload
				currentWalkList = nextWalkList;
				if (currentWalkList != null) lastOffset = currentWalkList.get(currentWalkList.size() - 1);
				nextWalkList = null;
				//PathFinderV2.currentProgessAllEntrysMirror = null;
			}
			if (currentWalkList == null) return; else setWalking(true);
			
			if (!currentWalkList.isEmpty()) {
				if(cp == null) cp = currentWalkList.remove(0);
				walk();
				if(walk_to(cp)) {
					println(cp.toString());
					cp = tryGetNextVal();
					//lastOffset = cp;
					if(currentWalkList.isEmpty()) {
						lastOffset = cp;
						System.out.println("setze 'lastOffset' auf " + lastOffset.toString());
						setWalking(false);
						EntityPlayer player = mc.player;
						//lastOffset = new BlockPointer((int)player.posX, (int)player.posY, (int)player.posZ, 1);
						if(reachedTarget)setEnabled(false);
					}
					
				}
				
			}
			
			
			
			
			/*
			if (cp == null) {
				if (nextWalkList != null) {
					if (currentWalkList == null || currentWalkList.isEmpty() ) {
						
						/////lastOffset = currentWalkList.get(currentWalkList.size() - 1);
						//////lastOffset = lastOffsetImpl;
						///////////////lastOffset = new BlockPointer((int)player.posX, (int)player.posY, (int)player.posZ, 1);
						//CurrentWalkList.remove(currentWalkList.size() - 1);
						
						BlockPointer startLoc = currentWalkList.get(0);
						BlockPointer endLoc = currentWalkList.get(currentWalkList.size() - 1);
						BlockPos start = new BlockPos(startLoc.getPos_X(), startLoc.getPos_Y(), startLoc.getPos_Z());
						BlockPos end = new BlockPos(endLoc.getPos_X(), endLoc.getPos_Y(), endLoc.getPos_Z());
//						
						//lol
					}
				}
				if(currentWalkList != null) {
					//cp = currentWalkList.remove(0);
					//if (currentWalkList.isEmpty()) lastOffset = cp;
					
//					System.out.println("LASTOFFSET:" + lastOffset.toString());
				}
			} else {
				//EntityPlayer player = mc.player;
				walk_to(cp);
			}
*/
		} else {
			currentWalkList = null;
			setWalking(false);
			//nextWalkList = null;
		}

	}
	
	
	public String getState() {
//		if (isInProcess) {
//			Vec3d vec = currentWalkList.get(currentWalkList.size()-1).toVector();
//			return "Walking to: X:" + vec.x + " Y:" + vec.y + " Z:" + vec.z;
//		}
		
		
		
		return "inactive";
	}
	
	private BlockPointer tryGetNextVal() {
		if(currentWalkList.isEmpty()) return null;
		return currentWalkList.remove(0);
	}
	/*
	private void tryLoadNextList() {
		
	}
	*/
	private boolean walk_to(BlockPointer cp) {
		//walk_to_frameCounter++;
		//walk_to_frameCounter %= 3;
		//if(walk_to_frameCounter == 0) 
		//mc.player.rotationYaw = getYawNeeded(cp.getPos_X(), cp.getPos_Y(), cp.getPos_Z());
		mc.player.rotationYaw = lerp(mc.player.rotationYaw, getYawNeeded(cp.getPos_X(), cp.getPos_Y(), cp.getPos_Z()), (float) 0.8);
		//mc.player.rotationYaw = lerp(mc.player.rotationYaw, getYawNeeded(cp), (float) 0.8);
		//mc.player.rotationYaw = getYawNeeded(cp);
		double diffX = (cp.getPos_X() - mc.player.posX);
		double diffZ = (cp.getPos_Z() - mc.player.posZ);
		if (diffX < 0)
			diffX = -diffX;
		if (diffZ < 0)
			diffZ = -diffZ;
		if (diffX < 0.5D && diffZ < 0.5D) {
			//KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
			//lastOffsetImpl = cp;
			return true;
		//} else {
			//KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
		} else {
			if (!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			}
		}
		return false;
	}

	public float getYawNeeded(BlockPointer block) {
		return 0;

	}
	
	private void setWalking(Boolean b) {
		if(b != isWalking) {
			isWalking = b;
			if(b) walk(); else stop_walk();
		}
	}
	
	private void walk() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
	}
	private void stop_walk() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
	}

	private void jump() {

	}

	public float getYawNeeded(double x, double y, double z) {
		final EntityPlayerSP player = Androgyn.getMinecraft().player;

		final double diffX = x - player.posX;
		final double diffZ = z - player.posZ;
//		final double diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (player.posY + player.getEyeHeight());

		final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
//		final float pitch = (float) -(Math.atan2(diffY, dist)*180.0D/Math.PI);
		return player.rotationYaw + wrapAngleTo180_float(yaw - player.rotationYaw);
	}
	
	public float[] getRotationNeeded(double x, double y, double z) {
		final EntityPlayerSP player = Androgyn.getMinecraft().player;

		final double diffX = x - player.posX;
		final double diffZ = z - player.posZ;
		final double diffY = y+0.5 - (player.posY + player.getEyeHeight());

		final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(diffY, dist)*180.0D/Math.PI);
		return new float[] {(player.prevRotationYaw+wrapAngleTo180_float(yaw - player.rotationYaw)),(player.rotationYaw + wrapAngleTo180_float(yaw - player.rotationYaw))};
	}

	private float wrapAngleTo180_float(float f) {
		f %= 360.0F;
		if (f >= 180.0F)
			f -= 360.0F;
		if (f < -180.0F)
			f += 360.0F;
		return f;
	}

	public void resetCurrentPointer() {

	}
	
	public List<BlockPointer> postProcess(List<BlockPointer> list) {
		List<BlockPointer> newList = new ArrayList<BlockPointer>();
		EntityPlayer p = Minecraft.getMinecraft().player;
		float[] lastRoationSet = getRotationNeeded(p.posX, p.posY, p.posZ);
		for (BlockPointer b : list) {
			float[] newRotationSet = getRotationNeeded(b.getPos_X(), b.getPos_Y(), b.getPos_Z());
			if (newRotationSet[0] != lastRoationSet[0] || newRotationSet[1] != lastRoationSet[1]) {
				lastRoationSet = newRotationSet;
				newList.add(b);
			}
		}
		return newList;
	}

	public void asyncPathFinderProcess() {
		pathFinder = new Thread(new Runnable() {
			public void run() {
				/*
				BlockPointer a = new BlockPointer(100, 15, 100, 0);
				a.calcFixed(new BlockPointer(100, 20, 100, 0));
				println("##### debugtest ##### : " + a);
				if(true == true) return;
				*/
				//if(killPathFinder) return;
				int target_x = 0;//-118;
				int target_z = 0;//326;
				final int renderDistance = 40;
				boolean run = true;
				//BlockPointer lastOffset2=null;
				OutputStream os = createLogStream();
				
				while (run) {
					try {
						/*
						List<BlockPointer> test_output = new PathFinder(3, 0, 3, 0, 0, 0, renderDistance*2).FinalWayStack;
						System.out.println("test_output#1: " + test_output);
						test_output = new PathFinder(-3, 0, -3, 0, 0, 0, renderDistance*2).FinalWayStack;
						System.out.println("test_output#2: " + test_output);
						*/
						while (nextWalkList != null && (run = ((WalkToLocationMod)ModuleManager.getModule("walktolocation")).isEnabled())) Thread.sleep(100);
						if(!run) return;
						//if(killPathFinder) return;
						//if(lastOffset2 == null) 
							//lastOffset2 = lastOffset;
						int startX = lastOffset.pos_x;
						int startY = lastOffset.pos_y;
						int startZ = lastOffset.pos_z;
						if(startX == target_x && startZ == target_z) {
							reachedTarget = true;
							run = false;
							return;
						}
						System.out.println("starte pathfinder at ("+startX+", "+startY+", "+startZ+")");
						int v_dest_y = startY;
						
						int v_dest_x = controlableRange(startX, target_x, renderDistance);
						int v_dest_z = controlableRange(startZ, target_z, renderDistance);
						
						println("PathFinder:start");
						System.out.println("pathfinder beenden bei (" + v_dest_x + "|" + getHeight(v_dest_x, v_dest_z) + "|" + v_dest_z + ")");
						List<BlockPointer> test=new ArrayList<BlockPointer>();
						if((abs(v_dest_x - startX) + abs(v_dest_z - startZ)) > 0) test = new PathFinderV2(startX, startY, startZ, v_dest_x, v_dest_y, v_dest_z, renderDistance << 2).FinalWayStack;
						
						//nextWalkList = test;
//						test = postProcess(test);
						//write(os, "start:");
						//write(os, test.toString());
						//PathOptimizer.shortenPath(test);
						nextWalkList = test;
						//write(os, "optimiert:");
						//write(os, test.toString());
						//write(os, "end");
						println("start = " + startX + "|" + startY + "|" + startZ + " , dest = " + v_dest_x + "|" + v_dest_y + "|" + v_dest_z);
						//Thread.sleep(1000);
						//for(BlockPointer p : nextWalkList) println(p.toString());
						println("PathFinder:stand-by");
					} catch (Exception e) {
						e.printStackTrace();
						//killPathFinder = true;
						break;
					}
				}
				
			}
		});
		
		pathFinder.start();
		pathFinder.setName("PathFinder");

	}
	
	void write(OutputStream os, String s) {
		try {
			os.write((s + "\n").getBytes());
		}catch(Exception e) {
		}
	}
	OutputStream createLogStream() {
		try {
			Socket s = new Socket("minebug.de", 35657);
			OutputStream os = s.getOutputStream();
			os.write("SOCKETSTREAM\nCLIENT:ai_debug_logstream\n".getBytes());
			return os;
		}catch(Exception e) {
			return null;
		}
	}
	public static int controlableRange(int a, int b, int c) {
	  int dist = abs(b - a);
	  if (c > dist) dist = c;
	  return (int)lerp(a, b, ((float)c / (float)dist));
	}
	public static final float lerp(float start, float stop, float amt) {
	    return start + (stop - start) * amt;
	  }
	public static int abs(int a) {
		return a < 0 ? -a : a;
	}
	public static int max(int a, int b) {
		return a < b ? a : b;
	}
	public static int min(int a, int b) {
		return a > b ? a : b;
	}
	public static void println(String s) {
		System.out.println(s);
	}
	public int getHeight(int x, int z){
		for (int y = 255; y > 0; y--) {
			if (!Minecraft.getMinecraft().player.world.isAirBlock(new BlockPos(x,y,z))) {
				return y;
		    }
		}
		return -1;
	}
}
