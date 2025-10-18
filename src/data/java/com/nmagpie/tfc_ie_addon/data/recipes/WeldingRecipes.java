package com.nmagpie.tfc_ie_addon.data.recipes;

import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

public interface WeldingRecipes extends Recipes
{
    default void weldingRecipes()
    {
        for (IEMetal metal : IEMetal.values())
            weldDoubleIngot(metal);
    }

    private void weldDoubleIngot(IEMetal metal)
    {
        add(new WeldingRecipe(
            Ingredient.of(ingotTagOf(metal)),
            Ingredient.of(ingotTagOf(metal)),
            metal.tier().level() - 1,
            ItemStackProvider.of(Items.METAL_ITEMS.get(metal).get(IEMetal.ItemType.DOUBLE_INGOT)),
            WeldingRecipe.Behavior.IGNORE
        ));
    }
}
