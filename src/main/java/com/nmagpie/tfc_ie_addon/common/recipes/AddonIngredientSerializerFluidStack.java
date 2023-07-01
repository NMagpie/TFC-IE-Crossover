package com.nmagpie.tfc_ie_addon.common.recipes;

import blusunrize.immersiveengineering.api.crafting.FluidTagInput;
import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import static com.nmagpie.tfc_ie_addon.TFC_IE_Addon.*;

public class AddonIngredientSerializerFluidStack implements IIngredientSerializer<AddonIngredientFluidStack>
{
    public static ResourceLocation NAME = new ResourceLocation(MOD_ID, "fluid");
    public static IIngredientSerializer<AddonIngredientFluidStack> INSTANCE = new AddonIngredientSerializerFluidStack();

    @Nonnull
    @Override
    public AddonIngredientFluidStack parse(@Nonnull FriendlyByteBuf buffer)
    {
        return new AddonIngredientFluidStack(FluidTagInput.read(buffer));
    }

    @Nonnull
    @Override
    public AddonIngredientFluidStack parse(@Nonnull JsonObject json)
    {
        return new AddonIngredientFluidStack(FluidTagInput.deserialize(json));
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf buffer, @Nonnull AddonIngredientFluidStack ingredient)
    {
        ingredient.getFluidTagInput().write(buffer);
    }
}
