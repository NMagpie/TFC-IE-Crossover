package com.nmagpie.tfc_ie_addon.compat.patchouli.component;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentRenderContext;

public abstract class TriInputOutputComponent<T extends Recipe<?>> extends TriRecipeComponent<T>
{
    @Override
    public void render(PoseStack poseStack, IComponentRenderContext context, float partialTicks, int mouseX, int mouseY)
    {
        if (recipe == null || recipe2 == null || recipe3 == null) return;

        renderSetup(poseStack);

        GuiComponent.blit(poseStack, 9, 20, 0, 90, 98, 26, 256, 256);
        GuiComponent.blit(poseStack, 9, 65, 0, 90, 98, 26, 256, 256);
        GuiComponent.blit(poseStack, 9, 110, 0, 90, 98, 26, 256, 256);

        context.renderIngredient(poseStack, 14, 25, mouseX, mouseY, getIngredient(recipe));
        context.renderItemStack(poseStack, 86, 25, mouseX, mouseY, getOutput(recipe));

        context.renderIngredient(poseStack, 14, 70, mouseX, mouseY, getIngredient(recipe2));
        context.renderItemStack(poseStack, 86, 70, mouseX, mouseY, getOutput(recipe2));

        context.renderIngredient(poseStack, 14, 115, mouseX, mouseY, getIngredient(recipe3));
        context.renderItemStack(poseStack, 86, 115, mouseX, mouseY, getOutput(recipe3));

        poseStack.popPose();
    }

    // these methods take a recipe parameter to avoid the nullability of recipe
    abstract Ingredient getIngredient(T recipe);

    abstract ItemStack getOutput(T recipe);
}
