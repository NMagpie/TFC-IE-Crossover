package com.nmagpie.tfc_ie_addon.compat.patchouli.component;

import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentRenderContext;

import net.dries007.tfc.compat.patchouli.PatchouliIntegration;

public abstract class TriInputOutputComponent<T extends Recipe<?>> extends TriRecipeComponent<T>
{
    @Override
    public void render(@Nonnull GuiGraphics graphics, @Nonnull IComponentRenderContext context, float partialTicks, int mouseX, int mouseY)
    {
        if (recipe == null || recipe2 == null || recipe3 == null) return;

        renderSetup(graphics);

        graphics.blit(PatchouliIntegration.TEXTURE, 9, 20, 0, 90, 98, 26, 256, 256);
        graphics.blit(PatchouliIntegration.TEXTURE, 9, 65, 0, 90, 98, 26, 256, 256);
        graphics.blit(PatchouliIntegration.TEXTURE, 9, 110, 0, 90, 98, 26, 256, 256);

        context.renderIngredient(graphics, 14, 25, mouseX, mouseY, getIngredient(recipe));
        context.renderItemStack(graphics, 86, 25, mouseX, mouseY, getOutput(recipe));

        context.renderIngredient(graphics, 14, 70, mouseX, mouseY, getIngredient(recipe2));
        context.renderItemStack(graphics, 86, 70, mouseX, mouseY, getOutput(recipe2));

        context.renderIngredient(graphics, 14, 115, mouseX, mouseY, getIngredient(recipe3));
        context.renderItemStack(graphics, 86, 115, mouseX, mouseY, getOutput(recipe3));

        graphics.pose().popPose();
    }

    // these methods take a recipe parameter to avoid the nullability of recipe
    abstract Ingredient getIngredient(T recipe);

    abstract ItemStack getOutput(T recipe);
}
