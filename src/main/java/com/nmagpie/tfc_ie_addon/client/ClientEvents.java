package com.nmagpie.tfc_ie_addon.client;

import blusunrize.immersiveengineering.api.ManualHelper;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.util.Metal;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ClientEvents {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::onTextureStitch);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        // Render Types
        final RenderType cutout = RenderType.cutout();

        Stream.of(Blocks.SMALL_ALUMINUM, Blocks.SMALL_LEAD, Blocks.SMALL_URANIUM, Blocks.BUDDING_QUARTZ, Blocks.QUARTZ_BLOCK,
                Blocks.QUARTZ_CLUSTER, Blocks.LARGE_QUARTZ_BUD, Blocks.MEDIUM_QUARTZ_BUD, Blocks.SMALL_QUARTZ_BUD
        ).forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));

        Blocks.ALUMINUM_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.LEAD_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        Blocks.URANIUM_ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));

        setupManual();
    }

    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        for (Metal metal : Metal.values()) {
            event.addSprite(metal.getSheet());
        }
    }

    public static void setupManual() {
        config.put("crucibleExternalHeaterFEPerTick", Config.SERVER.crucibleExternalHeaterFEPerTick::get);
        config.put("crucibleExternalHeaterTemperature", Config.SERVER.crucibleExternalHeaterTemperature::get);

        ManualHelper.ADD_CONFIG_GETTER.getValue().accept(s -> {
            if (s.startsWith(TFC_IE_Addon.MOD_ID)) {
                String path = s.substring(s.indexOf(".") + 1);
                if (config.containsKey(path)) {
                    return config.get(path).get();
                }
            }
            return null;
        });
    }

    public final static Map<String, Supplier<Object>> config = new HashMap<>();
}
