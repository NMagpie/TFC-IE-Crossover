package com.nmagpie.tfc_ie_addon.common.blockentities;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;

@SuppressWarnings({"unused", "SameParameterValue"})
public class BlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TerraFirmaCraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<AnvilBlockEntity>> ANVIL = register("anvil", AnvilBlockEntity::new, Stream.concat(
        TFCBlocks.ROCK_ANVILS.values().stream(),
        TFCBlocks.METALS.values().stream().map(m -> m.get(Metal.BlockType.ANVIL)).filter(Objects::nonNull)
    ));

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        Field anvil;

        RegistryObject<BlockEntityType<T>> registeredItem = RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);

        if (name.equals("anvil"))
            try
            {
                FieldUtil.setFinalStatic(TFCBlockEntities.class.getDeclaredField("ANVIL"), registeredItem);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

        return registeredItem;
    }
}
