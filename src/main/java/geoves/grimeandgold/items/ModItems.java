package geoves.grimeandgold.items;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.items.custom.EmptyCopperSiftItem;
import geoves.grimeandgold.items.custom.CopperSiftFullGrimeItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item DIAMOND_SHARD = registerItem("diamond_shard", Item::new);
    public static final Item COPPER_SIFT_EMPTY = registerItem("copper_sift", properties -> new EmptyCopperSiftItem(properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_GRIME = registerItem("copper_sift_filled_grime", properties -> new CopperSiftFullGrimeItem(ModBlocks.GRIME, properties.stacksTo(1).durability(8)));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name)))));
    }

    public static void registerModItems(){
        GrimeAndGold.LOGGER.info("Registering Mod Items for " + GrimeAndGold.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(DIAMOND_SHARD);
        });
    }
}
