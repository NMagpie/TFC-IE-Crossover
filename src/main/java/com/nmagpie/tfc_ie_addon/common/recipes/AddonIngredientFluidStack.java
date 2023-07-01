package com.nmagpie.tfc_ie_addon.common.recipes;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.api.crafting.FluidTagInput;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;

public class AddonIngredientFluidStack extends Ingredient
{
    private final FluidTagInput fluidTagInput;
    ItemStack[] cachedStacks;

    public AddonIngredientFluidStack(FluidTagInput fluidTagInput)
    {
        super(Stream.empty());
        this.fluidTagInput = fluidTagInput;
    }

    public FluidTagInput getFluidTagInput()
    {
        return fluidTagInput;
    }

    @Nonnull
    @Override
    public ItemStack[] getItems()
    {
        if (cachedStacks == null)
            cachedStacks = this.fluidTagInput.getMatchingFluidStacks()
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
        return this.cachedStacks;
    }

    @Override
    public boolean test(@Nullable ItemStack stack)
    {
        if (stack == null)
            return false;
        Optional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack).resolve();
        return handler.isPresent() && fluidTagInput.extractFrom(handler.get(), FluidAction.SIMULATE);
    }

    @Nonnull
    @Override
    public JsonElement toJson()
    {
        JsonObject ret = (JsonObject) this.fluidTagInput.serialize();
        ret.addProperty("type", AddonIngredientSerializerFluidStack.NAME.toString());
        return ret;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean isSimple()
    {
        return false;
    }

    @Nonnull
    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return AddonIngredientSerializerFluidStack.INSTANCE;
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
        return input.getContainerItem();
    }
}
