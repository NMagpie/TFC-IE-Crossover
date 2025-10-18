package com.nmagpie.tfc_ie_addon.util;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import net.dries007.tfc.common.recipes.INoopInputRecipe;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;

public class EmptyRecipe implements INoopInputRecipe
{
    public static final EmptyRecipe INSTANCE = new EmptyRecipe();
    public static final ResourceLocation ID = TFC_IE_Addon.identifier("empty");
    public static final RecipeType<EmptyRecipe> TYPE = RecipeType.simple(ID);
    public static final RecipeSerializer<EmptyRecipe> SERIALIZER = new RecipeSerializerImpl<>(new EmptyRecipe());

    @Override
    public @NotNull RecipeType<?> getType()
    {
        return TYPE;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }
}
