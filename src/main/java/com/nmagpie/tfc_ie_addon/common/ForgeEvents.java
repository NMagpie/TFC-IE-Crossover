package com.nmagpie.tfc_ie_addon.common;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.util.IEHeatHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;

public class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addGenericListener(BlockEntity.class, ForgeEvents::attachExternalHeatHandler);
    }

    public static void attachExternalHeatHandler(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if (event.getObject() instanceof CrucibleBlockEntity crucible)
        {
            IEHeatHandler.Provider provider = new IEHeatHandler.Provider(crucible);
            event.addCapability(TFC_IE_Addon.identifier("crucible_external_heater"), provider);
            event.addListener(provider::invalidate);
        }
    }
}
