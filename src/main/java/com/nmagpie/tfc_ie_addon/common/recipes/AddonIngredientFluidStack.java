package com.nmagpie.tfc_ie_addon.common.recipes;

import java.util.Objects;
import java.util.Optional;
import blusunrize.immersiveengineering.api.crafting.FluidTagInput;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.recipes.ingredients.DelegateIngredient;
import net.dries007.tfc.util.Helpers;

public class AddonIngredientFluidStack extends DelegateIngredient
{
    private final FluidTagInput fluidTagInput;

    public AddonIngredientFluidStack(@Nullable Ingredient delegate, FluidTagInput fluidTagInput)
    {
        super(delegate);
        this.fluidTagInput = fluidTagInput;
    }

    @Nonnull
    @Override
    protected ItemStack[] getDefaultItems()
    {
        return fluidTagInput.getMatchingFluidStacks()
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
    }

    @Override
    public boolean test(@Nullable ItemStack stack)
    {
        if (stack == null)
            return false;
        Optional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack).resolve();
        return handler.isPresent() && fluidTagInput.extractFrom(handler.get(), FluidAction.SIMULATE);
    }

    public ItemStack getExtractedStack(ItemStack input)
    {
        Optional<IFluidHandlerItem> handlerOpt = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(input, 1)).resolve();
        if (handlerOpt.isPresent())
        {
            IFluidHandlerItem handler = handlerOpt.get();
            fluidTagInput.extractFrom(handler, FluidAction.EXECUTE);
            return handler.getContainer();
        }
        return input.getCraftingRemainingItem();
    }

    @Nonnull
    @Override
    public IIngredientSerializer<? extends DelegateIngredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    public enum Serializer implements IIngredientSerializer<AddonIngredientFluidStack>
    {
        INSTANCE;

        @Nonnull
        @Override
        public AddonIngredientFluidStack parse(@Nonnull FriendlyByteBuf buffer)
        {
            final Ingredient internal = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);
            final FluidTagInput fluidTagInput = FluidTagInput.read(buffer);
            return new AddonIngredientFluidStack(internal, fluidTagInput);
        }

        @Nonnull
        @Override
        public AddonIngredientFluidStack parse(@Nonnull JsonObject json)
        {
            final FluidTagInput fluidTagInput = FluidTagInput.deserialize(json);
            return new AddonIngredientFluidStack(null, fluidTagInput);
        }

        @Override
        public void write(@Nonnull FriendlyByteBuf buffer, AddonIngredientFluidStack ingredient)
        {
            Helpers.encodeNullable(ingredient.delegate, buffer, Ingredient::toNetwork);
            ingredient.fluidTagInput.write(buffer);
        }
    }
}
