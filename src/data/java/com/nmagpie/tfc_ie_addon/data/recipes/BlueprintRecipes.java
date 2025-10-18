package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.data.recipes.builder.BlueprintCraftingRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

public interface BlueprintRecipes extends Recipes
{
    default void blueprintRecipes(RecipeOutput output)
    {
        BlueprintCraftingRecipeBuilder.builder()
            .category("molds")
            .output(Items.MOLD_BLOCK)
            .input(IETags.getTagsFor(EnumMetals.STEEL).plate, 3)
            .input(IEItems.Tools.WIRECUTTER)
            .build(output, toRL("mold_block"));

        BlueprintCraftingRecipeBuilder.builder()
            .category("molds")
            .output(Items.MOLD_SHEET)
            .input(IETags.getTagsFor(EnumMetals.STEEL).plate, 3)
            .input(IEItems.Tools.WIRECUTTER)
            .build(output, toRL("mold_sheet"));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("blueprint/" + name);
    }
}
