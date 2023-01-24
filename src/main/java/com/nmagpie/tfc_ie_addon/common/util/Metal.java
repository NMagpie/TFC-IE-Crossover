package com.nmagpie.tfc_ie_addon.common.util;

import com.nmagpie.tfc_ie_addon.common.Helpers;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.Locale;

public enum Metal implements RegistryMetal
{
    ALUMINUM(0xCCC1BCFF),

    LEAD(0x433F4DFF),

    URANIUM(0x738A6CFF);

    private final String serializedName;
    private final int color;
    private final ResourceLocation sheet;

    Metal(int color)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.sheet = Helpers.identifier("block/metal/full/" + serializedName);
    }

    public ResourceLocation getSheet()
    {
        return sheet;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public int getColor()
    {
        return color;
    }

    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public Tier toolTier()
    {
        return TFCTiers.STEEL;
    }

    @Override
    public ArmorMaterial armorTier()
    {
        return TFCArmorMaterials.RED_STEEL;
    }

    @Override
    public net.dries007.tfc.util.Metal.Tier metalTier()
    {
        return net.dries007.tfc.util.Metal.Tier.TIER_VI;
    }
}
