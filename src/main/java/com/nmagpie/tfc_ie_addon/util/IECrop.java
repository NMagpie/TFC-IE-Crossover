package com.nmagpie.tfc_ie_addon.util;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blockentities.BlockEntities;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.util.climate.ClimateRange;

public enum IECrop implements StringRepresentable
{
    HEMP(0.6f, 0.2f, 0.6f, 4, 1);

    private static ExtendedProperties doubleCrop()
    {
        return dead().blockEntity(BlockEntities.CROP).serverTicks(CropBlockEntity::serverTickBottomPartOnly);
    }

    private static ExtendedProperties dead()
    {
        return ExtendedProperties.of(MapColor.PLANT).noCollission().randomTicks().strength(0.4F).sound(SoundType.CROP).flammable(60, 30).pushReaction(PushReaction.DESTROY);
    }

    private final String serializedName;
    private final float nitrogen;
    private final float phosphorous;
    private final float potassium;
    private final Supplier<Block> factory;
    private final Supplier<Block> deadFactory;
    private final Supplier<Block> wildFactory;

    IECrop(float nitrogen, float phosphorous, float potassium, int doubleBlockBottomStages, int doubleBlockTopStages)
    {
        this(nitrogen, phosphorous, potassium,
                self -> createDoubleCrop(doubleCrop(), doubleBlockBottomStages, doubleBlockTopStages, self),
                self -> new DeadDoubleCropBlock(dead(), self.getClimateRange()),
            self -> new WildDoubleCropBlock(dead().randomTicks())
        );
    }

    IECrop(float nitrogen, float phosphorous, float potassium, Function<IECrop, Block> factory, Function<IECrop, Block> deadFactory, Function<IECrop, Block> wildFactory)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.nitrogen = nitrogen;
        this.phosphorous = phosphorous;
        this.potassium = potassium;
        this.factory = () -> factory.apply(this);
        this.deadFactory = () -> deadFactory.apply(this);
        this.wildFactory = () -> wildFactory.apply(this);
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public Block create()
    {
        return factory.get();
    }

    public Block createDead()
    {
        return deadFactory.get();
    }

    public Block createWild()
    {
        return wildFactory.get();
    }

    public float getNitrogen()
    {
        return this.nitrogen;
    }

    public float getPhosphorous()
    {
        return this.phosphorous;
    }

    public float getPotassium()
    {
        return this.potassium;
    }

    public Supplier<ClimateRange> getClimateRange()
    {
        return ClimateRange.MANAGER.getReference(TFC_IE_Addon.identifier("crop/" + serializedName));
    }

    // Hard coded for hemp
    private static DoubleCropBlock createDoubleCrop(ExtendedProperties properties, int singleStages, int doubleStages, IECrop crop)
    {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(singleStages + doubleStages - 1);
        return new DoubleCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, Blocks.DEAD_CROPS.get(crop), IEItems.Misc.HEMP_SEEDS, crop.getNitrogen(), crop.getPhosphorous(), crop.getPotassium(), crop.getClimateRange()) {
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }
}
