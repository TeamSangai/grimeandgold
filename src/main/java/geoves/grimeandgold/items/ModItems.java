package geoves.grimeandgold.items;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.items.custom.CopperSiftFullFerrisoilItem;
import geoves.grimeandgold.items.custom.CopperSiftFullGoldrustItem;
import geoves.grimeandgold.items.custom.EmptyCopperSiftItem;
import geoves.grimeandgold.items.custom.CopperSiftFullGrimeItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.function.Function;

public class ModItems {
    public static final Item DIAMOND_SHARD = registerItem("diamond_shard", Item::new);
    public static final Item QUARTZ_SHARD = registerItem("quartz_shard", Item::new);
    public static final Item EMERALD_SHARD = registerItem("emerald_shard", Item::new);
    public static final Item LAPIS_MOTE = registerItem("lapis_mote", Item::new);
    public static final Item CHARCOAL_MOTE = registerItem("charcoal_mote", properties -> new Item(properties.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_SMALL)));
    public static final Item FERROBEET_MELODY = registerItem("ferrobeet_melody", Item::new);
    public static final Item BEETROOT_PADDY = registerItem("beetroot_paddy", properties -> new Item(properties.food(new FoodProperties(3, 0.2f, false))));
    public static final Item COOKED_BEETROOT_PADDY = registerItem("cooked_beetroot_paddy", properties -> new Item(properties.food(new FoodProperties(5, 0.3f, false))));
    public static final Item BEETROOT_BURGER = registerItem("beetroot_burger", properties -> new Item(properties.food(new FoodProperties(8, 1.0f, false))));
    public static final Item COPPER_SIFT_EMPTY = registerItem("copper_sift", properties -> new EmptyCopperSiftItem(properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_GRIME = registerItem("copper_sift_filled_grime", properties -> new CopperSiftFullGrimeItem(ModBlocks.GRIME, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_FERRISOIL = registerItem("copper_sift_filled_ferrisoil", properties -> new CopperSiftFullFerrisoilItem(ModBlocks.FERRISOIL, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_GOLDRUST = registerItem("copper_sift_filled_goldrust", properties -> new CopperSiftFullGoldrustItem(ModBlocks.GOLDRUST, properties.stacksTo(1).durability(8)));
    public static final Item COPPER_SIFT_FULL_PAYDIRT = registerItem("copper_sift_filled_paydirt", properties -> new CopperSiftFullGoldrustItem(ModBlocks.PAYDIRT, properties.stacksTo(1).durability(8)));
    public static final Item SIFT_GRUB_BUCKET = registerItem("sift_grub_bucket", properties -> new MobBucketItem(ModEntityTypes.SIFT_GRUB, Fluids.WATER, SoundEvents.BUCKET_EMPTY_TADPOLE, properties.stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
    public static final Item BENTHIC_BRANCH_ITEM = registerItem("benthic_branch_item", properties -> new StandingAndWallBlockItem(ModBlocks.BENTHIC_BRANCH, ModBlocks.BENTHIC_WALL_BRANCH, Direction.DOWN, properties));

    public static final Item AQUATIC_MIXED_SEEDS = registerItem("aquatic_mixed_seeds", properties -> new BlockItem(ModBlocks.AQUATIC_MIXED_CROP, properties));
    public static final Item DESERT_MIXED_SEEDS = registerItem("desert_mixed_seeds", properties -> new BlockItem(ModBlocks.DESERT_MIXED_CROP, properties));

    public static final Item ITEM_COPPER_NAILS = registerItem("copper_nails_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS, properties));
    public static final Item ITEM_COPPER_NAILS_EXPOSED = registerItem("copper_nails_exposed_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_EXPOSED, properties));
    public static final Item ITEM_COPPER_NAILS_WEATHERED = registerItem("copper_nails_weathered_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_WEATHERED, properties));
    public static final Item ITEM_COPPER_NAILS_OXIDIZED = registerItem("copper_nails_oxidized_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_OXIDIZED, properties));
    public static final Item ITEM_COPPER_NAILS_WAXED = registerItem("copper_nails_waxed_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_WAXED, properties));
    public static final Item ITEM_COPPER_NAILS_WAXED_EXPOSED = registerItem("copper_nails_waxed_exposed_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_WAXED_EXPOSED, properties));
    public static final Item ITEM_COPPER_NAILS_WAXED_WEATHERED = registerItem("copper_nails_waxed_weathered_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_WAXED_WEATHERED, properties));
    public static final Item ITEM_COPPER_NAILS_WAXED_OXIDIZED = registerItem("copper_nails_waxed_oxidized_item", properties -> new BlockItem(ModBlocks.COPPER_NAILS_WAXED_OXIDIZED, properties));

    public static final Item SIFT_FLY_SPAWN_EGG = registerSpawnEgg(ModItemIds.SIFT_FLY_SPAWN_EGG, ModEntityTypes.SIFT_FLY);
    public static final Item SIFT_GRUB_SPAWN_EGG = registerSpawnEgg(ModItemIds.SIFT_GRUB_SPAWN_EGG, ModEntityTypes.SIFT_GRUB);

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name)))));
    }

    private static Item registerSpawnEgg(ResourceKey<Item> id, EntityType<?> type) {
        return registerItem(id, SpawnEggItem::new, new Item.Properties().spawnEgg(type));
    }

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
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
