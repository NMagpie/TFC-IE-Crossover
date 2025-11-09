package com.nmagpie.tfc_ie_addon.data.recipes;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

@SuppressWarnings("SameParameterValue")
public interface HeatRecipes extends Recipes
{
    default void heatRecipes()
    {
        add(IEItems.Ingredients.SLAG, IEBlocks.StoneDecoration.SLAG_GLASS, 380);
        add(net.minecraft.world.item.Items.BRICKS, IEBlocks.StoneDecoration.CLINKER_BRICK, 1499);

        for (IEOre ore : IEOre.values())
            addOres(ore, ore.metal());

        for (IEMetal metal : IEMetal.values())
        {
            add(Ingredient.of(ingotTagOf(metal)), new FluidStack(fluidOf(metal), 100), temperatureOf(metal));
            add(Ingredient.of(commonTagOf(Registries.ITEM, "rods/" + metal.getSerializedName())), new FluidStack(fluidOf(metal), 50), temperatureOf(metal));
        }

        Items.METAL_ITEMS.forEach((metal, items) -> items.forEach((type, item) -> add(nameOf(item), new HeatingRecipe(
            ingredientOf(metal, type),
            ItemStackProvider.empty(),
            new FluidStack(fluidOf(metal), units(type)),
            temperatureOf(metal), new ItemStack(item).isDamageableItem()))));
        Blocks.METALS.forEach((metal, items) -> items.forEach((type, item) -> add(nameOf(item), new HeatingRecipe(
            ingredientOf(metal, type),
            ItemStackProvider.empty(),
            new FluidStack(fluidOf(metal), units(type)),
            temperatureOf(metal), new ItemStack(item).isDamageableItem()))));
    }

    private void addOres(IEOre ore, IEMetal metal)
    {
        final float temperature = temperatureOf(metal);

        add(Ingredient.of(Blocks.SMALL_ORES.get(ore)), new FluidStack(fluidOf(metal), 10), temperature);
        add(Ingredient.of(Items.ORES.get(ore).get(Ore.Grade.POOR)), new FluidStack(fluidOf(metal), 15), temperature);
        add(Ingredient.of(Items.ORES.get(ore).get(Ore.Grade.NORMAL)), new FluidStack(fluidOf(metal), 25), temperature);
        add(Ingredient.of(Items.ORES.get(ore).get(Ore.Grade.RICH)), new FluidStack(fluidOf(metal), 35), temperature);
    }

    private void add(ItemLike input, ItemLike output, float temperature)
    {
        add(Ingredient.of(input), ItemStackProvider.of(output), temperature);
    }

    private void add(Ingredient input, ItemStackProvider output, float temperature)
    {
        add(new HeatingRecipe(input, output, FluidStack.EMPTY, temperature, false));
    }

    private void add(Ingredient input, FluidStack output, float temperature)
    {
        add(nameOf(input), new HeatingRecipe(input, ItemStackProvider.empty(), output, temperature, false));
    }
}
