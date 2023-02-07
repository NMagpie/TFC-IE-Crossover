package com.nmagpie.tfc_ie_addon.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

import static com.nmagpie.tfc_ie_addon.TFC_IE_Addon.MOD_ID;

@SuppressWarnings("unused")
public class Features {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);
    public static final RegistryObject<TFC_IE_GeodeFeature> QUARTZ_GEODE = register("quartz_geode", TFC_IE_GeodeFeature::new, TFC_IE_GeodeConfig.CODEC);
    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
