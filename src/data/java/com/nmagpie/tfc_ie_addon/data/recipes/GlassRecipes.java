package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.List;
import com.nmagpie.tfc_ie_addon.util.ModGlassOperation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.recipes.GlassworkingRecipe;
import net.dries007.tfc.util.Helpers;

public interface GlassRecipes extends Recipes
{
    default void glassRecipes()
    {
        add2(Ingredient.of(TFCTags.Items.GLASS_BATCHES_T2), List.of(ModGlassOperation.URANIUM.value()), TFCBlocks.COLORED_POURED_GLASS.get(DyeColor.LIME), Items.LIME_STAINED_GLASS);
        add2(Ingredient.of(TFCTags.Items.GLASS_BATCHES_T2), List.of(ModGlassOperation.LEAD.value()), TFCBlocks.COLORED_POURED_GLASS.get(DyeColor.YELLOW), Items.YELLOW_STAINED_GLASS);
    }

    private void add2(Ingredient input, List<GlassOperation> steps, ItemLike poured, ItemLike stained)
    {
        add(input, Helpers.immutableAdd(steps, GlassOperation.TABLE_POUR.value()), poured);
        add(input, Helpers.immutableAdd(steps, GlassOperation.BASIN_POUR.value()), stained);
    }

    private void add(Ingredient input, List<GlassOperation> steps, ItemLike output)
    {
        add(nameOf(output), new GlassworkingRecipe(steps, input, new ItemStack(output)));
    }
}
