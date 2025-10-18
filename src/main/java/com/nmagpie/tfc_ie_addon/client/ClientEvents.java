package com.nmagpie.tfc_ie_addon.client;

import java.util.Objects;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.api.ManualHelper;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

import net.dries007.tfc.client.extensions.FluidRendererExtension;
import net.dries007.tfc.common.fluids.TFCFluids;

import static net.dries007.tfc.client.ClientEventHandler.*;

public class ClientEvents
{
    public static void init(IEventBus bus)
    {
        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::registerColorHandlerItems);
        bus.addListener(ClientEvents::registerExtensions);
    }

    @SuppressWarnings("deprecation")
    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Render Types
        final RenderType cutout = RenderType.cutout();

        Stream.of(Blocks.BUDDING_QUARTZ, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_CLUSTER, Blocks.LARGE_QUARTZ_BUD, Blocks.MEDIUM_QUARTZ_BUD, Blocks.SMALL_QUARTZ_BUD
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Blocks.SMALL_ORES.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));
        Blocks.ORES.values().forEach(map -> map.values().forEach(inner -> inner.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout))));

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

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        for (Fluid fluid : BuiltInRegistries.FLUID)
        {
            if (Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid)).getNamespace().equals(TFC_IE_Addon.MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }

    public static void registerExtensions(RegisterClientExtensionsEvent event)
    {
        Fluids.METALS.forEach((metal, holder) -> event.registerFluidType(
            new FluidRendererExtension(TFCFluids.ALPHA_MASK | metal.getColor(), MOLTEN_STILL, MOLTEN_FLOW, null, null),
            holder.getType())
        );
    }
}
