package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.Locale;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

public interface CrusherRecipes extends Recipes
{
    default void crusherRecipes(RecipeOutput output)
    {
        // Ores
        for (Ore ore : Ore.values())
        {
            if (ore.isGraded())
            {
                CrusherRecipeBuilder.builder()
                    .output(TFCItems.ORE_POWDERS.get(ore), 4)
                    .input(TFCBlocks.SMALL_ORES.get(ore))
                    .setEnergy(1000)
                    .build(output, toRL("ore/" + ore.name().toLowerCase(Locale.ROOT)));
                for (Ore.Grade grade : Ore.Grade.values())
                {
                    CrusherRecipeBuilder.builder()
                        .output(TFCItems.ORE_POWDERS.get(ore), grade == Ore.Grade.POOR ? 6 : grade == Ore.Grade.NORMAL ? 10 : 14)
                        .input(TFCItems.GRADED_ORES.get(ore).get(grade))
                        .setEnergy(grade == Ore.Grade.POOR ? 1500 : grade == Ore.Grade.NORMAL ? 2500 : 3500)
                        .build(output, toRL("ore/" + (grade.name() + "_" + ore.name()).toLowerCase(Locale.ROOT)));
                }
            }
            else if (ore.hasPowder())
            {
                CrusherRecipeBuilder.builder()
                    .output(TFCItems.ORE_POWDERS.get(ore), 4)
                    .input(ore.isGem() ? Ingredient.of(TFCItems.ORES.get(ore), TFCItems.GEMS.get(ore)) : Ingredient.of(TFCItems.ORES.get(ore)))
                    .setEnergy(1600)
                    .build(output, toRL(ore.name().toLowerCase(Locale.ROOT)));
            }
        }
        for (IEOre ore : IEOre.values())
        {
            CrusherRecipeBuilder.builder()
                .output(Items.POWDERS.get(ore), 4)
                .input(Blocks.SMALL_ORES.get(ore))
                .setEnergy(1000)
                .build(output, toRL("ore/" + ore.name().toLowerCase(Locale.ROOT)));
            for (Ore.Grade grade : Ore.Grade.values())
            {
                CrusherRecipeBuilder.builder()
                    .output(Items.POWDERS.get(ore), grade == Ore.Grade.POOR ? 6 : grade == Ore.Grade.NORMAL ? 10 : 14)
                    .input(Items.ORES.get(ore).get(grade))
                    .setEnergy(grade == Ore.Grade.POOR ? 1500 : grade == Ore.Grade.NORMAL ? 2500 : 3500)
                    .build(output, toRL("ore/" + (grade.name() + "_" + ore.name()).toLowerCase(Locale.ROOT)));
            }
        }

        CrusherRecipeBuilder.builder()
            .output(net.minecraft.world.item.Items.REDSTONE, 8)
            .input(TFCItems.ORES.get(Ore.CINNABAR))
            .setEnergy(1600)
            .build(output, toRL("cinnabar"));
        CrusherRecipeBuilder.builder()
            .output(net.minecraft.world.item.Items.REDSTONE, 8)
            .input(TFCItems.ORES.get(Ore.CRYOLITE))
            .setEnergy(1600)
            .build(output, toRL("cryolite"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.FLUX), 6)
            .input(TFCItems.ORES.get(Ore.BORAX))
            .setEnergy(1600)
            .build(output, toRL("borax"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.SALT), 4)
            .input(TFCItems.ORES.get(Ore.HALITE))
            .setEnergy(1600)
            .build(output, toRL("halite"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.CHARCOAL), 4)
            .input(net.minecraft.world.item.Items.CHARCOAL)
            .setEnergy(1600)
            .build(output, toRL("charcoal"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.FLUX), 2)
            .input(TFCTags.Items.FLUXSTONE)
            .setEnergy(1600)
            .build(output, toRL("fluxstone"));
        CrusherRecipeBuilder.builder()
            .output(TagUtils.createItemWrapper(IETags.getDust("iron")))
            .input(commonTagOf(Metal.WROUGHT_IRON, Metal.ItemType.INGOT))
            .setEnergy(3000)
            .build(output, toRL("wrought_iron_ingot"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.OLIVE_PASTE, 2)
            .input(TFCItems.FOOD.get(Food.OLIVE))
            .setEnergy(800)
            .build(output, toRL("olive"));

        // Sandstone
        for (SandBlockType sand : SandBlockType.values())
        {
            for (SandstoneBlockType sandstone : SandstoneBlockType.values())
            {
                CrusherRecipeBuilder.builder()
                    .output(TFCBlocks.SAND.get(sand), 2)
                    .addSecondary(TFCItems.ORE_POWDERS.get(Ore.SALTPETER).asItem(), 0.5f)
                    .input(TFCBlocks.SANDSTONE.get(sand).get(sandstone))
                    .setEnergy(3200)
                    .build(output, toRL("sandstone/" + (sandstone.name() + sand.name()).toLowerCase(Locale.ROOT)));
            }
        }
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("crusher/" + name);
    }
}
