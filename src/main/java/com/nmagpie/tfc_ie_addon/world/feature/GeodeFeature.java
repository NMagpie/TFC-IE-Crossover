package com.nmagpie.tfc_ie_addon.world.feature;

import java.util.List;
import java.util.function.Predicate;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.nmagpie.tfc_ie_addon.common.blocks.BuddingQuartzBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

import net.dries007.tfc.util.Helpers;

/**
 * {@link net.dries007.tfc.world.feature.TFCGeodeFeature but with filling configuration and placement}
 */

public class GeodeFeature extends Feature<GeodeConfig>
{

    public GeodeFeature(Codec<GeodeConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GeodeConfig> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();
        final RandomSource random = context.random();
        final GeodeConfig config = context.config();
        final WorldgenRandom worldgenrandom = new WorldgenRandom(new XoroshiroRandomSource(level.getSeed()));
        final NormalNoise normalnoise = NormalNoise.create(worldgenrandom, -4, 1.0D);
        boolean cracked = (double) random.nextFloat() < 0.95F;
        List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
        List<BlockPos> crackBlocks = Lists.newLinkedList();

        final UniformInt outerWall = UniformInt.of(4, 6);
        final UniformInt pointOffset = UniformInt.of(1, 2);
        final int distributionPoint = UniformInt.of(3, 4).sample(random);
        final double relativeDistributionPoint = (double) distributionPoint / (double) outerWall.getMaxValue();

        double fillLimit = 1.0D / Math.sqrt(1.7); // mojang magic numbers
        double innerLimit = 1.0D / Math.sqrt(2.2 + relativeDistributionPoint);
        double middleLimit = 1.0D / Math.sqrt(3.2 + relativeDistributionPoint);
        double outerLimit = 1.0D / Math.sqrt(4.2 + relativeDistributionPoint);
        double distWeight = 1.0D / Math.sqrt(2 + random.nextDouble() / 2.0D + (distributionPoint > 3 ? relativeDistributionPoint : 0.0D));

        int invalid = 0;
        for (int i1 = 0; i1 < distributionPoint; ++i1)
        {
            int x = outerWall.sample(random);
            int y = outerWall.sample(random);
            int z = outerWall.sample(random);
            BlockPos offsetPos = origin.offset(x, y, z);
            BlockState found = level.getBlockState(offsetPos);
            if (found.isAir() || Helpers.isBlock(found, BlockTags.GEODE_INVALID_BLOCKS))
            {
                ++invalid;
                if (invalid > 1) return false;
            }
            list.add(Pair.of(offsetPos, pointOffset.sample(random)));
        }

        if (cracked)
        {
            int type = random.nextInt(4);
            int dist = distributionPoint * 2 + 1;
            if (type == 0)
            {
                crackBlocks.add(origin.offset(dist, 7, 0));
                crackBlocks.add(origin.offset(dist, 5, 0));
                crackBlocks.add(origin.offset(dist, 1, 0));
            }
            else if (type == 1)
            {
                crackBlocks.add(origin.offset(0, 7, dist));
                crackBlocks.add(origin.offset(0, 5, dist));
                crackBlocks.add(origin.offset(0, 1, dist));
            }
            else if (type == 2)
            {
                crackBlocks.add(origin.offset(dist, 7, dist));
                crackBlocks.add(origin.offset(dist, 5, dist));
                crackBlocks.add(origin.offset(dist, 1, dist));
            }
            else
            {
                crackBlocks.add(origin.offset(0, 7, 0));
                crackBlocks.add(origin.offset(0, 5, 0));
                crackBlocks.add(origin.offset(0, 1, 0));
            }
        }

        List<BlockPos> list2 = Lists.newArrayList();
        final Predicate<BlockState> predicate = isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);

        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-16, -16, -16), origin.offset(16, 16, 16)))
        {
            double noise = normalnoise.getValue(pos.getX(), pos.getY(), pos.getZ()) * 0.05;
            double pointAt = 0.0D;
            double crackWeight = 0.0D;

            for (Pair<BlockPos, Integer> pair : list)
            {
                pointAt += Mth.invSqrt(pos.distSqr(pair.getFirst()) + (double) pair.getSecond()) + noise;
            }

            for (BlockPos crackPos : crackBlocks)
            {
                crackWeight += Mth.invSqrt(pos.distSqr(crackPos) + 2D) + noise;
            }

            if (!(pointAt < outerLimit))
            {
                if (cracked && crackWeight >= distWeight && pointAt < fillLimit)
                {
                    this.safeSetBlock(level, pos, Blocks.AIR.defaultBlockState(), predicate);

                    for (Direction d : Helpers.DIRECTIONS)
                    {
                        BlockPos relativePos = pos.relative(d);
                        FluidState fluid = level.getFluidState(relativePos);
                        if (!fluid.isEmpty())
                        {
                            level.scheduleTick(relativePos, fluid.getType(), 0);
                        }
                    }
                }
                else if (pointAt >= fillLimit)
                {
                    this.safeSetBlock(level, pos, config.filling().getRandomValue(random).orElse(Blocks.AIR.defaultBlockState()), predicate);
                }
                else if (pointAt >= innerLimit)
                {
                    boolean flag1 = (double) random.nextFloat() < 0.083D;

                    this.safeSetBlock(level, pos, config.inner().getRandomValue(random).orElseThrow(), predicate);

                    if ((flag1) && (double) random.nextFloat() < 0.35D)
                    {
                        list2.add(pos.immutable());
                    }
                }
                else if (pointAt >= middleLimit)
                {
                    this.safeSetBlock(level, pos, config.middle(), predicate);
                }
                else if (pointAt >= outerLimit)
                {
                    this.safeSetBlock(level, pos, config.outer(), predicate);
                }
            }
        }

        for (BlockPos blockpos4 : list2)
        {
            BlockState blockstate1 = config.innerPlacements().getRandomValue(random).orElseThrow();

            for (Direction direction : Helpers.DIRECTIONS)
            {
                if (blockstate1.hasProperty(BlockStateProperties.FACING))
                {
                    blockstate1 = blockstate1.setValue(BlockStateProperties.FACING, direction);
                }

                BlockPos blockpos5 = blockpos4.relative(direction);
                BlockState blockstate2 = level.getBlockState(blockpos5);
                if (blockstate1.hasProperty(BlockStateProperties.WATERLOGGED))
                {
                    blockstate1 = blockstate1.setValue(BlockStateProperties.WATERLOGGED, blockstate2.getFluidState().isSource());
                }

                if (BuddingQuartzBlock.canClusterGrowAtState(blockstate2))
                {
                    this.safeSetBlock(level, blockpos5, blockstate1, predicate);
                    break;
                }
            }
        }

        return true;
    }

}
