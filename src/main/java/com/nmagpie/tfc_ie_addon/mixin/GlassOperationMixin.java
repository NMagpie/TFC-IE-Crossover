package com.nmagpie.tfc_ie_addon.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.common.capabilities.glass.GlassOperation;

@Mixin(GlassOperation.class)
public abstract class GlassOperationMixin
{
    @Invoker("<init>")
    private static GlassOperation init(String name, int id)
    {
        throw new AssertionError();
    }

    @Shadow
    @Final
    @Mutable
    private static GlassOperation[] $VALUES;

    @Inject(method = "getByPowder", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getByPowder(ItemStack stack, CallbackInfoReturnable<GlassOperation> cir)
    {
        if (stack.is(Items.GALENA_POWDER.get()))
            cir.setReturnValue(GlassOperation.valueOf("LEAD"));
        if (stack.is(Items.URANINITE_POWDER.get()))
            cir.setReturnValue(GlassOperation.valueOf("URANIUM"));
    }

    @Inject(method = "byIndex", at = @At("HEAD"), cancellable = true, remap = false)
    private static void byIndex(int id, CallbackInfoReturnable<GlassOperation> cir)
    {
        if (id >= 0 && id < GlassOperation.values().length)
            cir.setReturnValue(GlassOperation.values()[id]);
    }

    static
    {
        List<GlassOperation> values = new ArrayList<>(Arrays.asList($VALUES));
        GlassOperation last = values.get(values.size() - 1);

        values.add(init("LEAD", last.ordinal() + 1));
        values.add(init("URANIUM", last.ordinal() + 2));

        $VALUES = values.toArray(new GlassOperation[0]);
    }
}
