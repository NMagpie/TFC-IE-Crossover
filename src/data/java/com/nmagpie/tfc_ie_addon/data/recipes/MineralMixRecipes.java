package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.data.recipes.builder.MineralMixBuilder;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.items.TFCItems;

public interface MineralMixRecipes extends Recipes
{
    default void mineralMixRecipes(RecipeOutput output)
    {
        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.TETRAHEDRITE), 0.7f)
            .ore(TFCItems.GRADED_ORES.get(Ore.TETRAHEDRITE).get(Ore.Grade.POOR), 0.5f)
            .ore(TFCItems.GRADED_ORES.get(Ore.TETRAHEDRITE).get(Ore.Grade.NORMAL), 0.3f)
            .ore(TFCItems.GRADED_ORES.get(Ore.TETRAHEDRITE).get(Ore.Grade.RICH), 0.2f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.LIMONITE), 0.35f)
            .ore(TFCItems.GRADED_ORES.get(Ore.LIMONITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.LIMONITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.LIMONITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.CASSITERITE), 0.7f)
            .ore(TFCItems.GRADED_ORES.get(Ore.CASSITERITE).get(Ore.Grade.POOR), 0.5f)
            .ore(TFCItems.GRADED_ORES.get(Ore.CASSITERITE).get(Ore.Grade.NORMAL), 0.3f)
            .ore(TFCItems.GRADED_ORES.get(Ore.CASSITERITE).get(Ore.Grade.RICH), 0.2f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.MARBLE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.MARBLE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.ANDESITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.ANDESITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(20)
            .failchance(0.1f)
            .build(output, toRL("aikinite"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.HEMATITE), 0.6f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.POOR), 0.4f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.SPHALERITE), 0.35f)
            .ore(TFCItems.GRADED_ORES.get(Ore.SPHALERITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.SPHALERITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.SPHALERITE).get(Ore.Grade.RICH), 0.1f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CONGLOMERATE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CONGLOMERATE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SHALE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SHALE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(30)
            .failchance(0.05f)
            .build(output, toRL("franklinite"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(Blocks.QUARTZ_BLOCK, 0.3f)
            .ore(Items.QUARTZ_SHARD, 0.5f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.QUARTZITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.QUARTZITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(20)
            .failchance(0.15f)
            .build(output, toRL("quartzite"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(Blocks.SMALL_ORES.get(IEOre.GALENA), 0.6f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.POOR), 0.4f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.NORMAL), 0.2f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.MALACHITE), 0.35f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MALACHITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MALACHITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MALACHITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.BISMUTHINITE), 0.65f)
            .ore(TFCItems.GRADED_ORES.get(Ore.BISMUTHINITE).get(Ore.Grade.POOR), 0.4f)
            .ore(TFCItems.GRADED_ORES.get(Ore.BISMUTHINITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.BISMUTHINITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CLAYSTONE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CLAYSTONE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DOLOMITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DOLOMITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(20)
            .failchance(0.1f)
            .build(output, toRL("stannite"));

        // IE namespace

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER), 0.75f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.POOR), 0.6f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.NORMAL), 0.4f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH), 0.2f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_GOLD), 0.3f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_GOLD).get(Ore.Grade.RICH), 0.15f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GRANITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GRANITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(30)
            .failchance(0.1f)
            .build(output, toIERL("auricupride"));

        final TagKey<Item> phosphorus = TagUtils.createItemWrapper(IETags.getDust("phosphorus"));
        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCItems.ORES.get(Ore.BITUMINOUS_COAL), 0.8f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.2f)
            .ore(phosphorus, 0.2f, getTagCondition(phosphorus))
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.BASALT).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.BASALT).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHALK).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHALK).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(25)
            .failchance(0.05f)
            .build(output, toIERL("bituminous_coal"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER), 0.75f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.POOR), 0.6f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.NORMAL), 0.4f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_COPPER).get(Ore.Grade.RICH), 0.2f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.HEMATITE), 0.5f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.POOR), 0.3f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DACITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DACITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SLATE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SLATE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(20)
            .failchance(0.05f)
            .build(output, toIERL("chalcopyrite"));

        final TagKey<Item> mercury = TagUtils.createItemWrapper(IETags.getOre("mercury"));
        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCItems.ORES.get(Ore.CINNABAR), 0.6f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.4f)
            .ore(mercury, 0.3f, getTagCondition(mercury))
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GABBRO).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GABBRO).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SCHIST).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.SCHIST).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(15)
            .failchance(0.1f)
            .build(output, toIERL("cinnabar"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(Blocks.SMALL_ORES.get(IEOre.GALENA), 0.4f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.POOR), 0.3f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.NORMAL), 0.2f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.NATIVE_SILVER), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_SILVER).get(Ore.Grade.POOR), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_SILVER).get(Ore.Grade.NORMAL), 0.15f)
            .ore(TFCItems.GRADED_ORES.get(Ore.NATIVE_SILVER).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.4f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.LIMESTONE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.LIMESTONE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.QUARTZITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.QUARTZITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(15)
            .failchance(0.05f)
            .build(output, toIERL("galena"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.ROCK_BLOCKS.get(Rock.GRANITE).get(Rock.BlockType.RAW), 0.3f)
            .ore(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.RAW), 0.3f)
            .ore(TFCBlocks.ROCK_BLOCKS.get(Rock.ANDESITE).get(Rock.BlockType.RAW), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GRANITE).get(Rock.BlockType.GRAVEL), 0.1f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.GRAVEL), 0.1f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.ANDESITE).get(Rock.BlockType.GRAVEL), 0.1f)
            .weight(25)
            .failchance(0.05f)
            .build(output, toIERL("igneous_rock"));

        final TagKey<Item> titanium = TagUtils.createItemWrapper(IETags.getOre("titanium"));
        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.MAGNETITE), 0.3f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MAGNETITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MAGNETITE).get(Ore.Grade.NORMAL), 0.15f)
            .ore(TFCItems.GRADED_ORES.get(Ore.MAGNETITE).get(Ore.Grade.RICH), 0.1f)
            .ore(Blocks.SMALL_ORES.get(IEOre.BAUXITE), 0.7f)
            .ore(Items.ORES.get(IEOre.BAUXITE).get(Ore.Grade.POOR), 0.5f)
            .ore(Items.ORES.get(IEOre.BAUXITE).get(Ore.Grade.NORMAL), 0.4f)
            .ore(Items.ORES.get(IEOre.BAUXITE).get(Ore.Grade.RICH), 0.2f)
            .ore(titanium, 0.1f, getTagCondition(titanium))
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.MARBLE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.MARBLE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.PHYLLITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.PHYLLITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(20)
            .failchance(0.05f)
            .build(output, toIERL("laterite"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(TFCBlocks.SMALL_ORES.get(Ore.HEMATITE), 0.35f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCBlocks.SMALL_ORES.get(Ore.GARNIERITE), 0.35f)
            .ore(TFCItems.GRADED_ORES.get(Ore.GARNIERITE).get(Ore.Grade.POOR), 0.25f)
            .ore(TFCItems.GRADED_ORES.get(Ore.GARNIERITE).get(Ore.Grade.NORMAL), 0.2f)
            .ore(TFCItems.GRADED_ORES.get(Ore.GARNIERITE).get(Ore.Grade.RICH), 0.1f)
            .ore(TFCItems.ORES.get(Ore.SULFUR), 0.3f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHALK).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHALK).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GNEISS).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.GNEISS).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(25)
            .failchance(0.05f)
            .build(output, toIERL("pentlandite"));

        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(net.minecraft.world.item.Items.CLAY, 0.5f)
            .ore(Tags.Items.SANDS, 0.3f)
            .ore(Tags.Items.GRAVELS, 0.2f)
            .weight(25)
            .failchance(0.05f)
            .build(output, toIERL("silt"));

        final TagKey<Item> thorium = TagUtils.createItemWrapper(IETags.getOre("thorium"));
        MineralMixBuilder.builder()
            .dimensionOverworld()
            .ore(Blocks.SMALL_ORES.get(IEOre.URANINITE), 0.7f)
            .ore(Items.ORES.get(IEOre.URANINITE).get(Ore.Grade.POOR), 0.5f)
            .ore(Items.ORES.get(IEOre.URANINITE).get(Ore.Grade.NORMAL), 0.3f)
            .ore(Items.ORES.get(IEOre.URANINITE).get(Ore.Grade.RICH), 0.2f)
            .ore(Blocks.SMALL_ORES.get(IEOre.GALENA), 0.35f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.POOR), 0.25f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.NORMAL), 0.2f)
            .ore(Items.ORES.get(IEOre.GALENA).get(Ore.Grade.RICH), 0.1f)
            .ore(thorium, 0.1f, getTagCondition(thorium))
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.DIORITE).get(Rock.BlockType.GRAVEL), 0.2f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHERT).get(Rock.BlockType.RAW), 0.5f)
            .spoil(TFCBlocks.ROCK_BLOCKS.get(Rock.CHERT).get(Rock.BlockType.GRAVEL), 0.2f)
            .weight(10)
            .failchance(0.15f)
            .build(output, toIERL("uraninite"));
    }

    private static ICondition getTagCondition(TagKey<?> tag)
    {
        return new NotCondition(new TagEmptyCondition(tag.location()));
    }

    private ResourceLocation toRL(String name)
    {
        return TFC_IE_Addon.identifier("mineral/" + name);
    }

    private ResourceLocation toIERL(String name)
    {
        return ImmersiveEngineering.rl("mineral/" + name);
    }
}
