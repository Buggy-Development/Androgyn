package de.androgyn.util;

import net.minecraft.block.Block;

public class ABlock {

	private Block block;
	private int posX,posY,posZ;
	
	public ABlock(Block block, int posX,int posY,int posZ) {
		this.block = block;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public int getPosZ() {
		return posZ;
	}
	
}
