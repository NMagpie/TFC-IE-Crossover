package com.nmagpie.tfc_ie_addon.util;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;

public class RegisteredSoils
{
    public static void registerTFCSoils()
    {
        for (SoilBlockType.Variant soil : SoilBlockType.Variant.values())
        {
            String soilName = soil.name().toLowerCase();

            ResourceLocation rl = new ResourceLocation(TerraFirmaCraft.MOD_ID, "block/farmland/" + soilName);

            ClocheRecipe.registerSoilTexture(Ingredient.of(new ItemStack(soil.getBlock(SoilBlockType.DIRT).get().asItem())), rl);
        }
    }
}
