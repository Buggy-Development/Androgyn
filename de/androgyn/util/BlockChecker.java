package de.androgyn.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class BlockChecker {
	
	public static boolean hasProblemsPassingBlock(Block block) {
		if (a(block, Blocks.WATER)||a(block,Blocks.FLOWING_WATER)||a(block,Blocks.VINE)) return true;
		return false;
	}
	
	public static boolean canNotPassBlock(Block block) {
		if (a(block, Blocks.LAVA)||a(block,Blocks.FLOWING_LAVA)||a(block,Blocks.CACTUS)||a(block,Blocks.WEB)||a(block,Blocks.FIRE)) return true;
		return false;
	}
	
	private static boolean a(Block b, Block c) {
		return b.getRegistryName().equals(c.getRegistryName());
	}

}
