package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEFluids;
import blusunrize.immersiveengineering.data.recipes.builder.MixerRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;

public interface MixerRecipes extends Recipes
{
    default void mixerRecipes(RecipeOutput output)
    {
        MixerRecipeBuilder.builder()
            .output(TFCFluids.SALT_WATER.getSource(), 125)
            .fluidInput(TFCTags.Fluids.FRESH_WATER, 125)
            .input(TFCItems.POWDERS.get(Powder.SALT))
            .setEnergy(800)
            .build(output, toRL("salt_water"));
        MixerRecipeBuilder.builder()
            .output(IEFluids.HERBICIDE.getStill(), 500)
            .fluidInput(IETags.fluidEthanol, 500)
            .input(IETags.sulfurDust)
            .input(CompoundIngredient.of(
                Ingredient.of(TFCItems.ORE_POWDERS.get(Ore.NATIVE_COPPER)),
                Ingredient.of(TFCItems.ORE_POWDERS.get(Ore.MALACHITE)),
                Ingredient.of(TFCItems.ORE_POWDERS.get(Ore.TETRAHEDRITE))))
            .setEnergy(3200)
            .build(output, toRL("herbicide"));
        MixerRecipeBuilder.builder()
            .output(fluidOf(SimpleFluid.LIMEWATER), 500)
            .fluidInput(TFCTags.Fluids.FRESH_WATER, 500)
            .input(CompoundIngredient.of(
                Ingredient.of(TFCItems.POWDERS.get(Powder.FLUX)),
                Ingredient.of(TFCItems.POWDERS.get(Powder.LIME))))
            .setEnergy(800)
            .build(output, toRL("limewater"));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("mixer/" + name);
    }
}
