package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.List;
import java.util.function.Supplier;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

public interface ChiselRecipes extends Recipes
{
    default void chiselRecipes()
    {
        stairSlab(IEBlocks.StoneDecoration.CONCRETE);
        stairSlab(IEBlocks.StoneDecoration.CONCRETE_BRICK);
        stairSlab(IEBlocks.StoneDecoration.CONCRETE_TILE);
        stairSlab(IEBlocks.StoneDecoration.CONCRETE_LEADED);
    }

    private void stairSlab(IEBlocks.BlockEntry<? extends Block> input)
    {
        stairSlab(input, IEBlocks.TO_STAIRS.get(input.getId()), IEBlocks.TO_SLAB.get(input.getId()));
    }

    private void stairSlab(Supplier<? extends Block> input, Supplier<? extends Block> stair, Supplier<? extends Block> slab)
    {
        chisel(List.of(input), stair, ChiselMode.STAIR, ItemStackProvider.empty());
        chisel(List.of(input), slab, ChiselMode.SLAB, ItemStackProvider.of(slab.get()));
    }

    private void chisel(List<? extends Supplier<? extends Block>> input, Supplier<? extends Block> output, Holder<ChiselMode> mode, ItemStackProvider outputItem)
    {
        add(new ChiselRecipe(
            BlockIngredient.of(input.stream().map(Supplier::get)),
            output.get().defaultBlockState(),
            mode.value(),
            outputItem
        ));
    }
}
