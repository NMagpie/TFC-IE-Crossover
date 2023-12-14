package com.nmagpie.tfc_ie_addon.mixin;

import java.util.Objects;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.api.crafting.FluidTagInput;
import blusunrize.immersiveengineering.common.crafting.fluidaware.IngredientFluidStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;

@Mixin(IngredientFluidStack.class)
public abstract class IngredientFluidStackMixin extends Ingredient
{
    protected IngredientFluidStackMixin(Stream<? extends Value> values)
    {
        super(values);
    }

    @Shadow
    @Final
    private FluidTagInput fluidTagInput;

    @Shadow
    ItemStack[] cachedStacks;

    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true, remap = false)
    public void getItems(CallbackInfoReturnable<ItemStack[]> cir)
    {
        if (cachedStacks == null)
            cachedStacks = fluidTagInput.getMatchingFluidStacks()
                .stream()
                .flatMap(fluid -> Helpers.streamAllTagValues(TFCTags.Items.FLUID_ITEM_INGREDIENT_EMPTY_CONTAINERS, ForgeRegistries.ITEMS)
                    .map(item -> {
                        final ItemStack stack = new ItemStack(item);
                        final IFluidHandlerItem fluidHandler = Helpers.getCapability(stack, Capabilities.FLUID_ITEM);
                        if (fluidHandler != null)
                        {
                            fluidHandler.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
                            final FluidStack content = fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                            if (content.getFluid() == fluid.getFluid() && content.getAmount() >= fluid.getAmount())
                                return fluidHandler.getContainer();
                        }
                        return null;
                    }))
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new);
        cir.setReturnValue(cachedStacks);
    }
}
