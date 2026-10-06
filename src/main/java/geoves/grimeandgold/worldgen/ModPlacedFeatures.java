package geoves.grimeandgold.worldgen;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY = registerKey("overworld_cooled_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY = registerKey("overworld_cooled_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY = registerKey("overworld_cooled_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_IRON_SLAG_PLACED_KEY = registerKey("end_cooled_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_GOLD_SLAG_PLACED_KEY = registerKey("end_cooled_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_COPPER_SLAG_PLACED_KEY = registerKey("end_cooled_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> NETHER_IRON_SLAG_PLACED_KEY = registerKey("nether_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> NETHER_GOLD_SLAG_PLACED_KEY = registerKey("nether_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> NETHER_COPPER_SLAG_PLACED_KEY = registerKey("nether_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> PAYDIRT_PLACED = registerKey("paydirt_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var features = context.lookup(Registries.FEATURE);

        register(context, OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_COPPER_SLAG),
                OrePlacements.commonOrePlacement(12,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(250))));
        register(context, OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_IRON_SLAG),
                OrePlacements.commonOrePlacement(7,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(250))));
        register(context, OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_GOLD_SLAG),
                OrePlacements.commonOrePlacement(9,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(250))));
        register(context, PAYDIRT_PLACED, features.getOrThrow(ModFeatures.PAYDIRT),
                OrePlacements.commonOrePlacement(25,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(250))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<Feature> featureHolder, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(featureHolder, List.copyOf(modifiers)));
    }
}
