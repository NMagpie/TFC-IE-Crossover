package com.nmagpie.tfc_ie_addon.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import net.dries007.tfc.world.Codecs;

public record TFC_IE_GeodeConfig(BlockState outer, BlockState middle, SimpleWeightedRandomList<BlockState> inner,
                                 SimpleWeightedRandomList<BlockState
                                     > filling,
                                 SimpleWeightedRandomList<BlockState> innerPlacements) implements FeatureConfiguration
{
    public static final Codec<com.nmagpie.tfc_ie_addon.world.feature.TFC_IE_GeodeConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.BLOCK_STATE.fieldOf("outer").forGetter(c -> c.outer),
        Codecs.BLOCK_STATE.fieldOf("middle").forGetter(c -> c.middle),
        SimpleWeightedRandomList.wrappedCodec(Codecs.BLOCK_STATE).fieldOf("inner").forGetter(c -> c.inner),
        SimpleWeightedRandomList.wrappedCodec(Codecs.BLOCK_STATE).fieldOf("filling").forGetter(c -> c.filling),
        SimpleWeightedRandomList.wrappedCodec(Codecs.BLOCK_STATE).fieldOf("inner_placements").forGetter(c -> c.innerPlacements)
    ).apply(instance, com.nmagpie.tfc_ie_addon.world.feature.TFC_IE_GeodeConfig::new));
}
