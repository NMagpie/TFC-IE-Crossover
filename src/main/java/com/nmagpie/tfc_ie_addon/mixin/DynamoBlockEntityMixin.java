package com.nmagpie.tfc_ie_addon.mixin;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.metal.DynamoBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.common.blockentities.rotation.RotationSinkBlockEntity;
import net.dries007.tfc.util.rotation.NetworkAction;
import net.dries007.tfc.util.rotation.Node;
import net.dries007.tfc.util.rotation.SinkNode;

@Mixin(DynamoBlockEntity.class)
public abstract class DynamoBlockEntityMixin extends IEBaseBlockEntity implements IEBlockInterfaces.IStateBasedDirectional, IEServerTickableBE, RotationSinkBlockEntity
{
    @Shadow
    @Final
    private IRotationAcceptor rotationCap;

    public DynamoBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Unique
    private SinkNode tfc_ie_addon$node;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructor(BlockPos pos, BlockState state, CallbackInfo ci)
    {
        this.tfc_ie_addon$node = new SinkNode(pos, getFacing())
        {
            @NotNull
            @Override
            public String toString()
            {
                return "Dynamo[pos=%s, direction=%s]".formatted(pos(), getFacing());
            }
        };
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        performNetworkAction(NetworkAction.ADD);
    }

    @Override
    public void onChunkUnloaded()
    {
        super.onChunkUnloaded();
        performNetworkAction(NetworkAction.REMOVE);
    }

    @Override
    public void setRemovedIE()
    {
        super.setRemovedIE();
        performNetworkAction(NetworkAction.REMOVE);
    }

    @NotNull
    @Override
    public Node getRotationNode()
    {
        return tfc_ie_addon$node;
    }

    @Override
    public void tickServer()
    {
        if (tfc_ie_addon$node.rotation() != null)
        {
            rotationCap.inputRotation(Math.abs(tfc_ie_addon$node.rotation().speed() * 500 * Config.SERVER.tfcRotationalEnergyModifier.get()));
        }
    }
}
