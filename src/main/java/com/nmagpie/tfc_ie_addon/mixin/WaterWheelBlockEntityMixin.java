package com.nmagpie.tfc_ie_addon.mixin;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.api.utils.CapabilityReference;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blocks.rotation.WaterWheelBlock;
import net.dries007.tfc.util.rotation.Rotation;

@Mixin(WaterWheelBlockEntity.class)
public abstract class WaterWheelBlockEntityMixin extends BlockEntity
{
    protected WaterWheelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Unique
    private final CapabilityReference<IRotationAcceptor> tfc_ie$outputCap = CapabilityReference.forNeighbor(
        ((WaterWheelBlockEntity) (Object) this), IRotationAcceptor.CAPABILITY, tfc_ie$getFacing()
    );

    @Unique
    private final CapabilityReference<IRotationAcceptor> tfc_ie$reverseOutputCap = CapabilityReference.forNeighbor(
        ((WaterWheelBlockEntity) (Object) this), IRotationAcceptor.CAPABILITY, tfc_ie$getFacing().getOpposite()
    );

    @Unique
    private Direction tfc_ie$getFacing()
    {
        Direction.Axis axis = getBlockState().getValue(WaterWheelBlock.AXIS);
        return switch (axis)
        {
            case X -> Direction.EAST;
            case Y -> Direction.SOUTH;
            default -> Direction.NORTH;
        };
    }

    @Unique
    private static CapabilityReference<IRotationAcceptor> tfc_ie$getOutputCap(WaterWheelBlockEntity wheel)
    {
        CapabilityReference<IRotationAcceptor> cap = ((WaterWheelBlockEntityMixin) (Object) wheel).tfc_ie$outputCap;
        if (cap == null || cap.getNullable() == null)
            cap = ((WaterWheelBlockEntityMixin) (Object) wheel).tfc_ie$reverseOutputCap;
        return cap;
    }

    @Inject(method = "serverTick", at = @At(value = "HEAD"), remap = false)
    private static void serverTick(Level level, BlockPos pos, BlockState state, WaterWheelBlockEntity wheel, CallbackInfo ci)
    {
        IRotationAcceptor dynamo = tfc_ie$getOutputCap(wheel).getNullable();
        if (dynamo != null)
        {
            Rotation rotation = wheel.getRotationNode().rotation();
            if (rotation != null)
            {
                double power = rotation.speed() * 50 * Config.SERVER.tfcWaterWheelEnergyModifier.get();
                dynamo.inputRotation(Math.abs(power));
            }
        }
    }
}
