package com.nmagpie.tfc_ie_addon.common.blockentities;

import java.util.function.Supplier;
import java.util.stream.Stream;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities.Id;
import net.dries007.tfc.util.registry.RegistrationHelpers;

@SuppressWarnings("SameParameterValue")
public class BlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TFC_IE_Addon.MOD_ID);

    public static Id<CropBlockEntity> CROP;

    static
    {
        CROP = register("crop", (pos, state) -> new CropBlockEntity(CROP.get(), pos, state), Blocks.CROPS.values().stream());
    }

    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks));
    }
}
