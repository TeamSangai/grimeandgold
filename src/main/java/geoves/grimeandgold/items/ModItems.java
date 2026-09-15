package geoves.grimeandgold.items;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.entities.ModEntityTypeIds;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.items.custom.CopperSiftFullFerrisoilItem;
import geoves.grimeandgold.items.custom.CopperSiftFullGoldrustItem;
import geoves.grimeandgold.items.custom.EmptyCopperSiftItem;
import geoves.grimeandgold.items.custom.CopperSiftFullGrimeItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public class ModItems {
    public static final Item DIAMOND_SHARD = registerItem("diamond_shard", Item::new);
    public static final Item COPPER_SIFT_EMPTY = registerItem("copper_sift", properties -> new EmptyCopperSiftItem(properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_GRIME = registerItem("copper_sift_filled_grime", properties -> new CopperSiftFullGrimeItem(ModBlocks.GRIME, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_FERRISOIL = registerItem("copper_sift_filled_ferrisoil", properties -> new CopperSiftFullFerrisoilItem(ModBlocks.FERRISOIL, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_GOLDRUST = registerItem("copper_sift_filled_goldrust", properties -> new CopperSiftFullGoldrustItem(ModBlocks.GOLDRUST, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_PAYDIRT = registerItem("copper_sift_filled_paydirt", properties -> new CopperSiftFullGoldrustItem(ModBlocks.PAYDIRT, properties.stacksTo(1).durability(8)));
    public static final Item SIFT_GRUB_BUCKET = registerItem("sift_grub_bucket", properties -> new MobBucketItem(ModEntityTypes.SIFT_GRUB, Fluids.WATER, SoundEvents.BUCKET_EMPTY_TADPOLE, properties.stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));

    public static final Item SIFT_FLY_SPAWN_EGG = registerSpawnEgg(ModItemIds.SIFT_FLY_SPAWN_EGG, ModEntityTypes.SIFT_FLY);
    public static final Item SIFT_GRUB_SPAWN_EGG = registerSpawnEgg(ModItemIds.SIFT_GRUB_SPAWN_EGG, ModEntityTypes.SIFT_GRUB);

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name)))));
    }

    private static Item registerSpawnEgg(ResourceKey<Item> id, EntityType<?> type) {
        return registerItem(id, SpawnEggItem::new, new Item.Properties().spawnEgg(type));
    }

    private static Item registerItem(ResourceKey<Item> id, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(id));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void registerModItems(){
        GrimeAndGold.LOGGER.info("Registering Mod Items for " + GrimeAndGold.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(DIAMOND_SHARD);
        });
    }
}
