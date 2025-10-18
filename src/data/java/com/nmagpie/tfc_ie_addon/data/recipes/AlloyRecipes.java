package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.List;

import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.core.registries.BuiltInRegistries;

import net.dries007.tfc.common.recipes.AlloyRecipe;
import net.dries007.tfc.util.AlloyRange;
import net.dries007.tfc.util.Metal;

public interface AlloyRecipes extends Recipes
{
    default void alloyRecipes()
    {
        alloy(IEMetal.ELECTRUM,
            rangeOf(Metal.GOLD, 0.4, 0.6),
            rangeOf(Metal.SILVER, 0.4, 0.6));
        alloy(IEMetal.CONSTANTAN,
            rangeOf(Metal.COPPER, 0.4, 0.6),
            rangeOf(Metal.NICKEL, 0.4, 0.6));
    }

    private AlloyRange rangeOf(Metal metal, double min, double max)
    {
        return new AlloyRange(fluidOf(metal), min, max);
    }

    private void alloy(IEMetal metal, AlloyRange... ranges)
    {
        final AlloyRecipe recipe = new AlloyRecipe(List.of(ranges), fluidOf(metal));
        add(BuiltInRegistries.FLUID.getKey(recipe.result()).getPath().split("/")[1], recipe);
    }
}
