package com.nmagpie.tfc_ie_addon.mixin;

import java.util.Objects;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.common.crafting.fluidaware.IngredientFluidStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.Helpers;

/**
 * {@link net.dries007.tfc.common.recipes.ingredients.FluidContentIngredient}
 */
@Mixin(IngredientFluidStack.class)
public abstract class IngredientFluidStackMixin
{
    @Shadow
    @Final
    private SizedFluidIngredient fluidIngredient;

    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    public void getItems(CallbackInfoReturnable<Stream<ItemStack>> cir)
    {
        cir.setReturnValue(
            RecipeHelpers.stream(fluidIngredient)
                .flatMap(fluid -> Helpers.allItems(TFCTags.Items.FLUID_ITEM_INGREDIENT_EMPTY_CONTAINERS)
                    .map(item -> {
                        final ItemStack stack = new ItemStack(item);
                        final IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
                        if (fluidHandler != null)
                        {
                            // Attempt to fill with the current fluid
                            fluidHandler.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);

                            // Then attempt to drain, and ensure the content matches the filled fluid, and is of amount > the required amount.
                            final FluidStack content = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                            if (content.getFluid() == fluid && content.getAmount() >= this.fluidIngredient.amount())
                            {
                                return fluidHandler.getContainer();
                            }
                        }
                        return null;
                    }))
                .filter(Objects::nonNull)
        );
    }
}
