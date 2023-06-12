package com.nmagpie.tfc_ie_addon.common.util;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class Registered_Soils {

    public static void register_tfc_soils() {

        for (SoilBlockType.Variant soil : SoilBlockType.Variant.values()) {

            String soil_name = soil.name().toLowerCase();

            ResourceLocation rl = new ResourceLocation(TerraFirmaCraft.MOD_ID, "block/farmland/" + soil_name);

            ClocheRecipe.registerSoilTexture(Ingredient.of(new ItemStack(soil.getBlock(SoilBlockType.DIRT).get().asItem())), rl);
        }

    }

}
