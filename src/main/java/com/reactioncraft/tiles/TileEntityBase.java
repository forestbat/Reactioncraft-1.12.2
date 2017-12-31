package com.reactioncraft.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created on 12/31/17.
 */
public class TileEntityBase extends TileEntity {

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock()!=newState.getBlock();
    }
}
