package com.nmagpie.tfc_ie_addon;

import com.mojang.logging.LogUtils;
import com.nmagpie.tfc_ie_addon.client.ClientEvents;
import com.nmagpie.tfc_ie_addon.client.ClientForgeEvents;
import com.nmagpie.tfc_ie_addon.common.Events;
import com.nmagpie.tfc_ie_addon.common.blockenties.TFC_IE_BlockEntities;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.common.network.Packets;
import com.nmagpie.tfc_ie_addon.common.recipes.AddonIngredientSerializerFluidStack;
import com.nmagpie.tfc_ie_addon.common.recipes.RecipeSerializers;
import com.nmagpie.tfc_ie_addon.common.util.HerbicideEffects;
import com.nmagpie.tfc_ie_addon.common.util.Registered_Soils;
import com.nmagpie.tfc_ie_addon.config.Config;
import com.nmagpie.tfc_ie_addon.world.feature.Features;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
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
        Features.FEATURES.register(eventBus);
        TFC_IE_BlockEntities.BLOCK_ENTITIES.register(eventBus);
        RecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);

        Packets.init();
        Config.init();
        Events.init();

        CraftingHelper.register(AddonIngredientSerializerFluidStack.NAME, AddonIngredientSerializerFluidStack.INSTANCE);

        eventBus.addListener(this::setup);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init();
            ClientForgeEvents.init();
        }
    }

    private void setup(FMLCommonSetupEvent event)
    {
        Registered_Soils.register_tfc_soils();
        HerbicideEffects.register();
    }
}
