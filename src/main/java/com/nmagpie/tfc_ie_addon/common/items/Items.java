package com.nmagpie.tfc_ie_addon.common.items;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.util.IEMetal;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;

@SuppressWarnings("unused")
public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFC_IE_Addon.MOD_ID);

    public static final RegistryObject<Item> QUARTZ_SHARD = register("mineral/quartz_shard");

    public static final RegistryObject<Item> WIRECUTTER_HEAD = register("tool_head/wirecutter");

    public static final RegistryObject<Item> HAMMER_HEAD = register("tool_head/ie_hammer");

    public static final RegistryObject<Item> MOLD_SHEET = register("mold_sheet",
        () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MOLD_BLOCK = register("mold_block",
        () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Drillhead> DRILLHEAD_BLACK_STEEL = register("drillhead_black_steel", () -> new Drillhead(Drillhead.BLACK_STEEL));
    public static final RegistryObject<Drillhead> DRILLHEAD_BLUE_STEEL = register("drillhead_blue_steel", () -> new Drillhead(Drillhead.BLUE_STEEL));
    public static final RegistryObject<Drillhead> DRILLHEAD_RED_STEEL = register("drillhead_red_steel", () -> new Drillhead(Drillhead.RED_STEEL));

    public static final RegistryObject<Item> TREATED_WOOD_LUMBER = register("treated_wood_lumber");

    public static final Map<Ore.Grade, RegistryObject<Item>> BAUXITE_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_bauxite"));
    public static final Map<Ore.Grade, RegistryObject<Item>> GALENA_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_galena"));
    public static final Map<Ore.Grade, RegistryObject<Item>> URANINITE_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_uraninite"));
    public static final RegistryObject<Item> BAUXITE_POWDER = register("powder/bauxite");
    public static final RegistryObject<Item> GALENA_POWDER = register("powder/galena");
    public static final RegistryObject<Item> URANINITE_POWDER = register("powder/uraninite");

    public static final Map<IEMetal, Map<IEMetal.ItemType, RegistryObject<Item>>> METAL_ITEMS = Helpers.mapOfKeys(IEMetal.class, metal ->
        Helpers.mapOfKeys(IEMetal.ItemType.class, type ->
            register("metal/" + type.name() + "/" + metal.name(), () -> type.create(metal))
        )
    );
    public static final Map<IEMetal, RegistryObject<BucketItem>> METAL_FLUID_BUCKETS = Helpers.mapOfKeys(IEMetal.class, metal ->
        register("bucket/metal/" + metal.name(), () -> new BucketItem(Fluids.METALS.get(metal).source(), new Item.Properties().craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1)))
    );

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}