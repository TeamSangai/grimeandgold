package geoves.grimeandgold.worldgen;

import com.mojang.serialization.MapCodec;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.awt.*;
import java.util.List;

public class ModFeatures {

    public static final ResourceKey<Feature> BENTHIC_TREE_KEY = registerKey("benthic");
    public static final ResourceKey<Feature> OVERWORLD_IRON_SLAG = registerKey("overworld_iron_slag");
    public static final ResourceKey<Feature> NETHER_IRON_SLAG = registerKey("nether_iron_slag");
    public static final ResourceKey<Feature> END_IRON_SLAG = registerKey("end_iron_slag");
    public static final ResourceKey<Feature> OVERWORLD_GOLD_SLAG = registerKey("overworld_gold_slag");
    public static final ResourceKey<Feature> NETHER_GOLD_SLAG = registerKey("nether_gold_slag");
    public static final ResourceKey<Feature> END_GOLD_SLAG = registerKey("end_gold_slag");
    public static final ResourceKey<Feature> OVERWORLD_COPPER_SLAG = registerKey("overworld_copper_slag");
    public static final ResourceKey<Feature> NETHER_COPPER_SLAG = registerKey("nether_copper_slag");
    public static final ResourceKey<Feature> END_COPPER_SLAG = registerKey("end_copper_slag");


    public static void bootstrap(BootstrapContext<Feature> context) {
        RuleTest overworldCooledSlagReplaceables = new TagMatchTest(ModTags.Blocks.OVERWORLD_COOLED_SLAG_REPLACEABLE);
        RuleTest netherReplaceables = new TagMatchTest(ModTags.Blocks.NETHER_SLAG_REPLACEABLE);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        context.register(OVERWORLD_COPPER_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_COPPER_SLAG.defaultBlockState())), 6));
        context.register(OVERWORLD_IRON_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_IRON_SLAG.defaultBlockState())), 4));
        context.register(OVERWORLD_GOLD_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_GOLD_SLAG.defaultBlockState())), 5));

    }

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
    }

}
