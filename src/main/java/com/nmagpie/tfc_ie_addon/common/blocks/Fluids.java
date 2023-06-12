package com.nmagpie.tfc_ie_addon.common.blocks;

import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.common.util.Metal;
import net.dries007.tfc.common.fluids.FlowingFluidRegistryObject;
import net.dries007.tfc.common.fluids.MoltenFluid;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.nmagpie.tfc_ie_addon.TFC_IE_Addon.MOD_ID;
import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class Fluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);

    public static final Map<Metal, FlowingFluidRegistryObject<ForgeFlowingFluid>> METALS = Helpers.mapOfKeys(Metal.class, metal -> register(
            "metal/" + metal.getSerializedName(),
            "metal/flowing_" + metal.getSerializedName(),
            properties -> properties.block(Blocks.METAL_FLUIDS.get(metal)).bucket(Items.METAL_FLUID_BUCKETS.get(metal)).explosionResistance(100),
            FluidAttributes.builder(MOLTEN_STILL, MOLTEN_FLOW)
                    .translationKey("fluid.tfc_ie_addon.metal." + metal.getSerializedName())
                    .color(
                            ALPHA_MASK | metal.getColor())
                    .rarity(metal.getRarity())
                    .luminosity(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    ));

    private static <F extends FlowingFluid> FlowingFluidRegistryObject<F> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory) {
        return RegistrationHelpers.registerFluid(FLUIDS, sourceName, flowingName, builder, attributes, sourceFactory, flowingFactory);
    }
}
