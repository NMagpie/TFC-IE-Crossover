package com.nmagpie.tfc_ie_addon.compat.patchouli.component;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;

@SuppressWarnings("unused")
public class TriAnvilComponent extends TriInputOutputComponent<AnvilRecipe>
{
    @Override
    protected RecipeType<AnvilRecipe> getRecipeType()
    {
        return TFCRecipeTypes.ANVIL.get();
    }

    @Override
    public Ingredient getIngredient(AnvilRecipe recipe)
    {
        return recipe.getInput();
    }

    @Override
    public ItemStack getOutput(AnvilRecipe recipe)
    {
        return recipe.getResultItem();
    }
}
