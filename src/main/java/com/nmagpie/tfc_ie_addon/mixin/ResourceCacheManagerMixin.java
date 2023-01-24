//package com.nmagpie.tfc_ie_addon.mixin;
//
//import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.resource.ResourceCacheManager;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Pseudo;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
///**
// * For some reason bad namespaces are being initialized. This is the only way to fix that.
// */
//@Pseudo
////@Mixin(ResourceCacheManager.class)
//public class ResourceCacheManagerMixin
//{
//    @Inject(method = "index", at = @At("HEAD"), cancellable = true, remap = false)
//    private void inject$initForNamespace(String namespace, CallbackInfo ci)
//    {
//        if (!ResourceLocation.isValidNamespace(namespace))
//        {
//            ci.cancel();
//            TFC_IE_Addon.LOGGER.info("Namespace REJECTED: " + namespace);
//        }
//    }
//
//}
