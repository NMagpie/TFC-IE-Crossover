package com.nmagpie.tfc_ie_addon.common.blocks;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blocks.FluidCauldronBlock;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;

@SuppressWarnings("unused")
public class Blocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFC_IE_Addon.MOD_ID);
    public static final RegistryObject<AmethystBlock> QUARTZ_BLOCK = register("mineral/quartz_block", () -> new AmethystBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(1.5F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<BuddingQuartzBlock> BUDDING_QUARTZ = register("mineral/budding_quartz", () -> new BuddingQuartzBlock(Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).randomTicks().strength(1.5F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final RegistryObject<AmethystClusterBlock> QUARTZ_CLUSTER = register("mineral/quartz_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion().randomTicks().sound(SoundType.GLASS).strength(1.5F).lightLevel(light -> 5)));
    public static final RegistryObject<AmethystClusterBlock> LARGE_QUARTZ_BUD = register("mineral/large_quartz_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.GLASS).lightLevel(light -> 4)));
    public static final RegistryObject<AmethystClusterBlock> MEDIUM_QUARTZ_BUD = register("mineral/medium_quartz_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.GLASS).lightLevel(light -> 2)));
    public static final RegistryObject<AmethystClusterBlock> SMALL_QUARTZ_BUD = register("mineral/small_quartz_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.GLASS).lightLevel(light -> 1)));

    public static final RegistryObject<Block> SMALL_BAUXITE = register("ore/small_bauxite", () -> GroundcoverBlock.looseOre(Properties.of().mapColor(MapColor.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission().pushReaction(PushReaction.DESTROY)));

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> BAUXITE_ORES = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(Ore.Grade.class, grade ->
            register(("ore/" + grade.name() + "_bauxite" + "/" + rock.name()), () -> new Block(Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
        )
    );

    public static final RegistryObject<Block> SMALL_GALENA = register("ore/small_galena", () -> GroundcoverBlock.looseOre(Properties.of().mapColor(MapColor.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission().pushReaction(PushReaction.DESTROY)));

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> GALENA_ORES = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(Ore.Grade.class, grade ->
            register(("ore/" + grade.name() + "_galena" + "/" + rock.name()), () -> new Block(Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
        )
    );

    public static final RegistryObject<Block> SMALL_URANINITE = register("ore/small_uraninite", () -> GroundcoverBlock.looseOre(Properties.of().mapColor(MapColor.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission().pushReaction(PushReaction.DESTROY)));

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> URANINITE_ORES = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(Ore.Grade.class, grade ->
            register(("ore/" + grade.name() + "_uraninite" + "/" + rock.name()), () -> new Block(Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
        )
    );

    public static final Map<IEMetal, Map<IEMetal.BlockType, RegistryObject<Block>>> METALS = Helpers.mapOfKeys(IEMetal.class, metal ->
        Helpers.mapOfKeys(IEMetal.BlockType.class, type ->
            register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
        )
    );

    public static final Map<IEMetal, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(IEMetal.class, metal ->
        registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(Fluids.METALS.get(metal).source(), Properties.copy(net.minecraft.world.level.block.Blocks.LAVA).noLootTable()))
    );

    public static final Map<IEMetal, RegistryObject<FluidCauldronBlock>> METAL_CAULDRONS = Helpers.mapOfKeys(IEMetal.class, metal ->
        registerNoItem("cauldron/metal/" + metal.name(), () -> new FluidCauldronBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.CAULDRON)))
    );

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(BLOCKS, Items.ITEMS, name, blockSupplier, blockItemFactory);
    }

}
