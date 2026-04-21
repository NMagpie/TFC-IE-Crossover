package com.nmagpie.tfc_ie_addon.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor
{
    @Accessor("level")
    Level tfc_ie_addon$getLevel();

    @Accessor("worldPosition")
    BlockPos tfc_ie_addon$getWorldPosition();
}
