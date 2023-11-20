package com.nmagpie.tfc_ie_addon.mixin;

import blusunrize.immersiveengineering.api.IEApi;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.capabilities.size.ItemSizeManager;

@Mixin(IEApi.class)
public abstract class AllowInCrateMixin
{
    @Inject(method = "isAllowedInCrate", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void isAllowedInCrate(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        if (!ItemSizeManager.get(stack).getSize(stack).isEqualOrSmallerThan(Config.SERVER.crateMaximumItemSize.get()))
            cir.setReturnValue(false);
    }
}
