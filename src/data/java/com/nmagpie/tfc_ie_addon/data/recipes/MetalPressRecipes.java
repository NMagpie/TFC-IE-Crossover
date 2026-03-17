package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.Arrays;
import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.data.recipes.builder.MetalPressRecipeBuilder;
import com.eerussianguy.firmalife.common.items.FLItems;
import com.eerussianguy.firmalife.common.util.FLMetal;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

public interface MetalPressRecipes extends Recipes
{
    default void metalPressRecipes(RecipeOutput output)
    {
        final Metal[] RODS = {Metal.BISMUTH, Metal.BISMUTH_BRONZE, Metal.BLACK_BRONZE, Metal.CAST_IRON, Metal.BLACK_STEEL, Metal.BLUE_STEEL, Metal.RED_STEEL, Metal.STERLING_SILVER};

        for (Metal metal : Metal.values())
        {
            if (metal.defaultParts())
            {
                MetalPressRecipeBuilder.builder()
                    .output(TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.SHEET))
                    .input(ingotTagOf(metal), 2)
                    .mold(Items.MOLD_SHEET)
                    .setEnergy(2400)
                    .build(output, toRL("sheet_" + metal.getSerializedName()));
            }
            if (Arrays.asList(RODS).contains(metal))
            {
                MetalPressRecipeBuilder.builder()
                    .output(TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.ROD), 2)
                    .input(ingotTagOf(metal))
                    .mold(IEItems.Molds.MOLD_ROD)
                    .setEnergy(2400)
                    .build(output, toRL("rod_" + metal.getSerializedName()));
            }
        }
        for (IEMetal metal : IEMetal.values())
        {
            MetalPressRecipeBuilder.builder()
                .output(Items.METAL_ITEMS.get(metal).get(IEMetal.ItemType.SHEET))
                .input(ingotTagOf(metal), 2)
                .mold(Items.MOLD_SHEET)
                .setEnergy(2400)
                .build(output, toRL("sheet_" + metal.getSerializedName()));
        }
        MetalPressRecipeBuilder.builder()
            .output(IEBlocks.Metals.STORAGE.get(EnumMetals.STEEL))
            .input(ingotTagOf(Metal.STEEL), 9)
            .mold(Items.MOLD_BLOCK)
            .setEnergy(2400)
            .build(output, toRL("block_steel"));
        MetalPressRecipeBuilder.builder()
            .output(IEBlocks.Metals.STORAGE.get(EnumMetals.URANIUM))
            .input(ingotTagOf(IEMetal.URANIUM), 9)
            .mold(Items.MOLD_BLOCK)
            .setEnergy(2400)
            .build(output, toRL("block_uranium"));
        MetalPressRecipeBuilder.builder()
            .output(IEItems.Metals.PLATES.get(EnumMetals.IRON))
            .input(ingotTagOf(Metal.WROUGHT_IRON))
            .mold(IEItems.Molds.MOLD_PLATE)
            .setEnergy(2400)
            .build(output, toRL("plate_wrought_iron"));

        // Firmalife

        for (FLMetal metal : FLMetal.values())
        {
            MetalPressRecipeBuilder.builder()
                .output(FLItems.METAL_ITEMS.get(metal).get(FLMetal.ItemType.SHEET))
                .input(ingotTagOf(metal), 2)
                .mold(Items.MOLD_SHEET)
                .setEnergy(2400)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, toRL("sheet_" + metal.getSerializedName()));
            MetalPressRecipeBuilder.builder()
                .output(FLItems.METAL_ITEMS.get(metal).get(FLMetal.ItemType.ROD), 2)
                .input(ingotTagOf(metal))
                .mold(IEItems.Molds.MOLD_ROD)
                .setEnergy(2400)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, toRL("rod_" + metal.getSerializedName()));

        }
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("metalpress/" + name);
    }
}
