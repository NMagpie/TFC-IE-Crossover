package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IEApiDataComponents;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.tool.conveyor.IConveyorType;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.BasicConveyor;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.SplitConveyor;
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.google.common.collect.ImmutableMap;
import com.nmagpie.tfc_ie_addon.common.ModTags;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.data.Utils;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.AdvancedShapedRecipe;
import net.dries007.tfc.common.recipes.AdvancedShapelessRecipe;
import net.dries007.tfc.common.recipes.outputs.AddBaitToRodModifier;
import net.dries007.tfc.common.recipes.outputs.AddGlassModifier;
import net.dries007.tfc.common.recipes.outputs.AddPowderModifier;
import net.dries007.tfc.common.recipes.outputs.AddTraitModifier;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.CopyForgingBonusModifier;
import net.dries007.tfc.common.recipes.outputs.CopyInputModifier;
import net.dries007.tfc.common.recipes.outputs.CopyOldestFoodModifier;
import net.dries007.tfc.common.recipes.outputs.DamageCraftingRemainderModifier;
import net.dries007.tfc.common.recipes.outputs.ExtraProductModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;

public interface CraftingRecipes extends Recipes
{
    default void craftingRecipes()
    {
        replace("minecraft:iron_nugget")
            .input(getIronIngotsTag())
            .shapeless(net.minecraft.world.item.Items.IRON_NUGGET, 9);
        replace("minecraft:gold_nugget")
            .input(getGoldIngotsTag())
            .shapeless(net.minecraft.world.item.Items.GOLD_NUGGET, 9);
        replace("minecraft:iron_ingot_from_nuggets")
            .input('S', Tags.Items.NUGGETS_IRON)
            .pattern("SSS", "SSS", "SSS")
            .shaped(TFCItems.METAL_ITEMS.get(Metal.WROUGHT_IRON).get(Metal.ItemType.INGOT));
        replace("minecraft:gold_ingot_from_nuggets")
            .input('S', Tags.Items.NUGGETS_GOLD)
            .pattern("SSS", "SSS", "SSS")
            .shaped(TFCItems.METAL_ITEMS.get(Metal.GOLD).get(Metal.ItemType.INGOT));

        final ItemStack blueprintSpecialBullet = new ItemStack(IEItems.Misc.BLUEPRINT);
        blueprintSpecialBullet.set(IEApiDataComponents.BLUEPRINT_TYPE, "specialBullet");
        recipe("special_bullet")
            .input('E', IEItems.Ingredients.EMPTY_SHELL)
            .input('S', commonTagOf(Metal.BLUE_STEEL, Metal.ItemType.INGOT))
            .input('D', Tags.Items.DYES_BLUE)
            .input('P', net.minecraft.world.item.Items.PAPER)
            .pattern("SES", "DDD", "PPP")
            .shaped(blueprintSpecialBullet);

        recipe().to2x2(Items.TREATED_WOOD_LUMBER, IEBlocks.WoodenDecoration.TREATED_WOOD.get(TreatedWoodStyles.HORIZONTAL), 1);

        recipe("from_bauxite")
            .input(Items.POWDERS.get(IEOre.BAUXITE))
            .shapeless(net.minecraft.world.item.Items.RED_DYE);
        recipe("from_galena")
            .input(Items.POWDERS.get(IEOre.GALENA))
            .shapeless(net.minecraft.world.item.Items.BLACK_DYE);
        recipe("from_uraninite")
            .input(Items.POWDERS.get(IEOre.URANINITE))
            .shapeless(net.minecraft.world.item.Items.GREEN_DYE);

        for (IEMetal metal : IEMetal.values())
        {
            final var blocks = Blocks.METALS.get(metal);
            recipe()
                .input('S', ingredientOf(metal, IEMetal.ItemType.SHEET))
                .input('W', ItemTags.PLANKS)
                .input('H', TFCTags.Items.TOOLS_HAMMER)
                .pattern(" SH", "SWS", " S ")
                .damageInputs()
                .source(0, 2)
                .shaped(blocks.get(IEMetal.BlockType.BLOCK), 8);
            recipe()
                .input('B', ingredientOf(metal, IEMetal.BlockType.BLOCK))
                .pattern("BBB")
                .shaped(blocks.get(IEMetal.BlockType.BLOCK_SLAB), 6);
            recipe()
                .input('B', ingredientOf(metal, IEMetal.BlockType.BLOCK))
                .pattern("B  ", "BB ", "BBB")
                .shaped(blocks.get(IEMetal.BlockType.BLOCK_STAIRS), 8);
        }
        recipe("tfc")
            .input('g', IEItems.Ingredients.WOODEN_GRIP)
            .input('p', IEBlocks.MetalDevices.FLUID_PIPE)
            .input('h', IEBlocks.MetalDecoration.ENGINEERING_HEAVY)
            .input('t', IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.DRILL_WATERPROOF))
            .input('b', ingredientOf(TFCItems.BLUE_STEEL_BUCKET, TFCItems.RED_STEEL_BUCKET))
            .pattern(" tg", " hg", "pb ")
            .shaped(IEItems.Weapons.CHEMTHROWER);
        recipe("tfc")
            .input('i', IETags.getTagsFor(EnumMetals.IRON).plate)
            .input('b', ingredientOf(TFCItems.BLUE_STEEL_BUCKET, TFCItems.RED_STEEL_BUCKET))
            .pattern(" ii", "ibb", "ibb")
            .shaped(IEItems.Misc.JERRYCAN);
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private Builder recipe()
    {
        return new Builder((name, r) -> {
            if (name != null) add(name, r);
            else add(r);
        });
    }

    /**
     * @return A builder for a new recipe with a name inferred from the output, plus a suffix. The suffix should not start with an underscore.
     */
    private Builder recipe(String suffix)
    {
        return new Builder((name, r) -> {
            assert !suffix.startsWith("_") : "recipe(String suffix) shouldn't start with an '_', it is added for you!";
            assert name == null : "Cannot use a named recipe and recipe(String suffix) at the same time!";
            add(nameOf(r.getResultItem(lookup()).getItem()) + "_" + suffix, r);
        });
    }

    /**
     * @param modid check for mod loaded
     * @return A builder for a new recipe with a name inferred from the output.
     */
    private Builder recipeWithModLoadedCondition(String modid)
    {
        var condition = new ModLoadedCondition(modid);
        return new Builder((name, r) -> {
            if (name != null) add(name, r, condition);
            else add(r, condition);
        });
    }

    /**
     * @return A builder for a recipe that will replace a recipe at {@code name}.
     */
    private Builder replace(String name)
    {
        return new Builder((name1, r) -> {
            assert name1 == null : "Cannot used replace() with a named recipe!";
            replace(name, r);
        });
    }

    /**
     * A recipe builder capable of building shaped, shapeless recipes, optionally with both output and remainder features
     * of advanced shaped / shapeless recipes. It has some preliminary validations to ensure legal recipes are built
     */
    @SuppressWarnings({"unused", "SameParameterValue"})
    class Builder
    {
        final BiConsumer<String, Recipe<?>> onFinish;
        @Nullable String name = null;

        final List<ItemStackModifier> remainder = new ArrayList<>(); // For advanced recipes, remainder modifiers
        final List<ItemStackModifier> outputs = new ArrayList<>(); // For advanced recipes, output modifiers
        final NonNullList<Ingredient> ingredients = NonNullList.create(); // Shapeless recipes only
        final List<String> pattern = new ArrayList<>(); // Shaped recipes only
        final ImmutableMap.Builder<Character, Ingredient> keys = ImmutableMap.builder();
        int inputRow = 0, inputCol = 0;
        @Nullable Ingredient primaryInput = null;
        boolean needsAdvInput = false, hasAdvInputShaped = false, hasAdvInputShapeless = false;

        Builder(BiConsumer<String, Recipe<?>> onFinish)
        {
            this.onFinish = onFinish;
        }

        void useTool(TagKey<Item> tool, ItemLike input, ItemLike output)
        {
            input(input).inputIsPrimary(tool).damageInputs().shapeless(output);
        }

        void useTool(TagKey<Item> tool, Ingredient input, ItemLike output, int count)
        {
            input(input).inputIsPrimary(tool).damageInputs().shapeless(output, count);
        }

        void bricksWithMortar(ItemLike brick, ItemLike bricks, int count)
        {
            input('Y', TFCItems.MORTAR).input('X', brick).pattern("XYX", "YXY", "XYX").shaped(bricks, count);
        }

        void to3x3(Ingredient input, ItemLike storage)
        {
            input('X', input).pattern("XXX", "XXX", "XXX").shaped(storage);
        }

        void from3x3(Ingredient input, ItemLike item)
        {
            input(input).shapeless(item, 9);
        }

        void to2x2(ItemLike input, ItemLike output, int count)
        {
            input('X', input).pattern("XX", "XX").shaped(output, count);
        }

        Builder damageInputs()
        {
            remainder.add(DamageCraftingRemainderModifier.INSTANCE);
            return this;
        }

        Builder copyOldestFood()
        {
            outputs.add(CopyOldestFoodModifier.INSTANCE);
            return this;
        }

        Builder copyFood()
        {
            outputs.add(CopyFoodModifier.INSTANCE);
            return this;
        }

        Builder copyForging()
        {
            needsAdvInput = true;
            return addOutputModifier(CopyForgingBonusModifier.INSTANCE);
        }

        Builder copyInput()
        {
            needsAdvInput = true;
            return addOutputModifier(CopyInputModifier.INSTANCE);
        }

        Builder addGlass()
        {
            needsAdvInput = true;
            return addOutputModifier(AddGlassModifier.INSTANCE);
        }

        Builder addPowder() {return addOutputModifier(AddPowderModifier.INSTANCE);}

        Builder addBait() {return addOutputModifier(AddBaitToRodModifier.INSTANCE);}

        Builder extraProduct(ItemLike item) {return extraProduct(item, 1);}

        Builder extraProduct(ItemLike item, int count)
        {
            remainder.add(new ExtraProductModifier(new ItemStack(item, count)));
            return this;
        }

        Builder addTrait(Holder<FoodTrait> trait) {return addOutputModifier(AddTraitModifier.of(trait));}

        Builder addOutputModifier(ItemStackModifier modifier)
        {
            outputs.add(modifier);
            return this;
        }

        Builder input(ItemLike item) {return input(item, 1);}

        Builder input(ItemLike item, int count) {return input(Ingredient.of(item), count);}

        Builder input(TagKey<Item> item) {return input(item, 1);}

        Builder input(TagKey<Item> item, int count) {return input(Ingredient.of(item), count);}

        Builder input(Ingredient item) {return input(item, 1);}

        Builder input(Ingredient item, int count)
        {
            for (int n = 0; n < count; n++) ingredients.add(item);
            return this;
        }

        Builder inputIsPrimary(ItemLike item) {return inputIsPrimary(Ingredient.of(item));}

        Builder inputIsPrimary(TagKey<Item> item) {return inputIsPrimary(Ingredient.of(item));}

        Builder inputIsPrimary(Ingredient item)
        {
            primaryInput = item;
            hasAdvInputShapeless = true;
            return input(item);
        }

        Builder input(char key, TagKey<Item> input) {return input(key, Ingredient.of(input));}

        Builder input(char key, ItemLike input) {return input(key, Ingredient.of(input));}

        Builder input(char key, Ingredient input)
        {
            keys.put(key, input);
            return this;
        }

        Builder source(int row, int col)
        {
            inputRow = row;
            inputCol = col;
            hasAdvInputShaped = true;
            return this;
        }

        Builder pattern(String... pattern)
        {
            this.pattern.addAll(List.of(pattern));
            return this;
        }

        void shapeless(String name)
        {
            this.name = name;
            shapeless(ItemStack.EMPTY);
        }

        void shapeless(ItemLike output) {shapeless(output, 1);}

        void shapeless(ItemLike output, int count) {shapeless(new ItemStack(output, count));}

        void shapeless(ItemStack output)
        {
            assert pattern.isEmpty() && keys.build().isEmpty() : "Mixing shaped and shapeless recipes";
            assert hasAdvInputShapeless || !needsAdvInput : "Missing a .inputIsPrimary(Ingredient) for a recipe which depends on input";
            assert !outputs.isEmpty() || !output.isEmpty() : "Either non-empty output, or output modifiers must be present";

            onFinish.accept(name, isAdvanced()
                ? new AdvancedShapelessRecipe(ingredients, ItemStackProvider.of(output, outputs), remainder(), Optional.ofNullable(primaryInput))
                : new ShapelessRecipe("", CraftingBookCategory.MISC, output, ingredients));
        }

        void shaped(String name)
        {
            this.name = name;
            shaped(ItemStack.EMPTY);
        }

        void shaped(ItemLike output) {shaped(output, 1);}

        void shaped(ItemLike output, int count) {shaped(new ItemStack(output, count));}

        void shaped(ItemStack output)
        {
            assert ingredients.isEmpty() : "Mixing shaped and shapeless recipes";
            assert hasAdvInputShaped || !needsAdvInput : "Missing a .source(int, int) for a recipe which depends on input";
            assert !outputs.isEmpty() || !output.isEmpty() : "Either non-empty output, or output modifiers must be present";

            final ShapedRecipePattern pattern = ShapedRecipePattern.of(keys.build(), this.pattern);
            onFinish.accept(name, isAdvanced()
                ? new AdvancedShapedRecipe(pattern, true, ItemStackProvider.of(output, outputs), remainder(), inputRow, inputCol)
                : new ShapedRecipe("", CraftingBookCategory.MISC, pattern, output));
        }

        private Optional<ItemStackProvider> remainder()
        {
            return remainder.isEmpty() ? Optional.empty() : Optional.of(ItemStackProvider.of(ItemStack.EMPTY, remainder));
        }

        private boolean isAdvanced()
        {
            return !remainder.isEmpty() || !outputs.isEmpty();
        }
    }
}
