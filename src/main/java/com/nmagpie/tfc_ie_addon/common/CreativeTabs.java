package com.nmagpie.tfc_ie_addon.common;

import java.util.Map;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Blocks;
import com.nmagpie.tfc_ie_addon.common.items.Items;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.TFCCreativeTabs.Id;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;

@SuppressWarnings({"unused", "SameParameterValue"})
public class CreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFC_IE_Addon.MOD_ID);

    public static final Id MAIN = register("main", () -> new ItemStack(Items.QUARTZ_SHARD.get()), CreativeTabs::fillTab);

    private static Id register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final var holder = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("tfc_ie_addon.creative_tab." + name))
            .displayItems(displayItems)
            .build());
        return new Id(holder, displayItems);
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        accept(out, Items.WIRECUTTER_HEAD);
        accept(out, Items.HAMMER_HEAD);
        accept(out, Items.MOLD_SHEET);
        accept(out, Items.MOLD_BLOCK);
        accept(out, Items.DRILLHEAD_BLACK_STEEL);
        accept(out, Items.DRILLHEAD_BLUE_STEEL);
        accept(out, Items.DRILLHEAD_RED_STEEL);
        accept(out, Items.TREATED_WOOD_LUMBER);
        for (IEOre ore : IEOre.values())
        {
            accept(out, Blocks.SMALL_ORES, ore);
            accept(out, Items.ORES, ore, Ore.Grade.POOR);
            accept(out, Items.ORES, ore, Ore.Grade.NORMAL);
            accept(out, Items.ORES, ore, Ore.Grade.RICH);
        }
        for (IEMetal metal : IEMetal.values())
            for (IEMetal.ItemType itemType : IEMetal.ItemType.values())
                accept(out, Items.METAL_ITEMS, metal, itemType);
        Items.POWDERS.values().forEach(out::accept);
        for (IEMetal metal : IEMetal.values())
            accept(out, Items.METAL_FLUID_BUCKETS, metal);
        accept(out, Blocks.QUARTZ_BLOCK);
        accept(out, Blocks.BUDDING_QUARTZ);
        accept(out, Items.QUARTZ_SHARD);
        accept(out, Blocks.QUARTZ_CLUSTER);
        accept(out, Blocks.LARGE_QUARTZ_BUD);
        accept(out, Blocks.MEDIUM_QUARTZ_BUD);
        accept(out, Blocks.SMALL_QUARTZ_BUD);
        for (IEMetal metal : IEMetal.values())
            for (IEMetal.BlockType blockType : IEMetal.BlockType.values())
                accept(out, Blocks.METALS, metal, blockType);
        for (IEOre ore : IEOre.values())
            Blocks.ORES.values().forEach(map -> map.get(ore).values().forEach(out::accept));
    }

    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == net.minecraft.world.item.Items.AIR)
        {
            TFC_IE_Addon.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            return;
        }
        out.accept(reg.get());
    }
}
