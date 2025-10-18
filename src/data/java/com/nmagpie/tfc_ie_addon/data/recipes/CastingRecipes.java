package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.data.Utils;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.CastingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;

public interface CastingRecipes extends Recipes
{
    default void castingRecipes()
    {
        for (var entry : Utils.IE_METAL_MAP.entrySet())
        {
            casting(entry.getKey().name() + "_ingot", TFCItems.MOLDS.get(Metal.ItemType.INGOT), entry.getKey(), IEItems.Metals.INGOTS.get(entry.getValue()), 100, 0.1f);
            casting(entry.getKey().name() + "_fire_ingot", TFCItems.FIRE_INGOT_MOLD, entry.getKey(), IEItems.Metals.INGOTS.get(entry.getValue()), 100, 0.01f);
        }
    }

    private void casting(String name, ItemLike item, IEMetal metal, ItemLike result, int units, float chance)
    {
        add(name, new CastingRecipe(
            Ingredient.of(item),
            SizedFluidIngredient.of(fluidOf(metal), units),
            ItemStackProvider.of(result),
            chance
        ));
    }
}
