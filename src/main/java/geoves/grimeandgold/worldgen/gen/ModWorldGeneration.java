package geoves.grimeandgold.worldgen.gen;

import geoves.grimeandgold.tags.ModTags;
import geoves.grimeandgold.worldgen.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModWorldGeneration {

    public static void generateModWorldGen() {
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.HAS_VILLAGE_DESERT), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.PAYDIRT_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_BADLANDS), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.PAYDIRT_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.FERRISOIL_PLACED);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_LAPIS_ROUGH_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_EMERALD_ROUGH_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_DIAMOND_ROUGH_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_QUARTZ_ROUGH_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_COOLED_COPPER_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_COOLED_GOLD_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.END_COOLED_IRON_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_COPPER_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_GOLD_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ModPlacedFeatures.NETHER_IRON_SLAG_PLACED_KEY);

    }
}
