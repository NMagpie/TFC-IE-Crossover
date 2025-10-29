package com.nmagpie.tfc_ie_addon.util;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;

import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;

public class ModInteractionManager
{
    public static void registerInteractions()
    {
        InteractionManager.registerBlock(new BlockItemPlacement(IEItems.Misc.HEMP_SEEDS, Blocks.CROPS.get(IECrop.HEMP)));
    }
}
