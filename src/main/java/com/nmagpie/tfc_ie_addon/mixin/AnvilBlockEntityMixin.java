package com.nmagpie.tfc_ie_addon.mixin;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.llamalad7.mixinextras.sugar.Local;
import com.nmagpie.tfc_ie_addon.mixin.accessor.BlockEntityAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.Helpers;

/**
 * Outputs slag when high carbon steel ingot is forged
 */
@Mixin(AnvilBlockEntity.class)
public abstract class AnvilBlockEntityMixin implements BlockEntityAccessor
{
    @Inject(method = "work", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/recipes/AnvilRecipe;assemble(Lnet/dries007/tfc/common/recipes/AnvilRecipe$Inventory;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"), remap = false)
    private void work(ServerPlayer player, ForgeStep step, CallbackInfo ci, @Local AnvilRecipe recipe)
    {
        // implementation of AnvilRecipe.byId() is bugged in 1.21 so changed to check if the recipe input is a pig iron ingot
        if (recipe.getInput().test(new ItemStack(TFCItems.METAL_ITEMS.get(Metal.PIG_IRON).get(Metal.ItemType.INGOT))))
        {
            ItemStack slag = new ItemStack(IEItems.Ingredients.SLAG);
            if (!player.getInventory().add(slag))
                Helpers.spawnItem(this.tfc_ie_addon$getLevel(), this.tfc_ie_addon$getWorldPosition(), slag);
        }
    }
}
