package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class JesusEvent extends RaptorClientMainEvent {

	private BlockPos blockPos;
	private AxisAlignedBB alignedBB;
	
	public JesusEvent(BlockPos pos) {
		super();
		this.blockPos = pos;
	}
	
	public BlockPos getBlockPos() {
		return blockPos;
	}
	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
	}
	public AxisAlignedBB getAlignedBB() {
		return alignedBB;
	}
	public void setAlignedBB(AxisAlignedBB alignedBB) {
		this.alignedBB = alignedBB;
	}
	
	
}
