package com.nmagpie.tfc_ie_addon.common.blockenties;

import blusunrize.immersiveengineering.common.register.IEItems;
import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * {@link net.dries007.tfc.common.blockentities.AnvilBlockEntity but with firing event about successfull craft}
 */
public class AnvilBlockEntity extends net.dries007.tfc.common.blockentities.AnvilBlockEntity {

    public AnvilBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public InteractionResult work(ServerPlayer player, ForgeStep step) {
        InteractionResult result = super.work(player, step);

        if (result.equals(InteractionResult.SUCCESS) && this.inventory.getStackInSlot(SLOT_INPUT_MAIN).getItem().equals(
                TFCItems.METAL_ITEMS
                        .get(Metal.Default.HIGH_CARBON_STEEL)
                        .get(Metal.ItemType.INGOT).get()
        ))
            this.inventory.setStackInSlot(SLOT_INPUT_SECOND, new ItemStack(IEItems.Ingredients.SLAG));

        return result;
    }
}