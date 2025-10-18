package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.data.recipes.AlloyRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.AnvilRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.ArcFurnaceRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.BarrelRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.BlueprintRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.CastingRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.ChiselRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.ClocheFertilizerRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.ClocheRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.CokeOvenRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.CraftingRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.CrusherRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.FermenterRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.IECraftingRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.MetalPressRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.MineralMixRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.MixerRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.RefineryRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.SawmillRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.SqueezerRecipes;
import com.nmagpie.tfc_ie_addon.util.EmptyRecipe;
import com.nmagpie.tfc_ie_addon.data.recipes.GlassRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.HeatRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.QuernRecipes;
import com.nmagpie.tfc_ie_addon.data.recipes.WeldingRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import net.neoforged.neoforge.common.conditions.ICondition;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.registry.RegistryHolder;

@SuppressWarnings("NotNullFieldNotInitialized")
public class BuiltinRecipes extends RecipeProvider implements
    AlloyRecipes,
    AnvilRecipes,
    BarrelRecipes,
    CastingRecipes,
    ChiselRecipes,
    CraftingRecipes,
    GlassRecipes,
    HeatRecipes,
    QuernRecipes,
    WeldingRecipes,
    ArcFurnaceRecipes,
    BlueprintRecipes,
    ClocheRecipes,
    ClocheFertilizerRecipes,
    CokeOvenRecipes,
    CrusherRecipes,
    FermenterRecipes,
    MetalPressRecipes,
    MineralMixRecipes,
    MixerRecipes,
    RefineryRecipes,
    SawmillRecipes,
    SqueezerRecipes
{
    final CompletableFuture<?> before;

    PackOutput packOutput;
    CompletableFuture<HolderLookup.Provider> providerFuture;
    RecipeOutput output;
    HolderLookup.Provider lookup;

    public BuiltinRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<?> before, BuiltinItemHeat itemHeat)
    {
        super(output, lookup);
        this.packOutput = output;
        this.providerFuture = lookup;
        this.before = CompletableFuture.allOf(before, itemHeat.output());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider lookup)
    {
        this.lookup = lookup;
        return before.thenCompose(v -> super.run(output, lookup));
    }

    @Override
    public void buildRecipes(RecipeOutput output)
    {
        this.output = output;

        remove(
            "immersiveengineering:crafting/armor_faraday_chestplate",
            "immersiveengineering:crafting/armor_faraday_boots",
            "immersiveengineering:crafting/armor_faraday_helmet",
            "immersiveengineering:crafting/armor_faraday_leggings",

            "immersiveengineering:crafting/armor_steel_chestplate",
            "immersiveengineering:crafting/armor_steel_boots",
            "immersiveengineering:crafting/armor_steel_helmet",
            "immersiveengineering:crafting/armor_steel_leggings",

            "immersiveengineering:crafting/axe_steel",
            "immersiveengineering:crafting/sword_steel",
            "immersiveengineering:crafting/pickaxe_steel",
            "immersiveengineering:crafting/hoe_steel",
            "immersiveengineering:crafting/shovel_steel",

            "immersiveengineering:crafting/drillhead_steel",
            "immersiveengineering:crafting/drillhead_iron",

            "immersiveengineering:crafting/stick_aluminum",
            "immersiveengineering:crafting/stick_iron",
            "immersiveengineering:crafting/stick_steel",

            "immersiveengineering:crafting/toolupgrade_revolver_bayonet",
            "immersiveengineering:crafting/gunpart_barrel",

            "immersiveengineering:crafting/empty_casing",

            "immersiveengineering:crafting/gunpowder_from_dusts",

            "immersiveengineering:crusher/wool",

            "immersiveengineering:metalpress/rod_steel",

            "immersiveengineering:mineral/beryl",
            "immersiveengineering:mineral/cassiterite",

            "immersiveengineering:crafting/alloybrick",
            "immersiveengineering:crafting/blastbrick",
            "immersiveengineering:crafting/blastbrick_reinforced",

            "immersiveengineering:crafting/ingot_aluminum_to_storage_aluminum",
            "immersiveengineering:crafting/ingot_constantan_to_storage_constantan",
            "immersiveengineering:crafting/ingot_electrum_to_storage_electrum",
            "immersiveengineering:crafting/ingot_lead_to_storage_lead",
            "immersiveengineering:crafting/ingot_nickel_to_storage_nickel",
            "immersiveengineering:crafting/ingot_silver_to_storage_silver",
            "immersiveengineering:crafting/ingot_steel_to_storage_steel",
            "immersiveengineering:crafting/ingot_uranium_to_storage_uranium",

            "immersiveengineering:crafting/storage_steel_to_ingot_steel",
            "immersiveengineering:crafting/storage_uranium_to_ingot_uranium",

            "immersiveengineering:crafting/plate_aluminum_hammering",
            "immersiveengineering:crafting/plate_constantan_hammering",
            "immersiveengineering:crafting/plate_copper_hammering",
            "immersiveengineering:crafting/plate_electrum_hammering",
            "immersiveengineering:crafting/plate_gold_hammering",
            "immersiveengineering:crafting/plate_iron_hammering",
            "immersiveengineering:crafting/plate_lead_hammering",
            "immersiveengineering:crafting/plate_nickel_hammering",
            "immersiveengineering:crafting/plate_silver_hammering",
            "immersiveengineering:crafting/plate_steel_hammering",
            "immersiveengineering:crafting/plate_uranium_hammering",

            "immersiveengineering:crafting/treated_wood_horizontal",

            "immersiveengineering:arc_recycling_list"
        );

        alloyRecipes();
        anvilRecipes();
        barrelRecipes();
        castingRecipes();
        chiselRecipes();
        craftingRecipes();
        glassRecipes();
        heatRecipes();
        quernRecipes();
        weldingRecipes();

        arcFurnaceRecipes(output);
        blueprintRecipes(output);
        clocheRecipes(output);
        clocheFertilizerRecipes(output);
        cokeOvenRecipes(output);
        crusherRecipes(output);
        fermenterRecipes(output);
        metalPressRecipes(output);
        mineralMixRecipes(output);
        mixerRecipes(output);
        refineryRecipes(output);
        sawmillRecipes(output);
        squeezerRecipes(output);

        new IECraftingRecipes(packOutput, providerFuture).buildRecipes(output);

        // Collapse Recipes
        TFCBlocks.ROCK_BLOCKS.forEach((rock, blocks) -> {
            add(new CollapseRecipe(BlockIngredient.of(Stream.of(
                pivot(Blocks.ORES.get(rock), Ore.Grade.POOR).values()
            ).flatMap(Collection::stream).map(RegistryHolder::get)), blocks.get(Rock.BlockType.COBBLE).get().defaultBlockState()));
            Blocks.ORES.get(rock).forEach((ore, oreBlocks) -> {
                add(new CollapseRecipe(
                    BlockIngredient.of(oreBlocks.get(Ore.Grade.RICH).get()),
                    oreBlocks.get(Ore.Grade.NORMAL).get().defaultBlockState()));
                add(new CollapseRecipe(
                    BlockIngredient.of(oreBlocks.get(Ore.Grade.NORMAL).get()),
                    oreBlocks.get(Ore.Grade.POOR).get().defaultBlockState()));
            });
        });
    }

    @Override
    public HolderLookup.Provider lookup()
    {
        return lookup;
    }

    @Override
    public void add(String prefix, String name, Recipe<?> recipe, ICondition[] conditions)
    {
        output.accept(TFC_IE_Addon.identifier((prefix + "/" + name).toLowerCase(Locale.ROOT)), recipe, null, conditions);
    }

    @Override
    public void remove(String... names)
    {
        for (String name : names)
        {
            final ResourceLocation id = ResourceLocation.parse(name);
            output.accept(id, EmptyRecipe.INSTANCE, null, FalseCondition.INSTANCE);
        }
    }

    @Override
    public void replace(String name, Recipe<?> recipe)
    {
        final ResourceLocation id = ResourceLocation.parse(name);
        output.accept(id, recipe, null);
    }
}
