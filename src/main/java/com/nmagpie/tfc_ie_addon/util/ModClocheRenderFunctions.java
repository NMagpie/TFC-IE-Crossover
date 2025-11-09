package com.nmagpie.tfc_ie_addon.util;

import java.util.Collection;
import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction;
import blusunrize.immersiveengineering.mixin.accessors.CropBlockAccess;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.joml.Vector3f;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;

public class ModClocheRenderFunctions
{
    public static void init()
    {
        for (SoilBlockType.Variant soil : SoilBlockType.Variant.values())
        {
            String soilName = soil.name().toLowerCase();

            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(TerraFirmaCraft.MOD_ID, "block/farmland/" + soilName);

            ClocheRecipe.registerSoilTexture(Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(soil)), rl);
        }
    }

    public static void register()
    {
        register("doublecroptfc", RenderFunctionDoubleCropTFC.CODEC);
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(String path, DualMapCodec<? super RegistryFriendlyByteBuf, ? extends ClocheRenderFunction> codec)
    {
        ClocheRenderFunction.RENDER_FUNCTION_FACTORIES.put(TFC_IE_Addon.identifier(path), codec);
    }

    public static class RenderFunctionDoubleCropTFC implements ClocheRenderFunction
    {
        public static final DualMapCodec<? super RegistryFriendlyByteBuf, RenderFunctionDoubleCropTFC> CODEC = DualCompositeMapCodecs.composite(
            DualCodecs.registryEntry(BuiltInRegistries.BLOCK).fieldOf("block"), r -> r.cropBlock,
            DualCodecs.INT.fieldOf("doublingAge"), r -> r.doublingAge,
            RenderFunctionDoubleCropTFC::new
        );

        final Block cropBlock;
        IntegerProperty ageProperty;
        int maxAge;
        final int doublingAge;

        public RenderFunctionDoubleCropTFC(Block cropBlock, int doublingAge)
        {
            this.cropBlock = cropBlock;
            var cropAge = getCropAge(cropBlock);
            this.ageProperty = cropAge.getFirst();
            this.maxAge = cropAge.getSecond();
            this.doublingAge = doublingAge;
        }

        @Override
        public float getScale(ItemStack seed, float growth)
        {
            return 0.6875f;
        }

        @Override
        public Collection<Pair<BlockState, Transformation>> getBlocks(ItemStack stack, float growth)
        {
            int age = Math.min(this.maxAge, Math.round(this.maxAge * growth));
            if (age >= doublingAge)
            {
                Transformation top = new Transformation(new Vector3f(0, 1, 0), null, null, null);
                return ImmutableList.of(
                    Pair.of(cropBlock.defaultBlockState().setValue(ageProperty, age), new Transformation(null)),
                    Pair.of(cropBlock.defaultBlockState().setValue(ageProperty, age).setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP), top)
                );
            }
            return ImmutableList.of(Pair.of(cropBlock.defaultBlockState().setValue(ageProperty, age), new Transformation(null)));
        }

        @Override
        public DualMapCodec<? super RegistryFriendlyByteBuf, ? extends ClocheRenderFunction> codec()
        {
            return CODEC;
        }
    }

    private static Pair<IntegerProperty, Integer> getCropAge(Block block) throws IllegalArgumentException
    {
        if (block instanceof CropBlock crop)
            return Pair.of(((CropBlockAccess) crop).invokeGetAgeProperty(), crop.getMaxAge());
        else
        {
            for (Property<?> prop : block.defaultBlockState().getProperties())
                if ("age".equals(prop.getName()) && prop instanceof IntegerProperty intProp)
                {
                    int max = intProp.getPossibleValues().stream().max(Integer::compare).orElse(-1);
                    if (max > 0)
                        return Pair.of(intProp, max);
                }
        }
        throw new IllegalArgumentException("Block " + block.getDescriptionId() + " is not a valid crop block");
    }
}
