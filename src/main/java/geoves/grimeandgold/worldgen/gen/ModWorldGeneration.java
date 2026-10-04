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
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModPlacedFeatures.OVERWORLD_COOLED_COPPER_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModPlacedFeatures.OVERWORLD_COOLED_IRON_SLAG_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(ModTags.Biomes.IS_SNOWY_MOUNTAIN), GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ModPlacedFeatures.OVERWORLD_COOLED_GOLD_SLAG_PLACED_KEY);
    }
}
