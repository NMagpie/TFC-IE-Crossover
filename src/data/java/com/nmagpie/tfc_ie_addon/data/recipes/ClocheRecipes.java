package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction;
import blusunrize.immersiveengineering.client.utils.ClocheRenderFunctions;
import blusunrize.immersiveengineering.data.recipes.builder.ClocheRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.data.Utils;
import com.nmagpie.tfc_ie_addon.util.ModClocheRenderFunctions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;

public interface ClocheRecipes extends Recipes
{
    default void clocheRecipes(RecipeOutput output)
    {
        for (var entry : Utils.CROP_MAP.entrySet())
        {
            Crop crop = entry.getKey();
            builderTFCSoil()
                .output(entry.getValue(), crop == Crop.RED_BELL_PEPPER || crop == Crop.YELLOW_BELL_PEPPER ? 4 : crop == Crop.PUMPKIN || crop == Crop.MELON ? 1 : 6)
                .output(TFCItems.CROP_SEEDS.get(crop), 0.25f)
                .seed(TFCItems.CROP_SEEDS.get(crop))
                .setTime(128000)
                .setRender(getRenderFunction(crop))
                .build(output, toRL(crop.getSerializedName()));
        }
    }

    default ClocheRecipeBuilder builderTFCSoil()
    {
        return ClocheRecipeBuilder.builder()
            .soil(CompoundIngredient.of(
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.ENTISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.ARIDISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.OXISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.FLUVISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.ANDISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.PODZOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.ALFISOL)),
                Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(SoilBlockType.Variant.MOLLISOL))
            ));
    }

    private ClocheRenderFunction getRenderFunction(Crop crop)
    {
        final Block block = BuiltInRegistries.BLOCK.get(Helpers.identifier("crop/" + crop.getSerializedName()));
        if (block instanceof DoubleCropBlock doubleCropBlock)
            return new ModClocheRenderFunctions.RenderFunctionDoubleCropTFC(block, (doubleCropBlock.getMaxAge() + 1) / 2);
        else return new ClocheRenderFunctions.RenderFunctionCrop(block);
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("cloche/" + name);
    }
}
