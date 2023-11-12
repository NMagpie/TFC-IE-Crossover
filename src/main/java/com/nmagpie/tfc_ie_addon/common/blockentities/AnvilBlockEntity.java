package com.nmagpie.tfc_ie_addon.common.blockentities;

import blusunrize.immersiveengineering.common.register.IEItems;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.common.capabilities.forge.Forging;
import net.dries007.tfc.common.capabilities.forge.ForgingCapability;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.util.Helpers;

/**
 * {@link net.dries007.tfc.common.blockentities.AnvilBlockEntity but with firing event about successfull craft}
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AnvilBlockEntity extends net.dries007.tfc.common.blockentities.AnvilBlockEntity
{
    public AnvilBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public InteractionResult work(ServerPlayer player, ForgeStep step)
    {
        assert level != null;
        final ItemStack stack = inventory.getStackInSlot(SLOT_INPUT_MAIN);
        final Forging forge = ForgingCapability.get(stack);
        if (forge != null)
        {
            final AnvilRecipe recipe = forge.getRecipe(level);
            if (recipe != null)
            {
                if (recipe.getId().toString().equals("tfc:anvil/steel_ingot"))
                {
                    forge.addStep(step);
                    if (recipe.checkComplete(inventory))
                    {
                        ItemStack slag = new ItemStack(IEItems.Ingredients.SLAG);
                        if (!player.getInventory().add(slag))
                            Helpers.spawnItem(level, worldPosition, slag);
                    }
                }
            }
        }
        return super.work(player, step);
    }
}