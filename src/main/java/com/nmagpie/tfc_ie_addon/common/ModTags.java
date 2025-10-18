package com.nmagpie.tfc_ie_addon.common;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags
{
    public static final TagKey<Item> MOLDS = tag("molds");
    public static final TagKey<Item> STONE_BRICKS_NO_VARIANTS = tag("stone_bricks_no_variants");

    private static TagKey<Item> tag(String name)
    {
        return TagKey.create(Registries.ITEM, TFC_IE_Addon.identifier(name));
    }
}
