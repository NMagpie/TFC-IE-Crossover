package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.util.data.Fuel;

@SuppressWarnings("SameParameterValue")
public class BuiltinFuels extends DataManagerProvider<Fuel> implements Accessors
{
    public BuiltinFuels(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Fuel.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(IEItems.Ingredients.COAL_COKE, 3200, 1550, 1f);
        add(IEBlocks.StoneDecoration.COKE, 32000, 1550, 1f);
    }

    private void add(ItemLike item, int duration, float temperature, float purity)
    {
        add(nameOf(item), new Fuel(Ingredient.of(item), duration, temperature, purity));
    }
}
