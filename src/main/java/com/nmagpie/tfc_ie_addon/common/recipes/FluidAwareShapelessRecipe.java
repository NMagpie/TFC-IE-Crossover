package com.nmagpie.tfc_ie_addon.common.recipes;

import blusunrize.immersiveengineering.common.crafting.fluidaware.IngredientFluidStack;
import blusunrize.immersiveengineering.common.crafting.fluidaware.ShapelessFluidAwareRecipe;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class FluidAwareShapelessRecipe extends ShapelessFluidAwareRecipe
{

    public FluidAwareShapelessRecipe(ResourceLocation idIn, String groupIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
    {
        super(idIn, groupIn, recipeItemsIn, recipeOutputIn);
    }

    public FluidAwareShapelessRecipe(ShapelessRecipe in)
    {
        this(in.getId(), in.getGroup(), in.getIngredients(), in.getResultItem());
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv)
    {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        final MatchLocation offset = findMatch(inv);

        if (offset == null)
            return super.getRemainingItems(inv);

        for (int x = 0; x < inv.getWidth(); ++x)
            for (int y = 0; y < inv.getHeight(); ++y)
            {
                final int invIndex = getInventoryIndex(inv, x, y);
                final int ingrIndex = offset.getListIndex(x, y);
                if (ingrIndex >= 0 && ingrIndex < getIngredients().size())
                {
                    Ingredient ingr = getIngredients().get(ingrIndex);
                    final ItemStack item = inv.getItem(invIndex);
                    ItemStack result = ItemStack.EMPTY;
                    if (ingr instanceof IngredientFluidStack)
                        result = ((IngredientFluidStack) ingr).getExtractedStack(item.copy());
                    else if (ingr instanceof AddonIngredientFluidStack)
                        result = ((AddonIngredientFluidStack) ingr).getExtractedStack(item.copy());
                    else if (item.hasContainerItem())
                        result = item.getContainerItem();
                    if (result == item)
                        result = result.copy();
                    remaining.set(invIndex, result);
                }
            }

        return remaining;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return RecipeSerializers.SHAPELESS_SERIALIZER.get();
    }

    private int getInventoryIndex(CraftingContainer inv, int x, int y)
    {
        return x + y * inv.getWidth();
    }
}
