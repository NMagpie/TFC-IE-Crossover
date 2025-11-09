package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.data.FluidHeat;

public class BuiltinItemHeat extends DataManagerProvider<HeatDefinition> implements Accessors
{
    private final CompletableFuture<?> before;

    public BuiltinItemHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<?> before)
    {
        super(HeatCapability.MANAGER, output, lookup);
        this.before = before;
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> beforeRun()
    {
        return before.thenCompose(v -> super.beforeRun());
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(IEItems.Ingredients.SLAG, 0.6f);
        add(net.minecraft.world.item.Items.BRICKS, 1.0f);

        Blocks.METALS.forEach((metal, blocks) -> {
            add(metal, IEMetal.BlockType.BLOCK);
            add(metal, IEMetal.BlockType.BLOCK_SLAB);
            add(metal, IEMetal.BlockType.BLOCK_STAIRS);
        });
        Items.METAL_ITEMS.forEach((metal, items) -> {
            add(metal, IEMetal.ItemType.SHEET);
            add(metal, IEMetal.ItemType.DOUBLE_INGOT);
        });
        for (IEMetal metal : IEMetal.values())
        {
            add(metal.getSerializedName() + "/ingot", Ingredient.of(commonTagOf(Registries.ITEM, "ingots/" + metal.name())), metal, 100);
            add(metal.getSerializedName() + "/rod", Ingredient.of(commonTagOf(Registries.ITEM, "rods/" + metal.name())), metal, 50);
            add(metal.getSerializedName() + "/plate", Ingredient.of(commonTagOf(Registries.ITEM, "plates/" + metal.name())), metal, 100);
        }
//        for (Metal metal : Utils.METAL_MAP.keySet())
//            add(metal.getSerializedName() + "/plate", Ingredient.of(commonTagOf(Registries.ITEM, "plates/" + (metal == Metal.WROUGHT_IRON ? "iron" : metal.name()))), metal, 100);
        Items.ORES.forEach((ore, blocks) ->
            add(ore.name(), Ingredient.of(
                Blocks.SMALL_ORES.get(ore),
                blocks.get(Ore.Grade.POOR),
                blocks.get(Ore.Grade.NORMAL),
                blocks.get(Ore.Grade.RICH)
            ), ore.metal(), 40)
        );
    }

    private void add(ItemLike item, float heatCapacity)
    {
        add(Ingredient.of(item), heatCapacity);
    }

    private void add(Ingredient item, float heatCapacity)
    {
        add(nameOf(item), new HeatDefinition(item, heatCapacity, 0f, 0f));
    }

    private void add(IEMetal metal, IEMetal.ItemType type)
    {
        add(metal.getSerializedName() + "/" + type.name().toLowerCase(Locale.ROOT), ingredientOf(metal, type), metal, units(type));
    }

    private void add(IEMetal metal, IEMetal.BlockType type)
    {
        add(metal.getSerializedName() + "/" + type.name().toLowerCase(Locale.ROOT), ingredientOf(metal, type), metal, units(type));
    }

    private void add(String name, Ingredient ingredient, IEMetal metal, int units)
    {
        final FluidHeat fluidHeat = FluidHeat.MANAGER.getOrThrow(TFC_IE_Addon.identifier(metal.getSerializedName()));
        add(name, new HeatDefinition(
            ingredient,
            (fluidHeat.specificHeatCapacity() / BuiltinFluidHeat.HEAT_CAPACITY) * (units / 100f),
            fluidHeat.meltTemperature() * 0.6f,
            fluidHeat.meltTemperature() * 0.8f));
    }

    @SuppressWarnings("SameParameterValue")
    private void add(String name, Ingredient ingredient, Metal metal, int units)
    {
        final FluidHeat fluidHeat = FluidHeat.MANAGER.getOrThrow(Helpers.identifier(metal.getSerializedName()));
        add(name, new HeatDefinition(
            ingredient,
            (fluidHeat.specificHeatCapacity() / BuiltinFluidHeat.HEAT_CAPACITY) * (units / 100f),
            fluidHeat.meltTemperature() * 0.6f,
            fluidHeat.meltTemperature() * 0.8f));
    }
}
