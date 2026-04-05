package com.nmagpie.tfc_ie_addon.data.providers;

import java.util.concurrent.CompletableFuture;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.world.biome.TFCBiomes;

public class BuiltinBiomeTags extends BiomeTagsProvider
{
    public BuiltinBiomeTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> provider)
    {
        super(event.getGenerator().getPackOutput(), provider, TFC_IE_Addon.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        for (var entry : TFCBiomes.EXTENSIONS.getEntries())
        {
            tag(ModTags.Biomes.GENERATE_MINERAL_MIX)
                .addOptional(entry.get().key().location());
        }
    }
}
