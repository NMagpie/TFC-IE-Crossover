package com.nmagpie.tfc_ie_addon.compat.patchouli.component;

import java.util.function.UnaryOperator;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import vazkii.patchouli.api.IVariable;

import net.dries007.tfc.compat.patchouli.component.CustomComponent;

public abstract class TriRecipeComponent<T extends Recipe<?>> extends CustomComponent
{
    @Nullable protected transient T recipe, recipe2, recipe3;
    @SerializedName("recipe") String recipeName;
    @SerializedName("recipe2") String recipeName2;
    @SerializedName("recipe3") String recipeName3;

    @Override
    public void build(int componentX, int componentY, int pageNum)
    {
        super.build(componentX, componentY, pageNum);

        recipe = asRecipe(recipeName, getRecipeType()).orElse(null);
        recipe2 = asRecipe(recipeName2, getRecipeType()).orElse(null);
        recipe3 = asRecipe(recipeName3, getRecipeType()).orElse(null);
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup)
    {
        recipeName = lookup.apply(IVariable.wrap(recipeName)).asString();
        recipeName2 = lookup.apply(IVariable.wrap(recipeName2)).asString();
        recipeName3 = lookup.apply(IVariable.wrap(recipeName3)).asString();
    }

    protected abstract RecipeType<T> getRecipeType();
}
