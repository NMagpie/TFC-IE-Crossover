package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.concurrent.CompletableFuture;
import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IEApiDataComponents;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.tool.conveyor.ConveyorHandler;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.BasicConveyor;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.SplitConveyor;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.register.IEBlocks.*;
import blusunrize.immersiveengineering.common.register.IEItems;
import blusunrize.immersiveengineering.common.register.IEItems.*;
import blusunrize.immersiveengineering.data.recipes.IERecipeProvider;
import blusunrize.immersiveengineering.data.recipes.WrappingRecipeOutput;
import com.nmagpie.tfc_ie_addon.common.ModTags;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import com.nmagpie.tfc_ie_addon.data.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

public class IECraftingRecipes extends IERecipeProvider implements Accessors
{
    public IECraftingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, lookup);
    }

    @Override
    public void buildRecipes(RecipeOutput out)
    {
        decorationRecipes(out);
        deviceRecipes(out);
        ingredientRecipes(out);
        miscRecipes(out);
        oreRecipes(out);
        toolRecipes(out);
    }

    private void decorationRecipes(RecipeOutput out)
    {
        shapedMisc(WoodenDecoration.BASIC_ENGINEERING, 4)
            .pattern("iwi")
            .pattern("w w")
            .pattern("iwi")
            .define('i', getIronIngotsTag())
            .define('w', IETags.getItemTag(IETags.treatedWood))
            .unlockedBy("has_treated_planks", has(IETags.getItemTag(IETags.treatedWood)))
            .save(out, toRL(toPath(WoodenDecoration.BASIC_ENGINEERING)));

        shapedMisc(MetalDecoration.LV_COIL)
            .pattern("www")
            .pattern("wiw")
            .pattern("www")
            .define('i', getIronIngotsTag())
            .define('w', IEItems.Misc.WIRE_COILS.get(WireType.COPPER))
            .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
            .unlockedBy("has_"+toPath(IEItems.Misc.WIRE_COILS.get(WireType.COPPER)), has(IEItems.Misc.WIRE_COILS.get(WireType.COPPER)))
            .save(out, toRL(toPath(MetalDecoration.LV_COIL)));
        shapedMisc(MetalDecoration.MV_COIL)
            .pattern("www")
            .pattern("wiw")
            .pattern("www")
            .define('i', getIronIngotsTag())
            .define('w', IEItems.Misc.WIRE_COILS.get(WireType.ELECTRUM))
            .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
            .unlockedBy("has_"+toPath(IEItems.Misc.WIRE_COILS.get(WireType.ELECTRUM)), has(IEItems.Misc.WIRE_COILS.get(WireType.ELECTRUM)))
            .save(out, toRL(toPath(MetalDecoration.MV_COIL)));
        shapedMisc(MetalDecoration.HV_COIL)
            .pattern("www")
            .pattern("wiw")
            .pattern("www")
            .define('i', getIronIngotsTag())
            .define('w', IEItems.Misc.WIRE_COILS.get(WireType.STEEL))
            .unlockedBy("has_steel_ingot", has(IETags.getTagsFor(EnumMetals.STEEL).ingot))
            .unlockedBy("has_"+toPath(IEItems.Misc.WIRE_COILS.get(WireType.STEEL)), has(IEItems.Misc.WIRE_COILS.get(WireType.STEEL)))
            .save(out, toRL(toPath(MetalDecoration.HV_COIL)));

        shapedMisc(MetalDecoration.ALU_POST)
            .pattern("f")
            .pattern("f")
            .pattern("s")
            .define('f', MetalDecoration.ALU_FENCE)
            .define('s', ModTags.STONE_BRICKS_NO_VARIANTS)
            .unlockedBy("has_" + toPath(MetalDecoration.ALU_FENCE), has(MetalDecoration.ALU_FENCE))
            .save(out, toRL(toPath(MetalDecoration.ALU_POST)));
        shapedMisc(MetalDecoration.STEEL_POST)
            .pattern("f")
            .pattern("f")
            .pattern("s")
            .define('f', MetalDecoration.STEEL_FENCE)
            .define('s', ModTags.STONE_BRICKS_NO_VARIANTS)
            .unlockedBy("has_"+toPath(MetalDecoration.STEEL_FENCE), has(MetalDecoration.STEEL_FENCE))
            .save(out, toRL(toPath(MetalDecoration.STEEL_POST)));
        shapedMisc(WoodenDecoration.TREATED_POST)
            .pattern("f")
            .pattern("f")
            .pattern("s")
            .define('f', WoodenDecoration.TREATED_FENCE)
            .define('s', ModTags.STONE_BRICKS_NO_VARIANTS)
            .unlockedBy("has_"+toPath(WoodenDecoration.TREATED_FENCE), has(WoodenDecoration.TREATED_FENCE))
            .save(out, toRL(toPath(WoodenDecoration.TREATED_POST)));
    }

    private void deviceRecipes(RecipeOutput out)
    {
        shapedMisc(WoodenDevices.WINDMILL)
            .pattern("ppp")
            .pattern("pip")
            .pattern("ppp")
            .define('p', Ingredients.WINDMILL_BLADE)
            .define('i', getIronIngotsTag())
            .unlockedBy("has_"+toPath(Ingredients.WINDMILL_BLADE), has(Ingredients.WINDMILL_BLADE))
            .save(out, toRL(toPath(WoodenDevices.WINDMILL)));

        shapedMisc(WoodenDevices.WORKBENCH)
            .pattern("iss")
            .pattern("c f")
            .define('i', getIronIngotsTag())
            .define('s', IETags.getItemTag(IETags.treatedWoodSlab))
            .define('c', WoodenDevices.CRAFTING_TABLE)
            .define('f', WoodenDecoration.TREATED_FENCE)
            .unlockedBy("has_treated_planks", has(IETags.getItemTag(IETags.treatedWood)))
            .save(out, toRL(toPath(WoodenDevices.WORKBENCH)));

        shapedMisc(WoodenDevices.GUNPOWDER_BARREL)
            .pattern("gfg")
            .pattern("gbg")
            .define('f', IETags.fiberHemp)
            .define('g', Tags.Items.GUNPOWDERS)
            .define('b', WoodenDevices.WOODEN_BARREL)
            .unlockedBy("has_"+toPath(WoodenDevices.WOODEN_BARREL), has(WoodenDevices.WOODEN_BARREL))
            .save(out, toRL(toPath(WoodenDevices.GUNPOWDER_BARREL)));

        shapedMisc(Cloth.BALLOON, 2)
            .pattern(" f ")
            .pattern("ftf")
            .pattern(" s ")
            .define('f', IETags.fabricHemp)
            .define('t', Items.TORCH)
            .define('s', IETags.getItemTag(IETags.treatedWoodSlab))
            .unlockedBy("has_hemp_fabric", has(IETags.fabricHemp))
            .save(out, toRL(toPath(Cloth.BALLOON)));
        shapedMisc(Cloth.CUSHION, 3)
            .pattern("fff")
            .pattern("f f")
            .pattern("fff")
            .define('f', IETags.fabricHemp)
            .unlockedBy("has_hemp_fabric", has(IETags.fabricHemp))
            .save(out, toRL(toPath(Cloth.CUSHION)));
        shapedMisc(Cloth.STRIP_CURTAIN, 3)
            .pattern("sss")
            .pattern("fff")
            .pattern("fff")
            .define('s', IETags.metalRods)
            .define('f', IETags.fabricHemp)
            .unlockedBy("has_hemp_fabric", has(IETags.fabricHemp))
            .unlockedBy("has_metal_rod", has(IETags.metalRods))
            .save(out, toRL(toPath(Cloth.STRIP_CURTAIN)));

        ItemLike basic = ConveyorHandler.getBlock(BasicConveyor.TYPE);
        ItemLike splitter = ConveyorHandler.getBlock(SplitConveyor.TYPE);
        shapedMisc(basic, 8)
            .pattern("lll")
            .pattern("iri")
            .define('l', Tags.Items.LEATHERS)
            .define('i', getIronIngotsTag())
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(out, toRL(toPath(basic)));
        shapedMisc(splitter, 3)
            .pattern("cic")
            .pattern(" c ")
            .define('c', basic)
            .define('i', getIronIngotsTag())
            .unlockedBy("has_conveyor", has(basic))
            .save(out, toRL(toPath(splitter)));

        shapedMisc(Connectors.CURRENT_TRANSFORMER)
            .pattern(" m ")
            .pattern(" b ")
            .pattern("iei")
            .define('m', IEItems.Tools.VOLTMETER)
            .define('b', IETags.connectorInsulator)
            .define('i', getIronIngotsTag())
            .define('e', Ingredients.COMPONENT_ELECTRONIC)
            .unlockedBy("has_voltmeter", has(IEItems.Tools.VOLTMETER))
            .save(out, toRL(toPath(Connectors.CURRENT_TRANSFORMER)));

        shapedMisc(Connectors.TRANSFORMER)
            .pattern("lm")
            .pattern("eb")
            .pattern("ii")
            .define('l', Connectors.getEnergyConnector(WireType.LV_CATEGORY, false))
            .define('m', Connectors.getEnergyConnector(WireType.MV_CATEGORY, false))
            .define('e', Ingredients.COMPONENT_ELECTRONIC)
            .define('b', MetalDecoration.MV_COIL)
            .define('i', getIronIngotsTag())
            .unlockedBy("has_mv_connector", has(Connectors.getEnergyConnector(WireType.MV_CATEGORY, false)))
            .save(out, toRL(toPath(Connectors.TRANSFORMER)));
        shapedMisc(Connectors.TRANSFORMER_HV)
            .pattern("mh")
            .pattern("eb")
            .pattern("ii")
            .define('m', Connectors.getEnergyConnector(WireType.MV_CATEGORY, false))
            .define('h', Connectors.getEnergyConnector(WireType.HV_CATEGORY, false))
            .define('e', Ingredients.COMPONENT_ELECTRONIC)
            .define('b', MetalDecoration.HV_COIL)
            .define('i', getIronIngotsTag())
            .unlockedBy("has_hv_connector", has(Connectors.getEnergyConnector(WireType.HV_CATEGORY, false)))
            .save(out, toRL(toPath(Connectors.TRANSFORMER_HV)));

        shapedMisc(MetalDevices.DYNAMO)
            .pattern("rcr")
            .pattern("ili")
            .define('i', getIronIngotsTag())
            .define('l', MetalDecoration.LV_COIL)
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .define('c', Ingredients.COMPONENT_IRON)
            .unlockedBy("has_"+toPath(MetalDecoration.LV_COIL), has(MetalDecoration.LV_COIL))
            .save(out, toRL(toPath(MetalDevices.DYNAMO)));

        shapedMisc(MetalDevices.FLOODLIGHT)
            .pattern("sii")
            .pattern("pes")
            .pattern("sci")
            .define('i', getIronIngotsTag())
            .define('s', IETags.getTagsFor(EnumMetals.SILVER).plate)
            .define('e', Ingredients.LIGHT_BULB)
            .define('c', Ingredients.COMPONENT_IRON)
            .define('p', Tags.Items.GLASS_PANES)
            .unlockedBy("has_"+toPath(Ingredients.LIGHT_BULB), has(Ingredients.LIGHT_BULB))
            .save(out, toRL(toPath(MetalDevices.FLOODLIGHT)));

        shapedMisc(MetalDevices.ELECTROMAGNET)
            .pattern("pcp")
            .pattern("wiw")
            .pattern("pwp")
            .define('w', IEItems.Misc.WIRE_COILS.get(WireType.COPPER))
            .define('p', IETags.steelRod)
            .define('i', getIronIngotsTag())
            .define('c', Ingredients.COMPONENT_ELECTRONIC)
            .unlockedBy("has_"+toPath(Ingredients.COMPONENT_ELECTRONIC), has(Ingredients.COMPONENT_ELECTRONIC))
            .save(out, toRL(toPath(MetalDevices.ELECTROMAGNET)));

        shapedMisc(MetalDevices.PIPE_VALVE)
            .pattern("pc")
            .pattern("sr")
            .define('p', MetalDevices.FLUID_PIPE)
            .define('c', Ingredients.COMPONENT_IRON)
            .define('s', getIronRodsTag())
            .define('r', Tags.Items.DUSTS_REDSTONE)
            .unlockedBy("has_fluid_pipe", has(MetalDevices.FLUID_PIPE))
            .save(out, toRL(toPath(MetalDevices.PIPE_VALVE)));

        shapedMisc(MetalDevices.HATCH)
            .pattern("rr")
            .pattern("pp")
            .define('r', getIronRodsTag())
            .define('p', IETags.getTagsFor(EnumMetals.IRON).plate)
            .unlockedBy("has_plate", has(IETags.getTagsFor(EnumMetals.IRON).plate))
            .save(out, toRL(toPath(MetalDevices.HATCH)));
    }

    private void ingredientRecipes(RecipeOutput out)
    {
        shapedMisc(IEItems.Misc.BLUEPRINT)
            .pattern("jkl")
            .pattern("ddd")
            .pattern("ppp")
            .define('j', IETags.getTagsFor(EnumMetals.COPPER).ingot)
            .define('l', ingotTagOf(Metal.WROUGHT_IRON))
            .define('k', IETags.getTagsFor(EnumMetals.ALUMINUM).ingot)
            .define('d', Tags.Items.DYES_BLUE)
            .define('p', IETags.paper)
            .unlockedBy("has_paper", has(IETags.paper))
            .save(buildBlueprint(out, "components"), toRL("blueprint_components"));

        shapedMisc(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.BUZZSAW_SPAREBLADES))
            .pattern("rht")
            .pattern("rt ")
            .define('r', getIronRodsTag())
            .define('h', IETags.fiberHemp)
            .define('t', IETags.getItemTag(IETags.treatedWood))
            .unlockedBy("has_buzzsaw", has(Tools.BUZZSAW))
            .save(out, toRL(toPath(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.BUZZSAW_SPAREBLADES))));
        shapedMisc(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.SHIELD_SHOCK))
            .pattern("crc")
            .pattern("crc")
            .pattern("crc")
            .define('r', getIronRodsTag())
            .define('c', Connectors.getEnergyConnector(WireType.LV_CATEGORY, false))
            .unlockedBy("has_shield", has(IEItems.Misc.SHIELD))
            .save(out, toRL(toPath(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.SHIELD_SHOCK))));
        shapedMisc(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.SHIELD_MAGNET))
            .pattern("  l")
            .pattern("lc ")
            .pattern("lil")
            .define('i', getIronIngotsTag())
            .define('l', Tags.Items.LEATHERS)
            .define('c', MetalDecoration.LV_COIL)
            .unlockedBy("has_shield", has(IEItems.Misc.SHIELD))
            .save(out, toRL(toPath(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.SHIELD_MAGNET))));
        shapedMisc(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.POWERPACK_MAGNET))
            .pattern("rer")
            .pattern("wiw")
            .pattern(" w ")
            .define('r', IETags.steelRod)
            .define('w', IEItems.Misc.WIRE_COILS.get(WireType.COPPER))
            .define('e', Ingredients.COMPONENT_ELECTRONIC_ADV)
            .define('i', getIronIngotsTag())
            .unlockedBy("has_powerpack", has(IEItems.Misc.POWERPACK))
            .save(out, toRL(toPath(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.POWERPACK_MAGNET))));
    }

    private void miscRecipes(RecipeOutput out)
    {
        shapedMisc(IEItems.Misc.FLUORESCENT_TUBE)
            .pattern("GeG")
            .pattern("GgG")
            .pattern("GgG")
            .define('g', TFCBlocks.SMALL_ORES.get(Ore.SPHALERITE))
            .define('e', IEItems.Misc.GRAPHITE_ELECTRODE)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .unlockedBy("has_electrode", has(IEItems.Misc.GRAPHITE_ELECTRODE))
            .save(out, toRL(toPath(IEItems.Misc.FLUORESCENT_TUBE)));
        shapedMisc(IEItems.Misc.EARMUFFS)
            .pattern(" S ")
            .pattern("S S")
            .pattern("W W")
            .define('S', getIronRodsTag())
            .define('W', ItemTags.WOOL)
            .unlockedBy("has_iron_rod", has(IETags.ironRod))
            .save(out, toRL(toPath(IEItems.Misc.EARMUFFS)));
        ItemLike wireCoilRope = IEItems.Misc.WIRE_COILS.get(WireType.STRUCTURE_ROPE);
        shapedMisc(wireCoilRope, 4)
            .pattern(" w ")
            .pattern("wsw")
            .pattern(" w ")
            .define('w', IETags.fiberHemp)
            .define('s', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_hemp_fiber", has(IETags.fiberHemp))
            .save(out, toRL(toPath(wireCoilRope)));
    }

    private void oreRecipes(RecipeOutput out)
    {
        for (var entry : Utils.METAL_MAP.entrySet())
        {
            if (!(entry.getKey() == Metal.WROUGHT_IRON || entry.getKey() == Metal.GOLD))
            {
                add3x3Conversion(entry.getKey(), entry.getValue(), out);
            }
        }
    }

    private void toolRecipes(RecipeOutput out)
    {
        shapedMisc(Tools.HAMMER)
            .pattern("h")
            .pattern("s")
            .define('s', Tags.Items.RODS_WOODEN)
            .define('h', com.nmagpie.tfc_ie_addon.common.items.Items.HAMMER_HEAD)
            .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
            .save(out, toRL(toPath(Tools.HAMMER)));
        shapedMisc(Tools.WIRECUTTER)
            .pattern("si")
            .pattern(" s")
            .define('s', Tags.Items.RODS_WOODEN)
            .define('i', com.nmagpie.tfc_ie_addon.common.items.Items.WIRECUTTER_HEAD)
            .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
            .save(out, toRL(toPath(Tools.WIRECUTTER)));
        shapedMisc(Tools.SCREWDRIVER)
            .pattern(" i")
            .pattern("s ")
            .define('s', Tags.Items.RODS_WOODEN)
            .define('i', getIronRodsTag())
            .unlockedBy("has_iron_ingot", has(IETags.getTagsFor(EnumMetals.IRON).ingot))
            .save(out, toRL(toPath(Tools.SCREWDRIVER)));
        shapedMisc(Weapons.SPEEDLOADER)
            .pattern("sd")
            .pattern("dc")
            .define('s', getIronRodsTag())
            .define('d', IETags.plasticPlate)
            .define('c', Ingredients.COMPONENT_IRON)
            .unlockedBy("has_revolver", has(Weapons.REVOLVER))
            .save(out, toRL(toPath(Weapons.SPEEDLOADER)));
    }

    private void add3x3Conversion(Metal metal, EnumMetals enumMetals, RecipeOutput out)
    {
        final IETags.MetalTags tags = IETags.getTagsFor(enumMetals);
        final ItemLike nugget = IEItems.Metals.NUGGETS.get(enumMetals);
        final ItemLike ingot2 = IEItems.Metals.INGOTS.get(enumMetals);
        final ItemLike ingot = TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.INGOT);

        shapedMisc(ingot)
            .define('s', tags.nugget)
            .pattern("sss")
            .pattern("sss")
            .pattern("sss")
            .unlockedBy("has_"+toPath(nugget), has(nugget))
            .save(out, toRL(toPath(nugget)+"_to_")+toPath(ingot2));
        shapelessMisc(nugget, 9)
            .requires(tags.ingot)
            .unlockedBy("has_"+toPath(ingot2), has(ingot2))
            .save(out, toRL(toPath(ingot2)+"_to_"+toPath(nugget)));
    }

    @SuppressWarnings("SameParameterValue")
    private RecipeOutput buildBlueprint(RecipeOutput out, String blueprint)
    {
        ItemStack blueprintItem = new ItemStack(IEItems.Misc.BLUEPRINT);
        blueprintItem.set(IEApiDataComponents.BLUEPRINT_TYPE, blueprint);
        return WrappingRecipeOutput.replaceShapedOutput(out, blueprintItem);
    }
}
