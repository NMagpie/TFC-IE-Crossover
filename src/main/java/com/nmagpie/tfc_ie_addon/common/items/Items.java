package com.nmagpie.tfc_ie_addon.common.items;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import com.nmagpie.tfc_ie_addon.util.IEOre;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems.ItemId;
import net.dries007.tfc.util.Helpers;

@SuppressWarnings("unused")
public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TFC_IE_Addon.MOD_ID);

    public static final ItemId QUARTZ_SHARD = register("mineral/quartz_shard");

    public static final ItemId WIRECUTTER_HEAD = register("tool_head/wirecutter");

    public static final ItemId HAMMER_HEAD = register("tool_head/ie_hammer");

    public static final ItemId MOLD_SHEET = register("mold_sheet",
        () -> new Item(new Item.Properties().stacksTo(1)));
    public static final ItemId MOLD_BLOCK = register("mold_block",
        () -> new Item(new Item.Properties().stacksTo(1)));

    public static final ItemId DRILLHEAD_BLACK_STEEL = register("drillhead_black_steel", () -> new Drillhead(Drillhead.BLACK_STEEL));
    public static final ItemId DRILLHEAD_BLUE_STEEL = register("drillhead_blue_steel", () -> new Drillhead(Drillhead.BLUE_STEEL));
    public static final ItemId DRILLHEAD_RED_STEEL = register("drillhead_red_steel", () -> new Drillhead(Drillhead.RED_STEEL));

    public static final ItemId TREATED_WOOD_LUMBER = register("treated_wood_lumber");

    public static final Map<IEOre, Map<Ore.Grade, ItemId>> ORES = Helpers.mapOf(IEOre.class, ore ->
        Helpers.mapOf(Ore.Grade.class, grade ->
            register("ore/" + grade.name() + "_" + ore.name()))
    );
    public static final Map<IEOre, ItemId> POWDERS = Helpers.mapOf(IEOre.class, ore ->
        register("powder/" + ore.name())
    );

    public static final Map<IEMetal, Map<IEMetal.ItemType, ItemId>> METAL_ITEMS = Helpers.mapOf(IEMetal.class, metal ->
        Helpers.mapOf(IEMetal.ItemType.class, type ->
            register("metal/" + type.name() + "/" + metal.name(), () -> type.create(metal))
        )
    );
    public static final Map<IEMetal, ItemId> METAL_FLUID_BUCKETS = Helpers.mapOf(IEMetal.class, metal ->
        register("bucket/metal/" + metal.name(), () -> new BucketItem(Fluids.METALS.get(metal).getSource(), new Item.Properties().craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1)))
    );

    private static ItemId register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static ItemId register(String name, Item.Properties properties)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new Item(properties)));
    }

    private static ItemId register(String name, Supplier<Item> item)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }
}