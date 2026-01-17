package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;

public interface RefineryRecipes extends Recipes
{
    default void refineryRecipes(RecipeOutput output)
    {
        RefineryRecipeBuilder.builder()
            .output(fluidOf(SimpleFluid.BRINE), 10)
            .input(SizedFluidIngredient.of(fluidOf(SimpleFluid.VINEGAR), 1))
            .input(SizedFluidIngredient.of(TFCFluids.SALT_WATER.getSource(), 9))
            .setEnergy(50)
            .build(output, toRL("brine"));
        RefineryRecipeBuilder.builder()
            .output(fluidOf(SimpleFluid.MILK_VINEGAR), 10)
            .input(SizedFluidIngredient.of(fluidOf(SimpleFluid.VINEGAR), 1))
            .input(SizedFluidIngredient.of(Tags.Fluids.MILK, 9))
            .setEnergy(50)
            .addCondition(new NotCondition(new ModLoadedCondition("firmalife")))
            .build(output, toRL("milk_vinegar"));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("refinery/" + name);
    }
}
