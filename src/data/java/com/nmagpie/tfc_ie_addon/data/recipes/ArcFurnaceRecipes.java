package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.Locale;
import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.data.recipes.builder.ArcFurnaceRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.data.Utils;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

public interface ArcFurnaceRecipes extends Recipes
{
    default void arcFurnaceRecipes(RecipeOutput output)
    {
        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.BISMUTH_BRONZE).get(Metal.ItemType.INGOT), 4)
            .input(ingotTagOf(Metal.COPPER), 2)
            .additive(ingotTagOf(Metal.ZINC))
            .additive(ingotTagOf(Metal.BISMUTH))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toRL("bismuth_bronze"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.BLACK_BRONZE).get(Metal.ItemType.INGOT), 4)
            .input(ingotTagOf(Metal.COPPER), 2)
            .additive(ingotTagOf(Metal.GOLD))
            .additive(ingotTagOf(Metal.SILVER))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toRL("black_bronze"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.STERLING_SILVER).get(Metal.ItemType.INGOT), 5)
            .input(ingotTagOf(Metal.SILVER), 3)
            .additive(new IngredientWithSize(ingotTagOf(Metal.COPPER), 2))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toRL("sterling_silver"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.WEAK_BLUE_STEEL).get(Metal.ItemType.INGOT), 9)
            .input(ingotTagOf(Metal.BLACK_STEEL), 5)
            .additive(new IngredientWithSize(ingotTagOf(Metal.STEEL), 2))
            .additive(ingotTagOf(Metal.BISMUTH_BRONZE))
            .additive(ingotTagOf(Metal.STERLING_SILVER))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toRL("weak_blue_steel"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.WEAK_STEEL).get(Metal.ItemType.INGOT), 4)
            .input(ingotTagOf(Metal.BLACK_STEEL), 2)
            .additive(ingotTagOf(Metal.BLACK_BRONZE))
            .additive(ingotTagOf(Metal.NICKEL))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toRL("weak_steel"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.STEEL).get(Metal.ItemType.INGOT))
            .input(ingotTagOf(Metal.WROUGHT_IRON))
            .additive(IETags.coalCokeDust)
            .slag(IETags.slag, 1)
            .setTime(400)
            .setEnergy(204800)
            .build(output, toRL("steel"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.INGOT))
            .input(ingotTagOf(Metal.CAST_IRON))
            .additive(IETags.coalCokeDust)
            .slag(IETags.slag, 1)
            .setTime(400)
            .setEnergy(204800)
            .build(output, toRL("wrought_iron"));

        for (Ore ore : Ore.values())
        {
            if (ore.isGraded())
            {
                ArcFurnaceRecipeBuilder.builder()
                    .output(TFCItems.METAL_ITEMS.get(ore.metal()).get(Metal.ItemType.INGOT))
                    .input(TFCItems.ORE_POWDERS.get(ore), 20)
                    .setTime(100)
                    .setEnergy(25600)
                    .build(output, toRL(ore.name().toLowerCase(Locale.ROOT)));
            }
        }

        for (IEOre ore : IEOre.values())
        {
            ArcFurnaceRecipeBuilder.builder()
                .output(IEItems.Metals.INGOTS.get(Utils.IE_METAL_MAP.get(ore.metal())))
                .input(Items.POWDERS.get(ore), 20)
                .setTime(100)
                .setEnergy(25600)
                .build(output, toRL(ore.name().toLowerCase(Locale.ROOT)));
        }

        // IE namespace

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.BRASS).get(Metal.ItemType.INGOT), 10)
            .input(ingotTagOf(Metal.COPPER), 9)
            .additive(ingotTagOf(Metal.ZINC))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toIERL("alloy_brass"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.BRONZE).get(Metal.ItemType.INGOT), 10)
            .input(ingotTagOf(Metal.COPPER), 9)
            .additive(ingotTagOf(Metal.TIN))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toIERL("alloy_bronze"));

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.ROSE_GOLD).get(Metal.ItemType.INGOT), 4)
            .input(ingotTagOf(Metal.GOLD), 3)
            .additive(ingotTagOf(Metal.COPPER))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toIERL("alloy_rose_gold"));

        final Metal[] dustMetals = {Metal.COPPER, Metal.GOLD, Metal.NICKEL, Metal.SILVER, Metal.STEEL};
        for (Metal metal : dustMetals)
        {
            ArcFurnaceRecipeBuilder.builder()
                .output(TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.INGOT))
                .input(TagUtils.createItemWrapper(IETags.getDust(metal.getSerializedName())))
                .setTime(100)
                .setEnergy(51200)
                .build(output, toIERL("dust_" + metal.getSerializedName()));
        }

        ArcFurnaceRecipeBuilder.builder()
            .output(TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.INGOT))
            .input(TagUtils.createItemWrapper(IETags.getDust("iron")))
            .setTime(100)
            .setEnergy(51200)
            .build(output, toIERL("dust_iron" ));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("arcfurnace/" + name);
    }

    private ResourceLocation toIERL(String name)
    {
        return ImmersiveEngineering.rl("arcfurnace/" + name);
    }
}
