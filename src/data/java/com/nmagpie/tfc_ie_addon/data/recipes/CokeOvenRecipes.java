package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.data.recipes.builder.CokeOvenRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;

public interface CokeOvenRecipes extends Recipes
{
    default void cokeOvenRecipes(RecipeOutput output)
    {
        CokeOvenRecipeBuilder.builder()
            .output(IETags.coalCoke)
            .input(TFCItems.ORES.get(Ore.BITUMINOUS_COAL), 16)
            .creosoteAmount(FluidType.BUCKET_VOLUME / 2)
            .setTime(6000)
            .build(output, toRL("bituminous_coal"));
        CokeOvenRecipeBuilder.builder()
            .output(IETags.coalCoke)
            .input(TFCItems.ORES.get(Ore.LIGNITE), 16)
            .creosoteAmount(FluidType.BUCKET_VOLUME / 2)
            .setTime(6000)
            .build(output, toRL("lignite"));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("cokeoven/" + name);
    }
}
