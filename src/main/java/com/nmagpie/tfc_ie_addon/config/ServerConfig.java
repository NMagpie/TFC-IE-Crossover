package com.nmagpie.tfc_ie_addon.config;

import java.util.function.Supplier;

import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.config.BaseConfig;


public class ServerConfig extends BaseConfig
{
    public final Supplier<Integer> crucibleExternalHeaterFEPerTick;
    public final Supplier<Integer> crucibleExternalHeaterTemperature;
    public final Supplier<Size> crateMaximumItemSize;

    ServerConfig(ConfigBuilder builder)
    {
        builder.push("crucibleExternalHeater");

        crucibleExternalHeaterFEPerTick = builder.comment("The amount of FE an external heater consumes per tick when heating a crucible.").define("crucibleExternalHeaterFEPerTick", 20, 0, 32000);
        crucibleExternalHeaterTemperature = builder.comment("The maximum temperature a crucible reaches when heated by an external heater.").define("crucibleExternalHeaterTemperature", 2000, 0, Integer.MAX_VALUE);

        builder.swap("crate");

        crateMaximumItemSize = builder.comment("The largest (inclusive) size of an item that is allowed in a wooden storage crate or reinforced storage crate.").define("crateMaximumItemSize", Size.VERY_LARGE);

        builder.pop();
    }
}