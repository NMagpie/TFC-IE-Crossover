package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.common.register.IEFluids;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.fluids.FluidHolder;

public class BuiltinFluidTags extends TagsProvider<Fluid> implements Accessors
{
    public BuiltinFluidTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> provider)
    {
        super(event.getGenerator().getPackOutput(), Registries.FLUID, provider, TFC_IE_Addon.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(TFCTags.Fluids.MOLTEN_METALS).add(Fluids.METALS);
        for (IEFluids.FluidEntry fluid : IEFluids.ALL_ENTRIES)
        {
            tag(TFCTags.Fluids.USABLE_IN_RED_STEEL_BUCKET).add(fluid.getStill());
            tag(TFCTags.Fluids.USABLE_IN_WOODEN_BUCKET).add(fluid.getStill());
            tag(TFCTags.Fluids.USABLE_IN_BARREL).add(fluid.getStill());
        }
    }

    @Override
    protected FluidTagAppender tag(TagKey<Fluid> tag)
    {
        return new FluidTagAppender(getOrCreateRawBuilder(tag), modId);
    }

    @SuppressWarnings({"unused", "UnusedReturnValue", "SameParameterValue"})
    static class FluidTagAppender extends TagAppender<Fluid> implements Accessors
    {
        FluidTagAppender(TagBuilder builder, String modId)
        {
            super(builder);
        }

        FluidTagAppender add(Fluid... fluids) {return add(Arrays.stream(fluids));}

        FluidTagAppender add(Stream<Fluid> fluids)
        {
            fluids.forEach(b -> add(key(b)));
            return this;
        }

        FluidTagAppender add(Map<?, ? extends FluidHolder<? extends Fluid>> fluids)
        {
            fluids.values().forEach(v -> add(v.getSource()));
            return this;
        }

        FluidTagAppender add(FluidIngredient ingredient)
        {
            switch (ingredient)
            {
                case TagFluidIngredient tag -> addTag(tag.tag());
                case SingleFluidIngredient item -> add(item.fluid().value());
                case CompoundFluidIngredient comp -> comp.children().forEach(this::add);
                default -> throw new AssertionError("Unhandled ingredient type: " + ingredient);
            }
            return this;
        }

        @Override
        public FluidTagAppender addTag(TagKey<Fluid> tag) {return (FluidTagAppender) super.addTag(tag);}

        private ResourceKey<Fluid> key(Fluid fluid)
        {
            return BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow();
        }
    }
}
