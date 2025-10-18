package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.Objects;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;

public interface Recipes extends Accessors
{
    HolderLookup.Provider lookup();

    default String nameOf(Recipe<?> recipe)
    {
        return nameOf(recipe.getResultItem(lookup()).getItem());
    }

    default void add(Recipe<?> recipe)
    {
        add(nameOf(recipe), recipe);
    }

    default void add(Recipe<?> recipe, ICondition... conditions)
    {
        add(nameOf(recipe), recipe, conditions);
    }

    default void add(String name, Recipe<?> recipe)
    {
        add(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()), "No recipe type").getPath(), name, recipe);
    }

    default void add(String name, Recipe<?> recipe, ICondition... conditions)
    {
        add(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()), "No recipe type").getPath(), name, recipe, conditions);
    }

    default void add(String prefix, String name, Recipe<?> recipe)
    {
        add(prefix, name, recipe, new ICondition[0]);
    }

    void add(String prefix, String name, Recipe<?> recipe, ICondition... conditions);

    void remove(String... names);

    void replace(String name, Recipe<?> recipe);
}
