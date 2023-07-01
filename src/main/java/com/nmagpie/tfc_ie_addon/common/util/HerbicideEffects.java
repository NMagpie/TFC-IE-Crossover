package com.nmagpie.tfc_ie_addon.common.util;

import java.util.List;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.entities.ChemthrowerShotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.ICropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.plant.fruit.SeasonalPlantBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;

public class HerbicideEffects
{
    public static void register()
    {
        ChemthrowerHandler.registerEffect(IETags.fluidHerbicide, new ChemthrowerHandler.ChemthrowerEffect()
        {
            @Override
            public void applyToEntity(LivingEntity target, @Nullable Player shooter, ItemStack thrower, Fluid fluid)
            {
            }

            @Override
            public void applyToBlock(Level world, HitResult mop, @Nullable Player shooter, ItemStack thrower, Fluid fluid)
            {
                if (!(mop instanceof BlockHitResult result))
                    return;

                BlockPos pos = result.getBlockPos();
                BlockState hit = world.getBlockState(pos);
                BlockPos above = pos.above();
                BlockState stateAbove = world.getBlockState(above);
                Block blockAbove = stateAbove.getBlock();
                for (SoilBlockType.Variant soil : SoilBlockType.Variant.values())
                {
                    if (hit.is(soil.getBlock(SoilBlockType.GRASS).get().defaultBlockState().getBlock()) || hit.is(soil.getBlock(SoilBlockType.FARMLAND).get().defaultBlockState().getBlock()))
                    {
                        if (blockAbove instanceof ICropBlock cropBlock)
                        {
                            if (world.getBlockEntity(above) instanceof CropBlockEntity cropBlockEntity)
                            {
                                final boolean mature = cropBlockEntity.getGrowth() >= 1f;
                                cropBlock.die(world, above, stateAbove, mature);
                                world.destroyBlock(above, true);
                            }
                        }
                        else if (blockAbove instanceof DeadCropBlock || blockAbove instanceof WildCropBlock)
                            world.destroyBlock(above, true);
                        else if (blockAbove instanceof BushBlock && !(blockAbove instanceof SeasonalPlantBlock))
                            world.removeBlock(above, false);
                        world.setBlockAndUpdate(pos, soil.getBlock(SoilBlockType.DIRT).get().defaultBlockState());
                    }
                    else if (hit.is(soil.getBlock(SoilBlockType.CLAY_GRASS).get().defaultBlockState().getBlock()))
                    {
                        if (blockAbove instanceof WildCropBlock)
                            world.destroyBlock(above, true);
                        else if (blockAbove instanceof BushBlock && !(blockAbove instanceof SeasonalPlantBlock))
                            world.removeBlock(above, false);
                        world.setBlockAndUpdate(pos, soil.getBlock(SoilBlockType.CLAY).get().defaultBlockState());
                    }
                }

                // Base IE behaviour
                if (hit.is(BlockTags.LEAVES))
                    world.removeBlock(pos, false);
                else if (hit.getBlock() instanceof SnowyDirtBlock || hit.getBlock() instanceof FarmBlock)
                {
                    world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                    if (blockAbove instanceof BushBlock)
                        world.removeBlock(above, false);
                }

                AABB aabb = new AABB(pos).inflate(.25);
                List<ChemthrowerShotEntity> otherProjectiles = world.getEntitiesOfClass(ChemthrowerShotEntity.class, aabb);
                for (ChemthrowerShotEntity shot : otherProjectiles)
                    shot.discard();
            }
        });
    }
}
