package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.DataManager;

@SuppressWarnings("SameParameterValue")
public class BuiltinClimateRanges extends DataManagerProvider<ClimateRange>
{
    public BuiltinClimateRanges(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(ClimateRange.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(ClimateRange.MANAGER.getReference(TFC_IE_Addon.identifier("crop/hemp")), b -> b.hydration(30, 80).temperature(10, 47));
    }

    private void add(DataManager.Reference<ClimateRange> reference, UnaryOperator<ClimateRange.Builder> builder)
    {
        add(reference, builder.apply(new ClimateRange.Builder()).build());
    }
}
