package com.nmagpie.tfc_ie_addon.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import net.dries007.tfc.util.registry.RegistryRock;

public enum IEOre
{
    BAUXITE(IEMetal.ALUMINUM),
    GALENA(IEMetal.LEAD),
    URANINITE(IEMetal.URANIUM);

    private final IEMetal metal;

    IEOre(IEMetal metal)
    {
        this.metal = metal;
    }

    public IEMetal metal()
    {
        return metal;
    }

    public Block create(RegistryRock rock)
    {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(rock.category().hardness(6.5F), 10.0F).requiresCorrectToolForDrops();
        return new Block(properties);
    }
}
