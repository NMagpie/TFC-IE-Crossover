package com.nmagpie.tfc_ie_addon.world.feature;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.world.feature.TFCFeatures.Id;

@SuppressWarnings({"unused", "SameParameterValue"})
public class Features
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TFC_IE_Addon.MOD_ID);
    public static final Id<GeodeFeature> QUARTZ_GEODE = register("quartz_geode", GeodeFeature::new, GeodeConfig.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> Id<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return new Id<>(FEATURES.register(name, () -> factory.apply(codec)));
    }
}
