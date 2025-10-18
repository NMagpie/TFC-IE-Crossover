package com.nmagpie.tfc_ie_addon.data.recipes;

import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.recipes.QuernRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

public interface QuernRecipes extends Recipes
{
    default void quernRecipes()
    {
        Items.ORES.forEach((ore, items) -> {
            add("small", Blocks.SMALL_ORES.get(ore), Items.POWDERS.get(ore), 2);
            add("poor", items.get(Ore.Grade.POOR), Items.POWDERS.get(ore), 3);
            add("normal", items.get(Ore.Grade.NORMAL), Items.POWDERS.get(ore), 5);
            add("rich", items.get(Ore.Grade.RICH), Items.POWDERS.get(ore), 7);
        });
    }

    private void add(String suffix, ItemLike input, ItemLike output, int count)
    {
        add(suffix, Ingredient.of(input), output, count);
    }

    private void add(String suffix, Ingredient input, ItemLike output, int count)
    {
        add(nameOf(output) + "_" + suffix, new QuernRecipe(input, ItemStackProvider.of(output, count)));
    }
}
