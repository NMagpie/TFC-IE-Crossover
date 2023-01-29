package com.nmagpie.tfc_ie_addon.common.items;

import com.nmagpie.tfc_ie_addon.TFC_IE_Addon;
import com.nmagpie.tfc_ie_addon.common.blocks.Fluids;
import com.nmagpie.tfc_ie_addon.common.util.Metal;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static net.dries007.tfc.common.TFCItemGroup.MISC;

@SuppressWarnings("unused")
public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFC_IE_Addon.MOD_ID);

    public static final Map<Ore.Grade, RegistryObject<Item>> ALUMINUM_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_aluminum", TFCItemGroup.ORES));

    public static final Map<Ore.Grade, RegistryObject<Item>> LEAD_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_lead", TFCItemGroup.ORES));

    public static final Map<Ore.Grade, RegistryObject<Item>> URANIUM_ORES = Helpers.mapOfKeys(Ore.Grade.class, grade -> register("ore/" + grade.name() + "_uranium", TFCItemGroup.ORES));

    public static final Map<Metal, Map<Metal.ItemType, RegistryObject<Item>>> METAL_ITEMS = Helpers.mapOfKeys(Metal.class, metal ->
            Helpers.mapOfKeys(Metal.ItemType.class, type ->
                    register("metal/" + type.name() + "/" + metal.name(), () -> type.create(metal))
            )
    );

    public static final Map<Metal, RegistryObject<BucketItem>> METAL_FLUID_BUCKETS = Helpers.mapOfKeys(Metal.class, metal ->
        register("bucket/metal/" + metal.name(), () -> new BucketItem(Fluids.METALS.get(metal).source(), new Item.Properties().craftRemainder(net.minecraft.world.item.Items.BUCKET).stacksTo(1).tab(MISC)))
    );

    private static Item.Properties prop()
    {
        return new Item.Properties().tab(MISC);
    }

    private static RegistryObject<Item> register(String name, CreativeModeTab group)
    {
        return register(name, () -> new Item(new Item.Properties().tab(group)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}