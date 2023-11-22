package com.nmagpie.tfc_ie_addon.mixin;

import java.util.Iterator;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.dries007.tfc.common.recipes.GlassworkingRecipe;
import net.dries007.tfc.compat.jei.category.GlassworkingRecipeCategory;

@Mixin(GlassworkingRecipeCategory.class)
public abstract class GlassworkingRecipeCategoryMixin
{
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lnet/dries007/tfc/common/recipes/GlassworkingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    public void setRecipe(IRecipeLayoutBuilder builder, GlassworkingRecipe recipe, IFocusGroup focuses, CallbackInfo ci, ItemStack result, int idx, Iterator<GlassOperation> var12, GlassOperation operation, IRecipeSlotBuilder slot)
    {
        if (GlassOperation.valueOf("LEAD") == operation)
            slot.addItemStack(new ItemStack(Items.GALENA_POWDER.get()));
        if (GlassOperation.valueOf("URANIUM") == operation)
            slot.addItemStack(new ItemStack(Items.URANINITE_POWDER.get()));
    }
}
