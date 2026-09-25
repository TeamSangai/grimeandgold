package geoves.grimeandgold.tags;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SUPPORTS_SIFT_LARVA_EGGS = createTag("supports_sift_larva_eggs");
        public static final TagKey<Block> SIFT_LARVA_DESIRED = createTag("sift_larva_desired");
        public static final TagKey<Block> SIFT_LARVA_DISLIKED = createTag("sift_larva_disliked");
        public static final TagKey<Block> SUPPORTS_AQUATIC_FLOWERS = createTag("supports_aquatic_flowers");
        public static final TagKey<Block> SIFT_LARVA_CAN_TRANSMUTE = createTag("sift_larva_can_transmute");


        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> AQUATIC_FLOWERS = createTag("aquatic_flowers");
        public static final TagKey<Item> TALL_AQUATIC_FLOWERS = createTag("tall_aquatic_flowers");
        public static final TagKey<Item> DECOMPOSTABLE_VEG_LOW = createTag("decompostable_veg_low");
        public static final TagKey<Item> DECOMPOSTABLE_VEG_AVERAGE = createTag("decompostable_veg_average");
        public static final TagKey<Item> DECOMPOSTABLE_VEG_HIGH = createTag("decompostable_veg_high");
        public static final TagKey<Item> DECOMPOSTABLE_FLESH_LOW = createTag("decompostable_flesh_low");
        public static final TagKey<Item> DECOMPOSTABLE_FLESH_AVERAGE = createTag("decompostable_flesh_average");
        public static final TagKey<Item> DECOMPOSTABLE_FLESH_HIGH = createTag("decompostable_flesh_high");
        public static final TagKey<Item> DECOMPOSTABLE_CALCIUM_LOW = createTag("decompostable_calcium_low");
        public static final TagKey<Item> DECOMPOSTABLE_CALCIUM_AVERAGE = createTag("decompostable_calcium_average");
        public static final TagKey<Item> DECOMPOSTABLE_CALCIUM_AVERAGEINBUCKET = createTag("decompostable_calcium_average_inbucket");
        public static final TagKey<Item> DECOMPOSTABLE_CALCIUM_HIGH = createTag("decompostable_calcium_high");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        }
    }
}
