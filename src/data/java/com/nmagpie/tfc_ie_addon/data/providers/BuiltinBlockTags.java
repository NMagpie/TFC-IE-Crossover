package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import com.google.common.base.Preconditions;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.DecorationBlockHolder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.registry.IdHolder;


public class BuiltinBlockTags extends TagsProvider<Block> implements Accessors
{
    private final ExistingFileHelper.IResourceType resourceType;

    public BuiltinBlockTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(event.getGenerator().getPackOutput(), Registries.BLOCK, lookup, TFC_IE_Addon.MOD_ID, event.getExistingFileHelper());
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // ===== Minecraft Tags (ordered like BlockTags) ===== //

        tag(BlockTags.STAIRS).addEvery(b -> b instanceof StairBlock);
        tag(BlockTags.SLABS).addEvery(b -> b instanceof SlabBlock);
        tag(BlockTags.CROPS)
            .add(Blocks.CROPS)
            .add(Blocks.DEAD_CROPS)
            .add(Blocks.WILD_CROPS);
        tag(BlockTags.MINEABLE_WITH_HOE)
            .add(Blocks.CROPS)
            .add(Blocks.DEAD_CROPS)
            .add(Blocks.WILD_CROPS);
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add3(Blocks.ORES)
            .add(Blocks.SMALL_ORES)
            .add2(Blocks.METALS)
            .add(
                Blocks.QUARTZ_BLOCK,
                Blocks.BUDDING_QUARTZ,
                Blocks.QUARTZ_CLUSTER,
                Blocks.LARGE_QUARTZ_BUD,
                Blocks.MEDIUM_QUARTZ_BUD,
                Blocks.SMALL_QUARTZ_BUD
            );
        tag(BlockTags.NEEDS_IRON_TOOL).add2(pivot(Blocks.ORES, IEOre.URANINITE));
        tag(BlockTags.REPLACEABLE).addEvery(e -> e.defaultBlockState().canBeReplaced());

        // ===== Common Tags ===== //

        for (IEOre ore : IEOre.values())
        {
            final var ores = pivot(Blocks.ORES, ore);

            tag(Tags.Blocks.ORES).addTags(
                oreBlockTagOf(ore, Ore.Grade.POOR),
                oreBlockTagOf(ore, Ore.Grade.NORMAL),
                oreBlockTagOf(ore, Ore.Grade.RICH));
            tag(oreBlockTagOf(ore, Ore.Grade.POOR)).add(ores, Ore.Grade.POOR);
            tag(oreBlockTagOf(ore, Ore.Grade.NORMAL)).add(ores, Ore.Grade.NORMAL);
            tag(oreBlockTagOf(ore, Ore.Grade.RICH)).add(ores, Ore.Grade.RICH);
        }
        pivot(Blocks.METALS, IEMetal.BlockType.BLOCK).forEach((metal, block) -> tag(storageBlockTagOf(Registries.BLOCK, metal)).add(block));

        // ===== TFC Tags ===== //

        tag(TFCTags.Blocks.CAN_BE_SNOW_PILED)
            .add(Blocks.SMALL_ORES)
            .add(Blocks.WILD_CROPS);
    }

    @Override
    protected BlockTagAppender tag(TagKey<Block> tag)
    {
        return new BlockTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Block> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.ITEM.getDefaultKey()), "Adding air to block tag");
                return super.add(entry);
            }
        });
    }

    @SuppressWarnings({"unused", "UnusedReturnValue", "SameParameterValue"})
    static class BlockTagAppender extends TagAppender<Block> implements Accessors
    {
        BlockTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        BlockTagAppender add(Block... blocks)
        {
            for (Block block : blocks) add(key(block));
            return this;
        }

        BlockTagAppender add(Stream<? extends Supplier<? extends Block>> blocks)
        {
            blocks.forEach(b -> add(key(b.get())));
            return this;
        }

        @SafeVarargs
        final <T extends IdHolder<? extends Block>> BlockTagAppender add(T... blocks)
        {
            return add(Arrays.stream(blocks));
        }

        /**
         * Adds every TFC-added block matching the given predicate
         */
        BlockTagAppender addEveryTFC(Predicate<Block> predicate)
        {
            return add(TFCBlocks.BLOCKS.getEntries().stream().filter(e -> predicate.test(e.get())));
        }

        BlockTagAppender add(Map<?, ? extends IdHolder<? extends Block>> blocks)
        {
            blocks.values().forEach(this::add);
            return this;
        }

        BlockTagAppender add2(Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(this::add));
            return this;
        }

        <V> BlockTagAppender add2(Map<?, ? extends Map<?, V>> blocks, Function<V, ? extends IdHolder<? extends Block>> ap)
        {
            blocks.values().forEach(m -> m.values().forEach(v -> add(ap.apply(v))));
            return this;
        }

        BlockTagAppender add3(Map<?, ? extends Map<?, ? extends Map<?, ? extends IdHolder<? extends Block>>>> blocks)
        {
            blocks.values().forEach(m1 -> m1.values().forEach(m2 -> m2.values().forEach(this::add)));
            return this;
        }

        BlockTagAppender addAll(Map<?, DecorationBlockHolder> blocks)
        {
            blocks.values().forEach(h -> add(h.slab(), h.stair(), h.wall()));
            return this;
        }

        BlockTagAppender addAll2(Map<?, ? extends Map<?, DecorationBlockHolder>> blocks)
        {
            blocks.values().forEach(m -> m.values().forEach(h -> add(h.slab(), h.stair(), h.wall())));
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender add(Map<T1, Map<T2, V>> blocks, T2 key)
        {
            return add(pivot(blocks, key));
        }

        <T, V extends IdHolder<? extends Block>> BlockTagAppender addOnly(Map<T, V> blocks, Predicate<T> key)
        {
            blocks.forEach((k, v) -> {if (key.test(k)) add(v);});
            return this;
        }

        <T1, T2, V extends IdHolder<? extends Block>> BlockTagAppender addOnly2(Map<T1, Map<T2, V>> blocks, Predicate<T2> key)
        {
            blocks.values().forEach(m -> addOnly(m, key));
            return this;
        }

        @SafeVarargs
        @SuppressWarnings("unchecked")
        final <K> BlockTagAppender addTags(Function<K, TagKey<Block>> apply, K... values)
        {
            return addTags(Arrays.stream(values).map(apply).toArray(TagKey[]::new));
        }

        @Override
        public BlockTagAppender addTag(TagKey<Block> tag)
        {
            return (BlockTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final BlockTagAppender addTags(TagKey<Block>... values)
        {
            return (BlockTagAppender) super.addTags(values);
        }

        BlockTagAppender remove(Block... blocks)
        {
            for (Block block : blocks) remove(key(block));
            return this;
        }

        private ResourceKey<Block> key(Block block)
        {
            return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
        }

        // ===== Added methods ===== //

        BlockTagAppender addEvery(Predicate<Block> predicate)
        {
            return add(Blocks.BLOCKS.getEntries().stream().filter(e -> predicate.test(e.get())));
        }
    }
}
