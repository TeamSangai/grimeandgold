package geoves.grimeandgold.worldgen;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY = registerKey("overworld_cooled_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY = registerKey("overworld_cooled_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY = registerKey("overworld_cooled_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_IRON_SLAG_PLACED_KEY = registerKey("end_cooled_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_GOLD_SLAG_PLACED_KEY = registerKey("end_cooled_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> END_COOLED_COPPER_SLAG_PLACED_KEY = registerKey("end_cooled_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> END_DIAMOND_ROUGH_PLACED_KEY = registerKey("end_diamond_rough_placed");
    public static final ResourceKey<PlacedFeature> END_EMERALD_ROUGH_PLACED_KEY = registerKey("end_emerald_rough_placed");
    public static final ResourceKey<PlacedFeature> END_LAPIS_ROUGH_PLACED_KEY = registerKey("end_lapis_rough_placed");
    public static final ResourceKey<PlacedFeature> END_QUARTZ_ROUGH_PLACED_KEY = registerKey("end_quartz_rough_placed");
    public static final ResourceKey<PlacedFeature> NETHER_IRON_SLAG_PLACED_KEY = registerKey("nether_iron_slag_placed");
    public static final ResourceKey<PlacedFeature> NETHER_GOLD_SLAG_PLACED_KEY = registerKey("nether_gold_slag_placed");
    public static final ResourceKey<PlacedFeature> NETHER_COPPER_SLAG_PLACED_KEY = registerKey("nether_copper_slag_placed");
    public static final ResourceKey<PlacedFeature> PAYDIRT_PLACED = registerKey("paydirt_placed");
    public static final ResourceKey<PlacedFeature> GOLDRUST_PLACED = registerKey("goldrust_placed");
    public static final ResourceKey<PlacedFeature> FERRISOIL_PLACED = registerKey("ferrisoil_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var features = context.lookup(Registries.FEATURE);

        List<PlacementModifier> commonSlagVeinModifiers = List.of(
                CountPlacement.of(50),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.relativeToSeaLevel(24), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> uncommonSlagVeinModifiers = List.of(
                CountPlacement.of(40),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.relativeToSeaLevel(24), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> paydirtVeinModifiers = List.of(
                CountPlacement.of(30),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(32), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> ferrisoilVeinModifiers = List.of(
                CountPlacement.of(15),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.relativeToSeaLevel(-32))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> commonEndVeinModifiers = List.of(
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> unCommonEndVeinModifiers = List.of(
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> rareEndVeinModifiers = List.of(
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );
        List<PlacementModifier> grimeVeinModifiers = List.of(
                CountPlacement.of(30),
                InSquarePlacement.spread(),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0))),
                BiomeFilter.biome()
        );

        register(context, OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_COPPER_SLAG), commonSlagVeinModifiers);
        register(context, OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_IRON_SLAG), uncommonSlagVeinModifiers);
        register(context, OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.OVERWORLD_GOLD_SLAG), commonSlagVeinModifiers);
        register(context, PAYDIRT_PLACED, features.getOrThrow(ModFeatures.PAYDIRT), paydirtVeinModifiers);
        register(context, GOLDRUST_PLACED, features.getOrThrow(ModFeatures.GOLDRUST), paydirtVeinModifiers);
        register(context, FERRISOIL_PLACED, features.getOrThrow(ModFeatures.FERRISOIL), ferrisoilVeinModifiers);

        register(context, NETHER_COPPER_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.NETHER_COPPER_SLAG), unCommonEndVeinModifiers);
        register(context, NETHER_GOLD_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.NETHER_GOLD_SLAG), commonEndVeinModifiers);
        register(context, NETHER_IRON_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.NETHER_IRON_SLAG), rareEndVeinModifiers);

        register(context, END_DIAMOND_ROUGH_PLACED_KEY, features.getOrThrow(ModFeatures.END_DIAMOND_ROUGH), rareEndVeinModifiers);
        register(context, END_EMERALD_ROUGH_PLACED_KEY, features.getOrThrow(ModFeatures.END_EMERALD_ROUGH), unCommonEndVeinModifiers);
        register(context, END_LAPIS_ROUGH_PLACED_KEY, features.getOrThrow(ModFeatures.END_LAPIS_ROUGH), unCommonEndVeinModifiers);
        register(context, END_QUARTZ_ROUGH_PLACED_KEY, features.getOrThrow(ModFeatures.END_QUARTZ_ROUGH), commonEndVeinModifiers);
        register(context, END_COOLED_COPPER_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.END_COPPER_SLAG), commonEndVeinModifiers);
        register(context, END_COOLED_GOLD_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.END_GOLD_SLAG), unCommonEndVeinModifiers);
        register(context, END_COOLED_IRON_SLAG_PLACED_KEY, features.getOrThrow(ModFeatures.END_IRON_SLAG), rareEndVeinModifiers);
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<Feature> featureHolder, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(featureHolder, List.copyOf(modifiers)));
    }
}
