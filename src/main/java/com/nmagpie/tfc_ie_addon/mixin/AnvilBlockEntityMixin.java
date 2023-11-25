package com.nmagpie.tfc_ie_addon.mixin;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.mixin.accessor.BlockEntityAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.common.capabilities.forge.Forging;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.util.Helpers;

/**
 * Outputs slag when high carbon steel ingot is forged
 */
@Mixin(AnvilBlockEntity.class)
public abstract class AnvilBlockEntityMixin implements BlockEntityAccessor
{
    @Inject(method = "work", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/recipes/AnvilRecipe;assemble(Lnet/dries007/tfc/common/recipes/AnvilRecipe$Inventory;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void work(ServerPlayer player, ForgeStep step, CallbackInfoReturnable<InteractionResult> cir, ItemStack stack, Forging forge, ItemStack hammer, InteractionHand hammerSlot, AnvilRecipe recipe)
    {
        if (recipe.getId().toString().equals("tfc:anvil/high_carbon_steel_ingot"))
        {
            ItemStack slag = new ItemStack(IEItems.Ingredients.SLAG);
            if (!player.getInventory().add(slag))
                Helpers.spawnItem(this.getLevel(), this.getWorldPosition(), slag);
        }
    }
}
