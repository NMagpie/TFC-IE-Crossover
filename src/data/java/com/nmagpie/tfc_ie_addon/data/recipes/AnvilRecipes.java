package com.nmagpie.tfc_ie_addon.data.recipes;

import java.util.List;
import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.data.Utils;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;

import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;

import static net.dries007.tfc.common.component.forge.ForgeRule.*;

@SuppressWarnings("SameParameterValue")
public interface AnvilRecipes extends Recipes
{
    /**
     * Forge rules are entered in the order they are displayed in the anvil.
     * As such, they should be entered in a valid order of hits.
     * Ex. if the steps are BEND_THIRD_LAST, HIT_SECOND_LAST, PUNCH_LAST, they should be entered in that order.
     * Ex. if the steps are BEND_ANY, HIT_ANY, PUNCH_LAST, they can be entered in that order, or as HIT_ANY, BEND_ANY, PUNCH_LAST
     */
    default void anvilRecipes()
    {
        final ForgeRule[] TRIPLE_HITS = {HIT_THIRD_LAST, HIT_SECOND_LAST, HIT_LAST};
        final ForgeRule[] DRILLHEAD_RULES = {BEND_THIRD_LAST, DRAW_SECOND_LAST, HIT_LAST};

        for (var entry : Utils.METAL_MAP.entrySet())
            anvil(Ingredient.of(ingotTagOf(entry.getKey())), ItemStackProvider.of(IEItems.Metals.PLATES.get(entry.getValue())), entry.getKey().tier(), false, TRIPLE_HITS);
        for (var entry : Utils.IE_METAL_MAP.entrySet())
            anvil(Ingredient.of(ingotTagOf(entry.getKey())), ItemStackProvider.of(IEItems.Metals.PLATES.get(entry.getValue())), entry.getKey().tier().level(), false, TRIPLE_HITS);
        for (IEMetal metal : IEMetal.values())
            anvil(metal, IEMetal.ItemType.DOUBLE_INGOT, IEMetal.ItemType.SHEET, 1, TRIPLE_HITS);
        for (ArmorItem.Type type : ArmorItem.Type.values())
            if (type != ArmorItem.Type.BODY)
                anvil(ingredientOf(Metal.STEEL, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(IEItems.Misc.FARADAY_SUIT.get(type).get()), 4, true, PUNCH_NOT_LAST, HIT_ANY, DRAW_ANY);
        anvil(Ingredient.of(ingotTagOf(IEMetal.ALUMINUM)), ItemStackProvider.of(IEItems.Ingredients.STICK_ALUMINUM.get(), 2), 3, false, DRAW_THIRD_LAST, DRAW_SECOND_LAST, BEND_LAST);
        anvil(ingredientOf(Metal.WROUGHT_IRON, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(IEItems.Tools.DRILLHEAD_IRON), 3, false, DRILLHEAD_RULES);
        anvil(ingredientOf(Metal.STEEL, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(IEItems.Tools.DRILLHEAD_STEEL), 4, false, DRILLHEAD_RULES);
        anvil(ingredientOf(Metal.BLACK_STEEL, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(Items.DRILLHEAD_BLACK_STEEL), 5, false, DRILLHEAD_RULES);
        anvil(ingredientOf(Metal.BLUE_STEEL, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(Items.DRILLHEAD_BLUE_STEEL), 6, false, DRILLHEAD_RULES);
        anvil(ingredientOf(Metal.RED_STEEL, Metal.ItemType.DOUBLE_SHEET), ItemStackProvider.of(Items.DRILLHEAD_RED_STEEL), 6, false, DRILLHEAD_RULES);
        anvil(ingredientOf(Metal.COPPER, Metal.ItemType.INGOT), ItemStackProvider.of(IEItems.Ingredients.EMPTY_CASING, 2), 1, false, PUNCH_ANY, UPSET_ANY, SHRINK_LAST);
        anvil(ingredientOf(Metal.STEEL, Metal.ItemType.INGOT), ItemStackProvider.of(IEItems.Ingredients.GUNPART_BARREL), 4, false, SHRINK_ANY, HIT_ANY, DRAW_LAST);
        anvil(ingredientOf(Metal.STEEL, Metal.ItemType.INGOT), ItemStackProvider.of(IEItems.Misc.TOOL_UPGRADES.get(ToolUpgrade.REVOLVER_BAYONET)), 4, false, DRAW_ANY, BEND_ANY, HIT_LAST);
        anvil(ingredientOf(Metal.WROUGHT_IRON, Metal.ItemType.INGOT), ItemStackProvider.of(Items.HAMMER_HEAD), 3, false, HIT_THIRD_LAST, DRAW_SECOND_LAST, UPSET_LAST);
        anvil(ingredientOf(Metal.WROUGHT_IRON, Metal.ItemType.INGOT), ItemStackProvider.of(Items.WIRECUTTER_HEAD), 3, false, BEND_NOT_LAST, SHRINK_NOT_LAST, HIT_LAST);
    }

    private void anvil(IEMetal metal, IEMetal.ItemType inputType, IEMetal.ItemType outputType, int amount, ForgeRule... rules)
    {
        anvil(ingredientOf(metal, inputType), ItemStackProvider.of(Items.METAL_ITEMS.get(metal).get(outputType), amount), metal.tier().level(), false, rules);
    }

    private void anvil(Ingredient input, ItemStackProvider output, int minTier, boolean applyForgingBonus, ForgeRule... rules)
    {
        add(new AnvilRecipe(input, minTier, List.of(rules), applyForgingBonus, output));
    }
}
