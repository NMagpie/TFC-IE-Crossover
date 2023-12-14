package com.nmagpie.tfc_ie_addon.util;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import com.nmagpie.tfc_ie_addon.config.Config;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import net.dries007.tfc.common.capabilities.heat.HeatCapability;

public class IEHeatHandler
{
    public static class CrucibleHeater implements ExternalHeaterHandler.IExternalHeatable
    {
        private final BlockEntity blockEntity;

        public CrucibleHeater(BlockEntity blockEntity)
        {
            this.blockEntity = blockEntity;
        }

        @Override
        public int doHeatTick(int energyAvailable, boolean redstone)
        {
            return blockEntity.getCapability(HeatCapability.BLOCK_CAPABILITY).map(handler ->
            {
                int FEPerTick = Config.SERVER.crucibleExternalHeaterFEPerTick.get();
                if (energyAvailable >= FEPerTick && !redstone)
                {
                    handler.setTemperature(Config.SERVER.crucibleExternalHeaterTemperature.get());
                    return FEPerTick;
                }
                handler.setTemperature(0);
                return 0;
            }).orElse(0);
        }
    }

    public static class Provider implements ICapabilityProvider
    {
        private final CrucibleHeater heater;
        private final LazyOptional<ExternalHeaterHandler.IExternalHeatable> lazy;

        public Provider(BlockEntity blockEntity)
        {
            this.heater = new CrucibleHeater(blockEntity);
            this.lazy = LazyOptional.of(() -> heater);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return ExternalHeaterHandler.CAPABILITY.orEmpty(cap, lazy);
        }

        public void invalidate()
        {
            lazy.invalidate();
        }
    }
}
