package com.nmagpie.tfc_ie_addon.common.items;

import java.util.List;
import java.util.Locale;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.common.items.DrillheadItem;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import net.dries007.tfc.common.TFCTiers;

public class Drillhead extends DrillheadItem
{
    public static final DrillHeadPerm BLACK_STEEL = new DrillHeadPerm("black_steel", getIngotTagKey("black_steel"), 3, 1, TFCTiers.BLACK_STEEL, 12, 8, 16000, TFC_IE_Addon.identifier("item/drill_black_steel"));
    public static final DrillHeadPerm BLUE_STEEL = new DrillHeadPerm("blue_steel", getIngotTagKey("blue_steel"), 5, 1, TFCTiers.BLUE_STEEL, 15, 10, 20000, TFC_IE_Addon.identifier("item/drill_blue_steel"));
    public static final DrillHeadPerm RED_STEEL = new DrillHeadPerm("red_steel", getIngotTagKey("red_steel"), 5, 1, TFCTiers.RED_STEEL, 15, 10, 20000, TFC_IE_Addon.identifier("item/drill_red_steel"));

    public Drillhead(DrillHeadPerm perms)
    {
        super(perms);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, ctx, list, flag);
        list.set(2, Component.translatable(Lib.DESC_FLAVOUR + "drillhead.level", getHarvestLevelName(getMiningLevel(stack))).withStyle(ChatFormatting.GRAY));
    }

    private static TagKey<Item> getIngotTagKey(String path)
    {
        return TagUtils.createItemWrapper(ResourceLocation.fromNamespaceAndPath("c", "ingots/" + path));
    }

    private static String getHarvestLevelName(Tier tier)
    {
        return tier.toString().toUpperCase(Locale.ROOT).replaceAll("_", " ");
    }
}