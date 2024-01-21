package com.nmagpie.tfc_ie_addon;

import com.mojang.logging.LogUtils;
import com.nmagpie.tfc_ie_addon.client.ClientEvents;
import com.nmagpie.tfc_ie_addon.common.CreativeTabs;
import com.nmagpie.tfc_ie_addon.common.Events;
import com.nmagpie.tfc_ie_addon.common.ForgeEvents;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.config.Config;
import com.nmagpie.tfc_ie_addon.util.HerbicideEffects;
import com.nmagpie.tfc_ie_addon.util.ModCauldronInteractions;
import com.nmagpie.tfc_ie_addon.util.RegisteredSoils;
import com.nmagpie.tfc_ie_addon.world.feature.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(TFC_IE_Addon.MOD_ID)
public class TFC_IE_Addon
{
    public static final String MOD_ID = "tfc_ie_addon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFC_IE_Addon()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.ITEMS.register(eventBus);
        Blocks.BLOCKS.register(eventBus);
        Fluids.FLUIDS.register(eventBus);
        Fluids.FLUID_TYPES.register(eventBus);
        Features.FEATURES.register(eventBus);
        CreativeTabs.CREATIVE_TABS.register(eventBus);

        Config.init();
        Events.init();
        ForgeEvents.init();

        eventBus.addListener(this::setup);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init();
        }
    }

    private void setup(FMLCommonSetupEvent event)
    {
        RegisteredSoils.registerTFCSoils();
        HerbicideEffects.register();

        event.enqueueWork(ModCauldronInteractions::registerCauldronInteractions);
    }

    public static ResourceLocation identifier(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
