package com.nmagpie.tfc_ie_addon.client;

import java.util.Objects;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.api.ManualHelper;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::registerColorHandlerBlocks);
        bus.addListener(ClientEvents::registerColorHandlerItems);
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Render Types
        final RenderType cutout = RenderType.cutout();

        Stream.of(Blocks.SMALL_BAUXITE, Blocks.SMALL_GALENA, Blocks.SMALL_URANINITE, Blocks.BUDDING_QUARTZ, Blocks.QUARTZ_BLOCK,
            Blocks.QUARTZ_CLUSTER, Blocks.LARGE_QUARTZ_BUD, Blocks.MEDIUM_QUARTZ_BUD, Blocks.SMALL_QUARTZ_BUD
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Blocks.BAUXITE_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.GALENA_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.URANINITE_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));

        setupManual();
    }

    private static void setupManual()
    {
        ManualHelper.addConfigGetter(str -> switch (str)
        {
            case "crucibleExternalHeaterFEPerTick" -> Config.SERVER.crucibleExternalHeaterFEPerTick.get();
            case "crucibleExternalHeaterTemperature" -> Config.SERVER.crucibleExternalHeaterTemperature.get();
            default -> -1;
        });
    }

    public static void registerColorHandlerBlocks(RegisterColorHandlersEvent.Block event)
    {
        Blocks.METAL_CAULDRONS.forEach((metal, reg) -> event.register((state, level, pos, tintIndex) -> metal.getColor(), reg.get()));
    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
        {
            if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getNamespace().equals(TFC_IE_Addon.MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }
}
