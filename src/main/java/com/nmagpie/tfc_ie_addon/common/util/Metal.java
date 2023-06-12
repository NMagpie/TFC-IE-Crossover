package com.nmagpie.tfc_ie_addon.common.util;

import com.nmagpie.tfc_ie_addon.common.Helpers;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.util.NonNullFunction;

import java.util.Locale;

public enum Metal implements RegistryMetal {
    ELECTRUM(
            0xFCB74A,
            Rarity.EPIC,
            TFCTiers.WROUGHT_IRON,
            TFCArmorMaterials.WROUGHT_IRON,
            net.dries007.tfc.util.Metal.Tier.TIER_IV),

    CONSTANTAN(
            0xEC8068,
            Rarity.EPIC,
            TFCTiers.STEEL,
            TFCArmorMaterials.STEEL,
            net.dries007.tfc.util.Metal.Tier.TIER_IV),

    ALUMINUM(
            0xCCC1BC,
            Rarity.COMMON,
            TFCTiers.BRONZE,
            TFCArmorMaterials.BRONZE,
            net.dries007.tfc.util.Metal.Tier.TIER_I),

    LEAD(
            0x433F4D,
            Rarity.RARE,
            TFCTiers.BLACK_BRONZE,
            TFCArmorMaterials.WROUGHT_IRON,
            net.dries007.tfc.util.Metal.Tier.TIER_III),

    URANIUM(0x738A6C,
            Rarity.EPIC,
            TFCTiers.STEEL,
            TFCArmorMaterials.STEEL,
            net.dries007.tfc.util.Metal.Tier.TIER_IV);

    private final String serializedName;
    private final int color;
    private final ResourceLocation sheet;

    private final Rarity rarity;

    private final Tier toolTier;

    private final ArmorMaterial armorTier;

    private final net.dries007.tfc.util.Metal.Tier metalTier;

    Metal(int color, Rarity rarity, Tier toolTier, ArmorMaterial armorTier, net.dries007.tfc.util.Metal.Tier metalTier) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.color = color;
        this.sheet = Helpers.identifier("block/metal/full/" + serializedName);

        this.rarity = rarity;
        this.toolTier = toolTier;
        this.armorTier = armorTier;
        this.metalTier = metalTier;
    }

    public ResourceLocation getSheet() {
        return sheet;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public int getColor() {
        return color;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public Tier toolTier() {
        return toolTier;
    }

    @Override
    public ArmorMaterial armorTier() {
        return armorTier;
    }

    @Override
    public net.dries007.tfc.util.Metal.Tier metalTier() {
        return metalTier;
    }

    public enum ItemType {

        SHEET(metal -> new Item(new Item.Properties().tab(TFCItemGroup.METAL))),
        DOUBLE_INGOT(metal -> new Item(new Item.Properties().tab(TFCItemGroup.METAL)));

        private final NonNullFunction<Metal, Item> itemFactory;

        ItemType(NonNullFunction<Metal, Item> itemFactory) {
            this.itemFactory = itemFactory;
        }

        public Item create(Metal metal) {
            return itemFactory.apply(metal);
        }
    }
}
