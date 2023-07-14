package com.nmagpie.tfc_ie_addon.common;

import net.minecraft.resources.ResourceLocation;

import static com.nmagpie.tfc_ie_addon.TFC_IE_Addon.*;

public class Helpers
{
    public static final boolean ASSERTIONS_ENABLED = detectAssertionsEnabled();

    public static ResourceLocation identifier(String id)
    {
        return new ResourceLocation(MOD_ID, id);
    }

    @SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
    private static boolean detectAssertionsEnabled()
    {
        boolean enabled = false;
        assert enabled = true;
        return enabled;
    }
}
