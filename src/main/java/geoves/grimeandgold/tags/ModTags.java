package geoves.grimeandgold.tags;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SUPPORTS_SIFT_LARVA_EGGS = createTag("supports_sift_larva_eggs");
        public static final TagKey<Block> SIFT_LARVA_DESIRED = createTag("sift_larva_desired");
        public static final TagKey<Block> SIFT_LARVA_DISLIKED = createTag("sift_larva_disliked");
        public static final TagKey<Block> SUPPORTS_AQUATIC_FLOWERS = createTag("supports_aquatic_flowers");
        public static final TagKey<Block> SIFT_LARVA_CAN_TRANSMUTE = createTag("sift_larva_can_transmute");
        public static final TagKey<Block> TURNS_INTO_GRIME_BURROW = createTag("turns_into_grime_burrow");
        public static final TagKey<Block> TURNS_INTO_CLAY_BURROW = createTag("turns_into_clay_burrow");
        public static final TagKey<Block> TURNS_INTO_MUD_BURROW = createTag("turns_into_mud_burrow");
        public static final TagKey<Block> TURNS_INTO_SNOW_BURROW = createTag("turns_into_snow_burrow");
        public static final TagKey<Block> BURROWS = createTag("burrows");
        public static final TagKey<Block> AQUATIC_POLLINATOR_FLOWERS = createTag("aquatic_pollinator_flowers");
        public static final TagKey<Block> BENTHIC_LOGS = createTag("benthic_logs");
        public static final TagKey<Block> OVERWORLD_COOLED_SLAG_REPLACEABLE = createTag("overworld_cooled_slag_replaceable");
        public static final TagKey<Block> NETHER_SLAG_REPLACEABLE = createTag("nether_slag_replaceable");
        public static final TagKey<Block> PAYDIRT_REPLACEABLE = createTag("paydirt_replaceable");
        public static final TagKey<Block> FERRISOIL_REPLACEABLE = createTag("ferrisoil_replaceable");
        public static final TagKey<Block> GOLDRUST_REPLACEABLE = createTag("goldrust_replaceable");

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
        public static final TagKey<Item> SIFT_LARVA_STORES = createTag("sift_larva_stores");
        public static final TagKey<Item> BENTHIC_LOGS = createTag("benthic_logs");
        public static final TagKey<Item> ALL_COPPER_NAILS_ITEMS = createTag("all_copper_nails_items");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        }
    }
    public static class DamageTypes {
        public static final TagKey<DamageType> DAMAGES_BOOTS = createTag("damages_boots");

        private static TagKey<DamageType> createTag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        }
    }public static class Biomes {
        public static final TagKey<Biome> IS_SNOWY_MOUNTAIN = createTag("is_snowy_mountain");

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        }
    }
}
