package com.nmagpie.tfc_ie_addon.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ServerConfig
{

    ServerConfig(Builder innerBuilder)
    {

        innerBuilder.push("general");

        innerBuilder.pop();
    }
}
