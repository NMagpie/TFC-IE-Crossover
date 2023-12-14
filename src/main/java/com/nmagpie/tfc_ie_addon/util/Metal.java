package com.nmagpie.tfc_ie_addon.util;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.util.NonNullFunction;

import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.registry.RegistryMetal;

@MethodsReturnNonnullByDefault
public enum Metal implements RegistryMetal
{
    ELECTRUM(
        0xFCB74A,
        MapColor.COLOR_YELLOW,
        Rarity.EPIC,
        TFCTiers.WROUGHT_IRON,
        TFCArmorMaterials.WROUGHT_IRON,
        net.dries007.tfc.util.Metal.Tier.TIER_IV),

    CONSTANTAN(
        0xEC8068,
        MapColor.COLOR_ORANGE,
        Rarity.EPIC,
        TFCTiers.STEEL,
        TFCArmorMaterials.STEEL,
        net.dries007.tfc.util.Metal.Tier.TIER_IV),

    ALUMINUM(
        0xCCC1BC,
        MapColor.CLAY,
        Rarity.COMMON,
        TFCTiers.BRONZE,
        TFCArmorMaterials.BRONZE,
        net.dries007.tfc.util.Metal.Tier.TIER_I),

    LEAD(
        0x433F4D,
        MapColor.TERRACOTTA_BLUE,
        Rarity.RARE,
        TFCTiers.BLACK_BRONZE,
        TFCArmorMaterials.WROUGHT_IRON,
        net.dries007.tfc.util.Metal.Tier.TIER_III),

    URANIUM(0x738A6C,
        MapColor.TERRACOTTA_GREEN,
        Rarity.EPIC,
        TFCTiers.STEEL,
        TFCArmorMaterials.STEEL,
        net.dries007.tfc.util.Metal.Tier.TIER_IV);

    private final String serializedName;
    private final int color;
    private final MapColor mapColor;
    private final Rarity rarity;
    private final Tier toolTier;
    private final ArmorMaterial armorTier;
    private final net.dries007.tfc.util.Metal.Tier metalTier;

    Metal(int color, MapColor mapColor, Rarity rarity, Tier toolTier, ArmorMaterial armorTier, net.dries007.tfc.util.Metal.Tier metalTier)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.mapColor = mapColor;
        this.color = color;

        this.rarity = rarity;
        this.toolTier = toolTier;
        this.armorTier = armorTier;
        this.metalTier = metalTier;
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

    @Override
    public Rarity getRarity()
    {
        return rarity;
    }

    @Override
    public Tier toolTier()
    {
        return toolTier;
    }

    @Override
    public ArmorMaterial armorTier()
    {
        return armorTier;
    }

    @Override
    public net.dries007.tfc.util.Metal.Tier metalTier()
    {
        return metalTier;
    }

    @Override
    public MapColor mapColor()
    {
        return mapColor;
    }

    @Override
    public Supplier<Block> getFullBlock()
    {
        return Blocks.METALS.get(this).get(BlockType.BLOCK);
    }

    public enum ItemType
    {
        SHEET(metal -> new Item(new Item.Properties())),
        DOUBLE_INGOT(metal -> new Item(new Item.Properties()));

        private final NonNullFunction<Metal, Item> itemFactory;

        ItemType(NonNullFunction<Metal, Item> itemFactory)
        {
            this.itemFactory = itemFactory;
        }

        public Item create(Metal metal)
        {
            return itemFactory.apply(metal);
        }
    }

    public enum BlockType
    {
        BLOCK(metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_SLAB(metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_STAIRS(metal -> new StairBlock(() -> metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

        private final Function<RegistryMetal, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final String serializedName;

        BlockType(Function<RegistryMetal, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        BlockType(Function<RegistryMetal, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(RegistryMetal metal)
        {
            return () -> blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties)
        {
            return block -> blockItemFactory.apply(block, properties);
        }

        public String createName(RegistryMetal metal)
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
