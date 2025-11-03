package com.nmagpie.tfc_ie_addon;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import com.mojang.logging.LogUtils;
import com.nmagpie.tfc_ie_addon.client.ClientEvents;
import com.nmagpie.tfc_ie_addon.common.CreativeTabs;
import com.nmagpie.tfc_ie_addon.common.blockentities.BlockEntities;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.config.Config;
import com.nmagpie.tfc_ie_addon.util.CrucibleHeater;
import com.nmagpie.tfc_ie_addon.util.EmptyRecipe;
import com.nmagpie.tfc_ie_addon.util.HerbicideEffects;
import com.nmagpie.tfc_ie_addon.util.ModGlassOperation;
import com.nmagpie.tfc_ie_addon.util.ModClocheRenderFunctions;
import com.nmagpie.tfc_ie_addon.util.ModInteractionManager;
import com.nmagpie.tfc_ie_addon.world.feature.Features;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;

@Mod(TFC_IE_Addon.MOD_ID)
public class TFC_IE_Addon
{
    public static final String MOD_ID = "tfc_ie_addon";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFC_IE_Addon(ModContainer mod, IEventBus bus)
    {
        mod.registerConfig(ModConfig.Type.SERVER, Config.SERVER.spec());

        bus.addListener(this::setup);
        bus.addListener(this::register);
        bus.addListener(this::registerCapabilities);

        Items.ITEMS.register(bus);
        Blocks.BLOCKS.register(bus);
        BlockEntities.BLOCK_ENTITIES.register(bus);
        Fluids.FLUIDS.register(bus);
        Fluids.FLUID_TYPES.register(bus);
        Features.FEATURES.register(bus);
        CreativeTabs.CREATIVE_TABS.register(bus);

        ModGlassOperation.OPERATIONS.register(bus);
        ModClocheRenderFunctions.register();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init(bus);
        }
    }

    private void setup(FMLCommonSetupEvent event)
    {
        ModInteractionManager.registerInteractions();
        ModClocheRenderFunctions.init();
        HerbicideEffects.register();
    }

    private void register(RegisterEvent event)
    {
        event.register(Registries.RECIPE_TYPE, EmptyRecipe.ID, () -> EmptyRecipe.TYPE);
        event.register(Registries.RECIPE_SERIALIZER, EmptyRecipe.ID, () -> EmptyRecipe.SERIALIZER);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(
            ExternalHeaterHandler.CAPABILITY,
            TFCBlockEntities.CRUCIBLE.get(),
            CrucibleHeater::new
        );
    }

    public static ResourceLocation identifier(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
