package de.androgyn.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockPointer {

	public double recoursive_way_cost;
	public double local_way_cost;
	public int pos_x, pos_y, pos_z;
    private int y_original;
	public BlockPointer last;
	public float lastLengthCalc;

	public BlockPointer(int x, int y, int z, float neededPower) {
		local_way_cost = neededPower;
		pos_x = x;
		pos_y = y;
		pos_z = z;
		y_original = y;
	}
	public BlockPointer(int x, int y, int z) {
		pos_x = x;
		pos_y = y;
		pos_z = z;
		y_original = y;
	}

	public BlockPointer(int x, int y, int z, float neededPower, BlockPointer last) {
		this(x, y, z, neededPower);
		setParent(last);
	}

	public void setParent(BlockPointer last) {
		this.last = last;
		// recoursive_way_cost += local_way_cost;
		this.recoursive_way_cost = applyCost(last);
	}

	public double applyCost(BlockPointer last) {
		float dx = pos_x - last.pos_x;
		double dy = getHeightCost(last, this);// 0;// pos_y - last.pos_y;
		float dz = pos_z - last.pos_z;
		double local_mult = Math.sqrt(dx * dx + dy * dy + dz * dz);
		return last.recoursive_way_cost + (local_mult * this.local_way_cost);
	}

	public String toString() {
		return "pos:[" + pos_x + ", " + pos_y + ", " + pos_z + "] pwr:" + local_way_cost + " pwr_global:"
				+ recoursive_way_cost;
	}

	double getHeightCost(BlockPointer source, BlockPointer destination) {
		int h1 = source.pos_y;
		int h2 = destination.pos_y;
		int diff = h2 - h1;
		h1 = diff < 0 ? -diff : diff;
		//if (h1 < 2) return h1;
		//if(diff < 2 && diff >= -3) return 1;
		if(diff >= 2 || diff < -3) return 5000;
		if (diff == 1) return 5;
		if (diff == 0) return 1;
		if (diff == -1 || diff == -2 || diff == -3) return 0;
		
		//}
		// diff = -diff;
		// return diff * diff;
		return 5000;
	}

	public void getAirDistanceTo(int x, int y, int z) {
		float dx = pos_x - x;
		float dy = 0;// pos_y - y;
		float dz = pos_z - z;
		lastLengthCalc = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public String getPositionString() {
		return pos_x + "|" + pos_y + "|" + pos_z;
	}

	public static List<BlockPointer> mapBlocks(int range) {
		
		/*
		 * try { throw new Exception("dummy"); }catch(Exception e) {
		 * e.printStackTrace(); }
		 */
		long current = System.currentTimeMillis();
		List<BlockPointer> blocks = new ArrayList<BlockPointer>();
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		int baseX = (int) Math.round(player.posX);
		int baseZ = (int) Math.round(player.posZ);
		int c = 0;
		int y;
		for (int x = baseX - range; x < baseX + range; x++) {
			for (int z = baseZ - range; z < baseZ + range; z++) {
				y = getHeighestBlock(x, z);
				int cost = 1;
				if(isMaterial(x, y, z, Material.WATER)) cost = 5;
				if(isMaterial(x, y, z, Material.LAVA)) cost = 50000;
				blocks.add(new BlockPointer(x, y, z, cost));
			}

//			System.gc();
		}
		System.out.println(System.currentTimeMillis() - current);
		return blocks;
	}
	
	public void resetHeight() {
		pos_y = y_original;
	}

	public static int getHeighestBlock(int x, int z) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		for (int y = 255; y >= 0; y--) {
			//if (!player.world.isAirBlock(new BlockPos(x, y, z))) {
			if (!isBlockPassable(x, y, z) || isMaterial(x, y, z, Material.WATER)) {
				return y;
			}
		}
		return 255;
	}

	public void calcFixed(BlockPointer target) {
		//pos_y = 15
		//target = 20
		//System.out.println("calcFixed : pre : y=" + pos_y);
		//calcFixed(parent.pos_x, parent.pos_z, parent.pos_y);
		//System.out.println("calcFixed : post : y=" + pos_y);
//		if (target.pos_y >= 3) System.out.println("Called calcFixed on block -> " +target.pos_x + " " + target.pos_y + " " + target.pos_z);
		if(target.pos_y > pos_y) {//20 > 15
			//System.out.println("target.pos_y("+target.pos_y+") > pos_y("+pos_y+")");
			for (int i = -1; i <= 1; i++) {
				int yb = pos_y + i;//20 -10..0
				if (canPass(target.pos_x, yb, target.pos_z, i < 0)) {
					//target.pos_y = yb;
					//System.out.println("BlockPointer.calcFixed() : (" + target.pos_x + " " + target.pos_y + " " + target.pos_z + ") -> (" + target.pos_x + " " + yb + " " + target.pos_z + ")" );
					target.pos_y = yb;
				} else {
//					System.out.println("Can not walk to: (" + target.pos_x + " " + yb + " " + target.pos_z + ")");
				}
			}
		}
	}
	/*
	//p=3 pos_y=7
	public void calcFixed(int x, int z, int parent_block_y) {//wenn this zu hoch, dann fixen
		//if (canPass(x, parent_block_y, z)) System.out.println("BlockPointer.calcFixed() : y = " + pos_y);
		System.out.println("(" + x + " " + pos_y + " " + z + ")");
		if(parent_block_y > pos_y) {
			for (int i = 0; i <= 0; i++) {
				int yb = parent_block_y + i;
				if (canPass(x, yb, z)) {
					System.out.println("BlockPointer.calcFixed() : (" + x + " " + pos_y + " " + z + ") -> (" + x + " " + yb + " " + z + ")" );
					pos_y = yb;
					
				}
			}
		}
	}
*/
	public static boolean canPass(int x, int y, int z, boolean extra) {
		if(y < 0) return false;
		extra = extra ? isBlockPassable(x, y +3, z) : true;
		return isBlockPassable(x, y +1, z) 
				&& isBlockPassable(x, y +2, z)
				&& /*isBlockPassable(x, y +3, z)*/extra;//(!extra || isBlockPassable(x, y +3, z));
	}
	
	public static boolean isBlockPassable(int x, int y, int z) {
		World w = Minecraft.getMinecraft().world;
		//Block b = w.getBlockState(new BlockPos(x,y,z)).getBlock();
		if(w.getBlockState(new BlockPos(x,y,z)).getMaterial().equals(Material.LEAVES)) return false;
		return w.getBlockState(new BlockPos(x,y,z)).getBlock().isPassable(w, new BlockPos(x,y,z)) || w.isAirBlock(new BlockPos(x, y, z));
	}
	
	public static boolean isMaterial(int x, int y, int z, Material material) {
		return Minecraft.getMinecraft().world.getBlockState(new BlockPos(x,y,z)).getMaterial().equals(material);
	}

	public double getPos_X() {
		return pos_x + 0.5;
	}

	public double getPos_Y() {
		return pos_y + 0.5;
	}

	public double getPos_Z() {
		return pos_z + 0.5;
	}

	public Vec3d toVector() {
		return new Vec3d(pos_x, pos_y, pos_z);
	}
}