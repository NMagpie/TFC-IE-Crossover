package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.data.recipes.builder.SawmillRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.common.blocks.wood.Wood;

public interface SawmillRecipes extends Recipes
{
    default void sawmillRecipes(RecipeOutput output)
    {
        for (Wood wood : Wood.VALUES)
        {
            SawmillRecipeBuilder.builder()
                .output(lumberOf(wood), 4)
                .input(woodOf(wood, Wood.BlockType.PLANKS))
                .setEnergy(1600)
                .build(output, toRL(wood.getSerializedName() + "/planks"));
            SawmillRecipeBuilder.builder()
                .output(lumberOf(wood), 12)
                .input(woodOf(wood, Wood.BlockType.LOG))
                .addSawSecondary(IETags.sawdust)
                .addStripped(woodOf(wood, Wood.BlockType.STRIPPED_LOG))
                .addStripSecondary(IETags.sawdust)
                .setEnergy(1600)
                .build(output, toRL(wood.getSerializedName() + "/log"));
            SawmillRecipeBuilder.builder()
                .output(lumberOf(wood), 12)
                .input(woodOf(wood, Wood.BlockType.WOOD))
                .addSawSecondary(IETags.sawdust)
                .addStripped(woodOf(wood, Wood.BlockType.STRIPPED_WOOD))
                .addStripSecondary(IETags.sawdust)
                .setEnergy(1600)
                .build(output, toRL(wood.getSerializedName() + "/wood"));
            SawmillRecipeBuilder.builder()
                .output(lumberOf(wood), 12)
                .input(Ingredient.of(woodOf(wood, Wood.BlockType.STRIPPED_LOG), woodOf(wood, Wood.BlockType.STRIPPED_WOOD)))
                .addSawSecondary(IETags.sawdust)
                .setEnergy(1600)
                .build(output, toRL(wood.getSerializedName() + "/stripped_log"));
        }
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("sawmill/" + name);
    }
}
