package com.nmagpie.tfc_ie_addon.config;

import java.util.function.Supplier;

import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.config.BaseConfig;


public class ServerConfig extends BaseConfig
{
    public final Supplier<Integer> crucibleExternalHeaterFEPerTick;
    public final Supplier<Integer> crucibleExternalHeaterTemperature;
    public final Supplier<Size> crateMaximumItemSize;
    public final Supplier<Double> tfcRotationalEnergyModifier;

    ServerConfig(ConfigBuilder builder)
    {
        builder.push("crucibleExternalHeater");

        crucibleExternalHeaterFEPerTick = builder.comment("The amount of FE an external heater consumes per tick when heating a crucible.").define("crucibleExternalHeaterFEPerTick", 20, 0, 32000);
        crucibleExternalHeaterTemperature = builder.comment("The maximum temperature a crucible reaches when heated by an external heater.").define("crucibleExternalHeaterTemperature", 2000, 0, Integer.MAX_VALUE);

        builder.swap("misc");

        crateMaximumItemSize = builder.comment("The largest (inclusive) size of an item that is allowed in a wooden storage crate or reinforced storage crate.").define("crateMaximumItemSize", Size.VERY_LARGE);
        tfcRotationalEnergyModifier = builder.comment("A modifier to apply to the energy generation of a TFC windmill and TFC water wheel on a kinetic dynamo.").define("tfcRotationalEnergyModifier", 1, Double.MIN_VALUE, Double.MAX_VALUE);

        builder.pop();
    }
}