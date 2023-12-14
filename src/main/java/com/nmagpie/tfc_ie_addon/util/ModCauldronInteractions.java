package com.nmagpie.tfc_ie_addon.util;

import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.common.items.Items;

import net.dries007.tfc.util.CauldronInteractions;

public class ModCauldronInteractions
{
    public static void registerCauldronInteractions()
    {
        Items.METAL_FLUID_BUCKETS.values().forEach(reg -> CauldronInteractions.registerForVanillaCauldrons(reg.get(), CauldronInteractions::interactWithBucket));

        Blocks.METAL_CAULDRONS.forEach((metal, reg) -> CauldronInteractions.registerCauldronBlock(reg.get(), Fluids.METALS.get(metal).source().get()));
    }
}
