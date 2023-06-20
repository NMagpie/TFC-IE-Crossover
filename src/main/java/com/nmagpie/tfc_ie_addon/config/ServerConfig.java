package com.nmagpie.tfc_ie_addon.config;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

import java.util.function.Function;

public class ServerConfig {
    public final ForgeConfigSpec.IntValue crucibleExternalHeaterFEPerTick;
    public final ForgeConfigSpec.IntValue crucibleExternalHeaterTemperature;

    ServerConfig(Builder innerBuilder) {
        Function<String, Builder> builder = name -> innerBuilder.translation(TFC_IE_Addon.MOD_ID + ".config.server." + name);

        innerBuilder.push("crucibleExternalHeater");

        crucibleExternalHeaterFEPerTick = builder.apply("crucibleExternalHeaterFEPerTick").comment("The amount of FE an external heater consumes per tick when heating a crucible.").defineInRange("crucibleExternalHeaterFEPerTick", 20, 0, 32000);
        crucibleExternalHeaterTemperature = builder.apply("crucibleExternalHeaterTemperature").comment("The maximum temperature a crucible reaches when heated by an external heater.").defineInRange("crucibleExternalHeaterTemperature", 1600, 0, Integer.MAX_VALUE);

        innerBuilder.pop();
    }
}