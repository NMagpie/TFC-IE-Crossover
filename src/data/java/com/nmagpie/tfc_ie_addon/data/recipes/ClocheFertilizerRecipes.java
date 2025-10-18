package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.ClocheFertilizerBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import net.dries007.tfc.common.blocks.GroundcoverBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;

public interface ClocheFertilizerRecipes extends Recipes
{
    default void clocheFertilizerRecipes(RecipeOutput output)
    {
        ClocheFertilizerBuilder.builder(1.20f)
            .input(TFCItems.ORE_POWDERS.get(Ore.SYLVITE))
            .build(output, toRL("sylvite"));
        ClocheFertilizerBuilder.builder(1.10f)
            .input(TFCItems.POWDERS.get(Powder.WOOD_ASH))
            .build(output, toRL("wood_ash"));
        ClocheFertilizerBuilder.builder(1.30f)
            .input(TFCBlocks.GROUNDCOVER.get(GroundcoverBlockType.GUANO))
            .build(output, toRL("guano"));
        ClocheFertilizerBuilder.builder(1.10f)
            .input(TFCItems.FOOD.get(Food.SHELLFISH))
            .build(output, toRL("shellfish"));
        ClocheFertilizerBuilder.builder(1.20f)
            .input(TFCItems.COMPOST)
            .build(output, toRL("compost"));
        ClocheFertilizerBuilder.builder(1.20f)
            .input(TFCItems.PURE_NITROGEN)
            .build(output, toRL("pure_nitrogen"));
        ClocheFertilizerBuilder.builder(1.20f)
            .input(TFCItems.PURE_PHOSPHORUS)
            .build(output, toRL("pure_phosphorus"));
        ClocheFertilizerBuilder.builder(1.20f)
            .input(TFCItems.PURE_POTASSIUM)
            .build(output, toRL("pure_potassium"));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("fertilizer/" + name);
    }
}
