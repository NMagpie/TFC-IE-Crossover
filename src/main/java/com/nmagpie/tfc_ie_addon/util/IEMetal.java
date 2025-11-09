package com.nmagpie.tfc_ie_addon.util;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCTiers;

public enum IEMetal implements StringRepresentable
{
    ELECTRUM(
        0xFCB74A,
        MapColor.COLOR_YELLOW,
        TFCTiers.COPPER),

    CONSTANTAN(
        0xEC8068,
        MapColor.COLOR_ORANGE,
        TFCTiers.COPPER),

    ALUMINUM(
        0xCCC1BC,
        MapColor.CLAY,
        TFCTiers.WROUGHT_IRON),

    LEAD(
        0x433F4D,
        MapColor.TERRACOTTA_BLUE,
        TFCTiers.COPPER),

    URANIUM(0x738A6C,
        MapColor.TERRACOTTA_GREEN,
        TFCTiers.WROUGHT_IRON);

    private final String serializedName;
    private final int color;
    private final MapColor mapColor;
    private final LevelTier tier;

    IEMetal(int color, MapColor mapColor, LevelTier tier)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.mapColor = mapColor;
        this.color = color;
        this.tier = tier;
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public int getColor()
    {
        return color;
    }

    public MapColor mapColor()
    {
        return mapColor;
    }

    public LevelTier tier()
    {
        return tier;
    }

    public Supplier<Block> getFullBlock()
    {
        return Blocks.METALS.get(this).get(BlockType.BLOCK);
    }

    public enum ItemType
    {
        SHEET(metal -> new Item(new Item.Properties())),
        DOUBLE_INGOT(metal -> new Item(new Item.Properties()));

        private final Function<IEMetal, Item> itemFactory;

        ItemType(Function<IEMetal, Item> itemFactory)
        {
            this.itemFactory = itemFactory;
        }

        public Item create(IEMetal metal)
        {
            return itemFactory.apply(metal);
        }
    }

    public enum BlockType
    {
        BLOCK(metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_SLAB(metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_STAIRS(metal -> new StairBlock(metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

        private final Function<IEMetal, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final String serializedName;

        BlockType(Function<IEMetal, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        BlockType(Function<IEMetal, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(IEMetal metal)
        {
            return () -> blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties)
        {
            return block -> blockItemFactory.apply(block, properties);
        }

        public String createName(IEMetal metal)
        {
            if (this == BLOCK_SLAB || this == BLOCK_STAIRS)
            {
                return BLOCK.createName(metal) + (this == BLOCK_SLAB ? "_slab" : "_stairs");
            }
            else
            {
                return "metal/" + serializedName + "/" + metal.getSerializedName();
            }
        }
    }
}
