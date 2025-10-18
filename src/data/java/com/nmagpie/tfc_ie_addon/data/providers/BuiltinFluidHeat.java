package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import net.dries007.tfc.util.data.FluidHeat;

public class BuiltinFluidHeat extends DataManagerProvider<FluidHeat> implements Accessors
{
    public static final float HEAT_CAPACITY = 0.003f;

    public BuiltinFluidHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(FluidHeat.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(IEMetal.ELECTRUM, 0.55f, 1000);
        add(IEMetal.CONSTANTAN, 0.35f, 1250);
        add(IEMetal.ALUMINUM, 0.7f, 1740);
        add(IEMetal.LEAD, 0.13f, 330);
        add(IEMetal.URANIUM, 0.12f, 1130);
    }

    private void add(IEMetal metal, float baseHeatCapacity, float meltTemperature)
    {
        add(metal.getSerializedName(), new FluidHeat(Fluids.METALS.get(metal).getSource(), meltTemperature, HEAT_CAPACITY / baseHeatCapacity));
    }
}
