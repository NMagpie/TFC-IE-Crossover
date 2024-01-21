package com.nmagpie.tfc_ie_addon.common;

import java.nio.file.Path;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.config.Config;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;

public class Events
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(Events::setupImmersivePetroleumCompat);
    }

    public static void setupImmersivePetroleumCompat(AddPackFindersEvent event)
    {
        if (event.getPackType() == PackType.SERVER_DATA && ModList.get().isLoaded("immersivepetroleum") && Config.COMMON.enableImmersivePetorlumCompat.get())
        {
            Path resourcePath = ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile().findResource("compat/immersivepetroleum");
            try (PathPackResources pack = new PathPackResources(ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile().getFileName() + ":" + resourcePath, true, resourcePath))
            {
                PackMetadataSection metadata = new PackMetadataSection(Component.translatable("pack.tfc_ie_addon.immersivepetroleum.description"), SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
                event.addRepositorySource(consumer ->
                    consumer.accept(Pack.create(
                        "builtin/tfc_ie_addon_immersivepetroleum_compat",
                        Component.translatable("pack.tfc_ie_addon.immersivepetroleum.title"),
                        true, id -> pack,
                        new Pack.Info(metadata.getDescription(), metadata.getPackFormat(PackType.SERVER_DATA), FeatureFlagSet.of()),
                        PackType.SERVER_DATA,
                        Pack.Position.TOP,
                        false,
                        PackSource.BUILT_IN))
                );
            }
        }
    }
}
