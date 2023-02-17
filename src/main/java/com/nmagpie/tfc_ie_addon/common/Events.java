package com.nmagpie.tfc_ie_addon.common;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.resource.PathResourcePack;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

public class Events
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(Events::onPackFinder);
    }

    public static void onPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                var modFile = ModList.get().getModFileById(TFC_IE_Addon.MOD_ID).getFile();
                var resourcePath = modFile.getFilePath();
                var pack = new PathResourcePack(modFile.getFileName() + ":overload", resourcePath)
                {
                    @Nonnull
                    @Override
                    protected Path resolve(@Nonnull String... paths)
                    {
                        return modFile.findResource(paths);
                    }
                };
                var metadata = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
                if (metadata != null)
                {
                    TFC_IE_Addon.LOGGER.info("Injecting tfc + ie addon override pack");
                    event.addRepositorySource((consumer, constructor) ->
                        consumer.accept(constructor.create("builtin/tfc_ie_addon_data", new TextComponent("TFC + IE Resources"), true, () -> pack, metadata, Pack.Position.TOP, PackSource.BUILT_IN, false))
                    );
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
