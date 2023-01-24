package com.nmagpie.tfc_ie_addon.client;

import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.util.Metal;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.stream.Stream;

public class ClientEvents
{

    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::onTextureStitch);
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Render Types
        final RenderType cutout = RenderType.cutout();

        Stream.of(Blocks.SMALL_ALUMINUM
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Stream.of(Blocks.SMALL_LEAD
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Stream.of(Blocks.SMALL_URANIUM
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Blocks.ALUMINUM_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.LEAD_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.URANIUM_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
    }

    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        for (Metal metal : Metal.values())
        {
            event.addSprite(metal.getSheet());
        }
    }
}
