package com.nmagpie.tfc_ie_addon.common.recipes;

import blusunrize.immersiveengineering.common.crafting.serializers.WrappingRecipeSerializer;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, TFC_IE_Addon.MOD_ID);

    public static final RegistryObject<WrappingRecipeSerializer<FluidAwareShapedRecipe, ?>> SHAPED_SERIALIZER = RECIPE_SERIALIZERS.register(
            "shaped_fluid", () -> new WrappingRecipeSerializer<>(
                    RecipeSerializer.SHAPED_RECIPE, FluidAwareShapedRecipe::toVanilla, FluidAwareShapedRecipe::new
            )
    );
    public static final RegistryObject<WrappingRecipeSerializer<FluidAwareShapelessRecipe, ?>> SHAPELESS_SERIALIZER = RECIPE_SERIALIZERS.register(
            "shapeless_fluid", () -> new WrappingRecipeSerializer<>(
                    RecipeSerializer.SHAPELESS_RECIPE, FluidAwareShapelessRecipe::toVanilla, FluidAwareShapelessRecipe::new
            )
    );
}
