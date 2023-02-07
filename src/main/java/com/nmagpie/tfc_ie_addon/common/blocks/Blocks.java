package com.nmagpie.tfc_ie_addon.common.blocks;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.common.util.Metal;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.TFCMaterials;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.dries007.tfc.common.TFCItemGroup.ORES;
import static net.dries007.tfc.common.TFCItemGroup.ROCK_STUFFS;

@SuppressWarnings("unused")
public class Blocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFC_IE_Addon.MOD_ID);
    public static final RegistryObject<AmethystBlock> QUARTZ_BLOCK = register("mineral/quartz_block", () -> new AmethystBlock(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_LIGHT_GRAY).strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()), ROCK_STUFFS);
    public static final RegistryObject<BuddingQuartzBlock> BUDDING_QUARTZ = register("mineral/budding_quartz", () -> new BuddingQuartzBlock(Properties.of(Material.AMETHYST).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()), ROCK_STUFFS);
    public static final RegistryObject<AmethystClusterBlock> QUARTZ_CLUSTER = register("mineral/quartz_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel((p_152632_) -> {
        return 5;
    })), ROCK_STUFFS);
    public static final RegistryObject<AmethystClusterBlock> LARGE_QUARTZ_BUD = register("mineral/large_quartz_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel((p_152629_) -> {
        return 4;
    })), ROCK_STUFFS);
    public static final RegistryObject<AmethystClusterBlock> MEDIUM_QUARTZ_BUD = register("mineral/medium_quartz_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel((p_152617_) -> {
        return 2;
    })), ROCK_STUFFS);
    public static final RegistryObject<AmethystClusterBlock> SMALL_QUARTZ_BUD = register("mineral/small_quartz_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(QUARTZ_CLUSTER.get()).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel((p_187409_) -> {
        return 1;
    })), ROCK_STUFFS);

    public static final RegistryObject<Block> SMALL_ALUMINUM = register("ore/small_aluminum", () -> GroundcoverBlock.looseOre(Properties.of(Material.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission()), ORES);

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> ALUMINUM_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                    register(("ore/" + grade.name() + "_aluminum" + "/" + rock.name()), () -> new Block(Properties.of(Material.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()), TFCItemGroup.ORES)
            )
    );

    public static final RegistryObject<Block> SMALL_LEAD = register("ore/small_lead", () -> GroundcoverBlock.looseOre(Properties.of(Material.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission()), ORES);

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> LEAD_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                    register(("ore/" + grade.name() + "_lead" + "/" + rock.name()), () -> new Block(Properties.of(Material.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()), TFCItemGroup.ORES)
            )
    );

    public static final RegistryObject<Block> SMALL_URANIUM = register("ore/small_uranium", () -> GroundcoverBlock.looseOre(Properties.of(Material.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission()), ORES);

    public static final Map<Rock, Map<Ore.Grade, RegistryObject<Block>>> URANIUM_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                    register(("ore/" + grade.name() + "_uranium" + "/" + rock.name()), () -> new Block(Properties.of(Material.STONE).sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()), TFCItemGroup.ORES)
            )
    );

    public static final Map<Metal, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(Metal.class, metal ->
        register("fluid/metal/" + metal.name(), () -> new LiquidBlock(Fluids.METALS.get(metal).source(), Properties.of(TFCMaterials.MOLTEN_METAL).noCollission().strength(100f).noDrops()))
    );

    private static ToIntFunction<BlockState> alwaysLit()
    {
        return s -> 15;
    }

    private static boolean always(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type)
    {
        return true;
    }

    public static int lightEmission(BlockState state)
    {
        return state.getValue(BlockStateProperties.LIT) ? 15 : 0;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, CreativeModeTab group)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties().tab(group)));
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
