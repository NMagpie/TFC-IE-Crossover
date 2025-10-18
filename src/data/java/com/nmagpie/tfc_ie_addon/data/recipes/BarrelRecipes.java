package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.common.register.IEFluids;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.recipes.BarrelRecipe;

public interface BarrelRecipes extends Recipes
{
    default void barrelRecipes()
    {
        barrel()
            .input(Tags.Items.DUSTS_REDSTONE)
            .input(Fluids.WATER, 250)
            .output(IEFluids.REDSTONE_ACID.getStill(), 250)
            .instant();
        barrel()
            .input(TFCTags.Items.LUMBER)
            .input(IEFluids.CREOSOTE.getStill(), 50)
            .output(Items.TREATED_WOOD_LUMBER)
            .sealed(hours(8));
    }

    private BarrelRecipe.Builder barrel()
    {
        return new BarrelRecipe.Builder(r -> {
            if (!r.getResultItem().isEmpty()) add("barrel", nameOf(r.getResultItem().getItem()), r);
            else if (!r.getOutputFluid().isEmpty()) add("barrel", nameOf(r.getOutputFluid().getFluid()), r);
            else throw new IllegalStateException("Barrel recipe requires a custom name!");
        });
    }
}
