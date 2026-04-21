package com.nmagpie.tfc_ie_addon.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;

@Mixin(CrucibleBlockEntity.class)
public interface CrucibleBlockEntityAccessor
{
    @Accessor("targetTemperature")
    void tfc_ie_addon$setTargetTemperature(float targetTemperature);

    @Accessor("targetTemperatureStabilityTicks")
    void tfc_ie_addon$setTargetTemperatureStabilityTicks(int targetTemperatureStabilityTicks);
}
