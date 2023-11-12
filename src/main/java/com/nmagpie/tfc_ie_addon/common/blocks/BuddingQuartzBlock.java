package com.nmagpie.tfc_ie_addon.common.blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import static com.nmagpie.tfc_ie_addon.common.blocks.Blocks.*;

@ParametersAreNonnullByDefault
public class BuddingQuartzBlock extends BuddingAmethystBlock
{
    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingQuartzBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (random.nextInt(15) == 0)
        {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate))
            {
                block = SMALL_QUARTZ_BUD.get();
            }
            else if (blockstate.is(SMALL_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction)
            {
                block = MEDIUM_QUARTZ_BUD.get();
            }
            else if (blockstate.is(MEDIUM_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction)
            {
                block = LARGE_QUARTZ_BUD.get();
            }
            else if (blockstate.is(LARGE_QUARTZ_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction)
            {
                block = QUARTZ_CLUSTER.get();
            }

            if (block != null)
            {
                BlockState blockstate1 = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(blockpos, blockstate1);
            }

        }
    }
}
