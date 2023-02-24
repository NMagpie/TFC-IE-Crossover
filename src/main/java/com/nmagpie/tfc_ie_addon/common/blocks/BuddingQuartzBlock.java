package com.nmagpie.tfc_ie_addon.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;

import static com.nmagpie.tfc_ie_addon.common.blocks.Blocks.*;

public class BuddingQuartzBlock extends BuddingAmethystBlock {

    private static final Direction[] DIRECTIONS = Direction.values();
    public BuddingQuartzBlock(Properties p_152726_) {
        super(p_152726_);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pRandom.nextInt(15) == 0) {
            Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pPos.relative(direction);
            BlockState blockstate = pLevel.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = SMALL_QUARTZ_BUD.get();
            } else if (blockstate.is(SMALL_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = MEDIUM_QUARTZ_BUD.get();
            } else if (blockstate.is(MEDIUM_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = LARGE_QUARTZ_BUD.get();
            } else if (blockstate.is(LARGE_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = QUARTZ_CLUSTER.get();
            }

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                pLevel.setBlockAndUpdate(blockpos, blockstate1);
            }

        }
    }
}
