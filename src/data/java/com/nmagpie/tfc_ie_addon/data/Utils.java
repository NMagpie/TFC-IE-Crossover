package com.nmagpie.tfc_ie_addon.data;

import java.util.Map;
import blusunrize.immersiveengineering.api.EnumMetals;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;

public class Utils
{
    public static final Map<IEMetal, EnumMetals> IE_METAL_MAP = Map.of(
        IEMetal.ELECTRUM, EnumMetals.ELECTRUM,
        IEMetal.CONSTANTAN, EnumMetals.CONSTANTAN,
        IEMetal.ALUMINUM, EnumMetals.ALUMINUM,
        IEMetal.LEAD, EnumMetals.LEAD,
        IEMetal.URANIUM, EnumMetals.URANIUM
    );

    public static final Map<Metal, EnumMetals> METAL_MAP = Map.of(
        Metal.COPPER, EnumMetals.COPPER,
        Metal.GOLD, EnumMetals.GOLD,
        Metal.WROUGHT_IRON, EnumMetals.IRON,
        Metal.SILVER, EnumMetals.SILVER,
        Metal.NICKEL, EnumMetals.NICKEL,
        Metal.STEEL, EnumMetals.STEEL
    );

    public static final Map<Crop, ? extends ItemLike> CROP_MAP = Map.ofEntries(
        Map.entry(Crop.CASSAVA, TFCItems.FOOD.get(Food.CASSAVA)),
        Map.entry(Crop.GREEN_BEAN, TFCItems.FOOD.get(Food.GREEN_BEAN)),
        Map.entry(Crop.LENTIL, TFCItems.FOOD.get(Food.LENTIL)),
        Map.entry(Crop.PEANUT, TFCItems.FOOD.get(Food.PEANUT)),
        Map.entry(Crop.SOYBEAN, TFCItems.FOOD.get(Food.SOYBEAN)),
        Map.entry(Crop.BARLEY, TFCItems.FOOD.get(Food.BARLEY)),
        Map.entry(Crop.OAT, TFCItems.FOOD.get(Food.OAT)),
        Map.entry(Crop.RYE, TFCItems.FOOD.get(Food.RYE)),
        Map.entry(Crop.MAIZE, TFCItems.FOOD.get(Food.MAIZE)),
        Map.entry(Crop.WHEAT, TFCItems.FOOD.get(Food.WHEAT)),
        Map.entry(Crop.RICE, TFCItems.FOOD.get(Food.RICE)),
        Map.entry(Crop.BEET, TFCItems.FOOD.get(Food.BEET)),
        Map.entry(Crop.CABBAGE, TFCItems.FOOD.get(Food.CABBAGE)),
        Map.entry(Crop.CARROT, TFCItems.FOOD.get(Food.CARROT)),
        Map.entry(Crop.GARLIC, TFCItems.FOOD.get(Food.GARLIC)),
        Map.entry(Crop.POTATO, TFCItems.FOOD.get(Food.POTATO)),
        Map.entry(Crop.ONION, TFCItems.FOOD.get(Food.ONION)),
        Map.entry(Crop.SQUASH, TFCItems.FOOD.get(Food.SQUASH)),
        Map.entry(Crop.TOMATO, TFCItems.FOOD.get(Food.TOMATO)),
        Map.entry(Crop.RED_BELL_PEPPER, TFCItems.FOOD.get(Food.RED_BELL_PEPPER)),
        Map.entry(Crop.YELLOW_BELL_PEPPER, TFCItems.FOOD.get(Food.YELLOW_BELL_PEPPER)),
        Map.entry(Crop.PUMPKIN, TFCBlocks.PUMPKIN),
        Map.entry(Crop.MELON, TFCBlocks.MELON),
        Map.entry(Crop.ALFALFA, TFCItems.ALFALFA),
        Map.entry(Crop.CANOLA, TFCItems.CANOLA),
        Map.entry(Crop.RADISH, TFCItems.FOOD.get(Food.RADISH)),
        Map.entry(Crop.JUTE, TFCItems.JUTE),
        Map.entry(Crop.SUGARCANE, TFCItems.FOOD.get(Food.SUGARCANE)),
        Map.entry(Crop.PAPYRUS, TFCItems.PAPYRUS)
    );
}
