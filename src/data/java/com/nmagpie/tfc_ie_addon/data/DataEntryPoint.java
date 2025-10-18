package com.nmagpie.tfc_ie_addon.data;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinBlockTags;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinFertilizers;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinFluidHeat;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinFluidTags;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinFuels;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinItemHeat;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinItemSizes;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinItemTags;
import com.nmagpie.tfc_ie_addon.data.providers.BuiltinRecipes;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TFC_IE_Addon.MOD_ID)
public class DataEntryPoint
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        final PackOutput output = event.getGenerator().getPackOutput();
        final var lookup = event.getLookupProvider();

        final var fluidHeat = add(event, new BuiltinFluidHeat(output, lookup)).output();
        final var itemHeat = add(event, new BuiltinItemHeat(output, lookup, fluidHeat));

        add(event, new BuiltinRecipes(output, lookup, fluidHeat, itemHeat));

        final var blockTags = add(event, new BuiltinBlockTags(event, lookup)).contentsGetter();

        add(event, new BuiltinItemTags(event, lookup, blockTags));
        add(event, new BuiltinFluidTags(event, lookup));

        add(event, new BuiltinFertilizers(output, lookup));
        add(event, new BuiltinFuels(output, lookup));
        add(event, new BuiltinItemSizes(output, lookup));
    }

    private static <T extends DataProvider> T add(GatherDataEvent event, T provider)
    {
        return event.getGenerator().addProvider(true, provider);
    }
}
