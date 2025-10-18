package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.Set;
import blusunrize.immersiveengineering.common.register.IEFluids;
import blusunrize.immersiveengineering.data.recipes.builder.FermenterRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.items.TFCItems;

public interface SqueezerRecipes extends Recipes
{
    default void squeezerRecipes(RecipeOutput output)
    {
        final Fluid plantoil = IEFluids.PLANTOIL.getStill();
        final Set<Crop> blacklist = Set.of(Crop.GREEN_BEAN, Crop.LENTIL, Crop.GARLIC, Crop.POTATO, Crop.SUGARCANE, Crop.PAPYRUS);

        for (Crop crop : Crop.values())
        {
            if (!blacklist.contains(crop))
            {
                FermenterRecipeBuilder.builder()
                    .output(plantoil, 80)
                    .input(TFCItems.CROP_SEEDS.get(crop))
                    .setEnergy(6400)
                    .build(output, toRL(crop.getSerializedName()));
            }
        }
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("squeezer/" + name);
    }
}
