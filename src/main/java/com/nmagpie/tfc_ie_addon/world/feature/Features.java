package com.nmagpie.tfc_ie_addon.world.feature;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"unused", "SameParameterValue"})
public class Features
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TFC_IE_Addon.MOD_ID);
    public static final RegistryObject<GeodeFeature> QUARTZ_GEODE = register("quartz_geode", GeodeFeature::new, GeodeConfig.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
