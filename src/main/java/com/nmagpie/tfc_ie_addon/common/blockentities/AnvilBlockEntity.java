package com.nmagpie.tfc_ie_addon.common.blockentities;

import blusunrize.immersiveengineering.common.register.IEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.common.capabilities.forge.Forging;
import net.dries007.tfc.common.capabilities.forge.ForgingCapability;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

/**
 * {@link net.dries007.tfc.common.blockentities.AnvilBlockEntity but with firing event about successfull craft}
 */
public class AnvilBlockEntity extends net.dries007.tfc.common.blockentities.AnvilBlockEntity
{

    public AnvilBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public InteractionResult work(ServerPlayer player, ForgeStep step)
    {
        InteractionResult result = super.work(player, step);

        assert level != null;
        final ItemStack stack = inventory.getStackInSlot(SLOT_INPUT_MAIN);
        final Forging forge = ForgingCapability.get(stack);
        final Item item = TFCItems.METAL_ITEMS.get(Metal.Default.HIGH_CARBON_STEEL).get(Metal.ItemType.INGOT).get();
        if (forge != null)
        {
            if (result.equals(InteractionResult.SUCCESS) && stack.is(item) && forge.getWork() == 0)
            {
                ItemStack slag = new ItemStack(IEItems.Ingredients.SLAG);
                if (!player.getInventory().add(slag))
                    Helpers.spawnItem(level, worldPosition, slag);
            }
        }

        return result;
    }
}