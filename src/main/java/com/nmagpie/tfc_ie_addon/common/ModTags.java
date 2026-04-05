package com.nmagpie.tfc_ie_addon.common;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class ModTags
{
    public static class Items
    {
        public static final TagKey<Item> MOLDS = tag("molds");
        public static final TagKey<Item> STONE_BRICKS_NO_VARIANTS = tag("stone_bricks_no_variants");

        private static TagKey<Item> tag(String name)
        {
            return TagKey.create(Registries.ITEM, TFC_IE_Addon.identifier(name));
        }
    }

    public static class Biomes
    {
        public static final TagKey<Biome> GENERATE_MINERAL_MIX = tag("generate_mineral_mix");

        private static TagKey<Biome> tag(String name)
        {
            return TagKey.create(Registries.BIOME, TFC_IE_Addon.identifier(name));
        }
    }
}
