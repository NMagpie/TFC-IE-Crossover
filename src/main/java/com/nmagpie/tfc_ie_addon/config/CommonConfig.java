package com.nmagpie.tfc_ie_addon.config;

import java.util.function.Function;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class CommonConfig
{
    public final ForgeConfigSpec.ConfigValue<Boolean> enableImmersivePetorlumCompat;

    CommonConfig(Builder innerBuilder)
    {
        Function<String, Builder> builder = name -> innerBuilder.translation(TFC_IE_Addon.MOD_ID + ".config.common." + name);

        innerBuilder.push("compat");

        enableImmersivePetorlumCompat = builder.apply("enableImmersivePetorlumCompat").comment("Only takes effect if Immersive Petroleum is installed.").define("enableImmersivePetorlumCompat", true);

        innerBuilder.pop();
    }
}
