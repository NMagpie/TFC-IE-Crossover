package com.nmagpie.tfc_ie_addon.common;

import java.io.IOException;
import java.nio.file.Path;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.util.IEHeatHandler;
import com.nmagpie.tfc_ie_addon.config.Config;
import javax.annotation.Nonnull;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;

public class Events
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(Events::onPackFinder);
        bus.addListener(Events::setupImmersivePetroleumCompat);

        MinecraftForge.EVENT_BUS.register(ExternalHeatCapListener.class);
    }

    public static void onPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                final IModFile modFile = ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile();
                final Path resourcePath = modFile.getFilePath();
                try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":overload", true, resourcePath)
                {
                    private final IModFile file = ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile();

                    @Nonnull
                    @Override
                    protected Path resolve(@Nonnull String... paths)
                    {
                        return file.findResource(paths);
                    }
                })
                {
                    final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                    if (metadata != null)
                    {
                        TFC_IE_Addon.LOGGER.info("Injecting tfc + ie addon override pack");
                        event.addRepositorySource(consumer ->
                            consumer.accept(Pack.readMetaAndCreate("tfc_ie_addon_data", Component.literal("TFC + IE Resources"), true, id -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void setupImmersivePetroleumCompat(AddPackFindersEvent event)
    {
        if (event.getPackType() == PackType.SERVER_DATA && ModList.get().isLoaded("immersivepetroleum") && Config.COMMON.enableImmersivePetorlumCompat.get())
        {
            Path resourcePath = ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile().findResource("compat/immersivepetroleum");
            PathPackResources pack = new PathPackResources(ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile().getFileName() + ":" + resourcePath, true, resourcePath);
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

    public static class ExternalHeatCapListener
    {
        @SubscribeEvent
        public static void attachExternalHeatHandler(AttachCapabilitiesEvent<BlockEntity> event)
        {
            if (event.getObject() instanceof CrucibleBlockEntity crucible)
            {
                IEHeatHandler.Provider provider = new IEHeatHandler.Provider(crucible);
                event.addCapability(Helpers.identifier("crucible_external_heater"), provider);
                event.addListener(provider::invalidate);
            }
        }
    }
}
