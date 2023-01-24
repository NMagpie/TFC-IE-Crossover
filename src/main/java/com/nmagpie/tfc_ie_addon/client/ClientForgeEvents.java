package com.nmagpie.tfc_ie_addon.client;

import com.google.common.base.Stopwatch;
import com.nmagpie.tfc_ie_addon.common.Helpers;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

import static com.nmagpie.tfc_ie_addon.TFC_IE_Addon.LOGGER;

public class ClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientForgeEvents::onSelfTest);
    }

    private static void onSelfTest(SelfTests.ClientSelfTestEvent event)
    {
        if (Helpers.ASSERTIONS_ENABLED)
        {
            final Stopwatch tick = Stopwatch.createStarted();
            ClientSelfTests.validateModels();
            LOGGER.info("Client self tests passed in {}", tick.stop());
        }
    }
}
