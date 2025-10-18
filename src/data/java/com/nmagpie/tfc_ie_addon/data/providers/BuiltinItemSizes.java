package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import com.nmagpie.tfc_ie_addon.common.ModTags;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;

@SuppressWarnings("SameParameterValue")
public class BuiltinItemSizes extends DataManagerProvider<ItemSizeDefinition> implements Accessors
{
    public BuiltinItemSizes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(ItemSizeManager.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add("molds", ModTags.MOLDS, Size.NORMAL, Weight.MEDIUM);
    }

    private void add(String name, TagKey<Item> item, Size size, Weight weight)
    {
        add(name, new ItemSizeDefinition(Ingredient.of(item), size, weight));
    }
}
