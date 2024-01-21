package com.nmagpie.tfc_ie_addon.common.items;

import java.util.List;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.common.items.DrillheadItem;
import com.google.common.base.CaseFormat;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;
import org.apache.commons.lang3.StringUtils;

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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        list.set(2, Component.translatable(Lib.DESC_FLAVOUR + "drillhead.level", getHarvestLevelName(getMiningLevel(stack))));
    }

    private static TagKey<Item> getIngotTagKey(String path)
    {
        return TagUtils.createItemWrapper(new ResourceLocation("forge", "ingots/" + path));
    }

    private static String getHarvestLevelName(Tier tier)
    {
        return StringUtils.join(
            StringUtils.splitByCharacterTypeCamelCase(
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, TierSortingRegistry.getName(tier).getPath())),
            " "
        );
    }
}