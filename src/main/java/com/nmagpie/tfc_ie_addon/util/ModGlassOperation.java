package com.nmagpie.tfc_ie_addon.util;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.items.TFCItems;

@SuppressWarnings("unused")
public class ModGlassOperation
{
    public static final DeferredRegister<GlassOperation> OPERATIONS = DeferredRegister.create(GlassOperation.KEY, TFC_IE_Addon.MOD_ID);

    public static final DeferredHolder<GlassOperation, GlassOperation> LEAD = powder("lead", Items.POWDERS.get(IEOre.GALENA));
    public static final DeferredHolder<GlassOperation, GlassOperation> URANIUM = powder("uranium", Items.POWDERS.get(IEOre.URANINITE));

    private static DeferredHolder<GlassOperation, GlassOperation> powder(String name, TFCItems.ItemId item)
    {
        return OPERATIONS.register(name, () -> new GlassOperation(item.holder(), true));
    }
}
