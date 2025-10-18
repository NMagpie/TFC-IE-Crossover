package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.util.data.Fertilizer;

public class BuiltinFertilizers extends DataManagerProvider<Fertilizer> implements Accessors
{
    public BuiltinFertilizers(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(Fertilizer.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(IEItems.Misc.FERTILIZER, 0.1f, 0.2f, 0.5f);
        add(IEItems.Ingredients.SLAG, 0, 0.2f, 0.1f);
        add(IEItems.Ingredients.DUST_SALTPETER, 0.2f, 0, 0);
    }

    private void add(ItemLike input, float n, float p, float k)
    {
        add(nameOf(input), new Fertilizer(Ingredient.of(input), n, p, k));
    }
}
