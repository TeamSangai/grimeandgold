package geoves.grimeandgold.worldgen;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.custom.BenthicBranchBlock;
import geoves.grimeandgold.blocks.custom.BenthicWallBranchBlock;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.PoplarTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.lwjgl.system.ffm.mapping.Mapping;

import java.util.List;

import static net.minecraft.core.Direction.*;

public class ModFeatures {

    public static final ResourceKey<Feature> BENTHIC_TREE_KEY = registerKey("benthic_fallen_tree");
    public static final ResourceKey<Feature> GRIME_FEATURE_KEY = registerKey("grime_feature");
    public static final ResourceKey<Feature> WARM_OCEAN_GRAVEL_FEATURE_KEY = registerKey("warm_ocean_gravel_feature");

    public static final ResourceKey<Feature> OVERWORLD_IRON_SLAG = registerKey("overworld_iron_slag");
    public static final ResourceKey<Feature> NETHER_IRON_SLAG = registerKey("nether_iron_slag");
    public static final ResourceKey<Feature> END_IRON_SLAG = registerKey("end_iron_slag");
    public static final ResourceKey<Feature> OVERWORLD_GOLD_SLAG = registerKey("overworld_gold_slag");
    public static final ResourceKey<Feature> NETHER_GOLD_SLAG = registerKey("nether_gold_slag");
    public static final ResourceKey<Feature> END_GOLD_SLAG = registerKey("end_gold_slag");
    public static final ResourceKey<Feature> OVERWORLD_COPPER_SLAG = registerKey("overworld_copper_slag");
    public static final ResourceKey<Feature> NETHER_COPPER_SLAG = registerKey("nether_copper_slag");
    public static final ResourceKey<Feature> END_COPPER_SLAG = registerKey("end_copper_slag");

    public static final ResourceKey<Feature> END_DIAMOND_ROUGH = registerKey("end_diamond_rough");
    public static final ResourceKey<Feature> END_EMERALD_ROUGH = registerKey("end_emerald_rough");
    public static final ResourceKey<Feature> END_LAPIS_ROUGH = registerKey("end_lapis_rough");
    public static final ResourceKey<Feature> END_QUARTZ_ROUGH = registerKey("end_quartz_rough");

    public static final ResourceKey<Feature> PAYDIRT = registerKey("paydirt");
    public static final ResourceKey<Feature> GOLDRUST = registerKey("goldrust");
    public static final ResourceKey<Feature> FERRISOIL = registerKey("ferrisoil");


    public static void bootstrap(BootstrapContext<Feature> context) {
        RuleTest overworldCooledSlagReplaceables = new TagMatchTest(ModTags.Blocks.OVERWORLD_COOLED_SLAG_REPLACEABLE);
        RuleTest paydirtReplaceables = new TagMatchTest(ModTags.Blocks.PAYDIRT_REPLACEABLE);
        RuleTest goldrustReplaceables = new TagMatchTest(ModTags.Blocks.GOLDRUST_REPLACEABLE);
        RuleTest ferrisoilReplaceables = new TagMatchTest(ModTags.Blocks.FERRISOIL_REPLACEABLE);
        RuleTest netherSlagReplaceables = new TagMatchTest(ModTags.Blocks.NETHER_SLAG_REPLACEABLE);
        RuleTest grimeReplaceables = new TagMatchTest(ModTags.Blocks.GRIME_REPLACEABLE);
        RuleTest warmOceanGravelReplaceables = new TagMatchTest(ModTags.Blocks.WARM_OCEAN_GRAVEL_REPLACEABLE);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);


        context.register(BENTHIC_TREE_KEY, new TreeFeature.Builder(BlockStateProvider.of(ModBlocks.BENTHIC_LOG),
                new PoplarTrunkPlacer(4, 2, 4, UniformInt.of(4,6), UniformInt.of(1,3)), BlockStateProvider.of(ModBlocks.BENTHIC_BRAMBLES),
                new CherryFoliagePlacer(UniformInt.of(0, 3), UniformInt.of(0, 3), UniformInt.of(4, 6), 0.4f, 0.4f, 0.3f, 0.3f),
                new TwoLayersFeatureSize(1,0,2), BlockStateProvider.holderOf(ModBlocks.GRIME)).build());
        context.register(WARM_OCEAN_GRAVEL_FEATURE_KEY, new OreFeature(List.of(BlockReplacement.replace(warmOceanGravelReplaceables, Blocks.GRAVEL.defaultBlockState())), 24));
        context.register(GRIME_FEATURE_KEY, new OreFeature(List.of(BlockReplacement.replace(grimeReplaceables, ModBlocks.GRIME.defaultBlockState())), 18));

        context.register(OVERWORLD_COPPER_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_COPPER_SLAG.defaultBlockState())), 6));
        context.register(OVERWORLD_IRON_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_IRON_SLAG.defaultBlockState())), 4));
        context.register(OVERWORLD_GOLD_SLAG, new OreFeature(List.of(BlockReplacement.replace(overworldCooledSlagReplaceables, ModBlocks.COOLED_GOLD_SLAG.defaultBlockState())), 5));

        context.register(NETHER_COPPER_SLAG, new OreFeature(List.of(BlockReplacement.replace(netherSlagReplaceables, ModBlocks.COPPER_SLAG.defaultBlockState())), 12));
        context.register(NETHER_IRON_SLAG, new OreFeature(List.of(BlockReplacement.replace(netherSlagReplaceables, ModBlocks.IRON_SLAG.defaultBlockState())), 9));
        context.register(NETHER_GOLD_SLAG, new OreFeature(List.of(BlockReplacement.replace(netherSlagReplaceables, ModBlocks.GOLD_SLAG.defaultBlockState())), 14));

        context.register(END_GOLD_SLAG, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.COOLED_GOLD_SLAG.defaultBlockState())), 8));
        context.register(END_COPPER_SLAG, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.COOLED_COPPER_SLAG.defaultBlockState())), 10));
        context.register(END_IRON_SLAG, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.COOLED_IRON_SLAG.defaultBlockState())), 6));
        context.register(END_DIAMOND_ROUGH, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.DIAMOND_ROUGH.defaultBlockState())), 5));
        context.register(END_EMERALD_ROUGH, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.EMERALD_ROUGH.defaultBlockState())), 7));
        context.register(END_LAPIS_ROUGH, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.LAPIS_ROUGH.defaultBlockState())), 9));
        context.register(END_QUARTZ_ROUGH, new OreFeature(List.of(BlockReplacement.replace(endReplaceables, ModBlocks.QUARTZ_ROUGH.defaultBlockState())), 10));

        context.register(PAYDIRT, new OreFeature(List.of(BlockReplacement.replace(paydirtReplaceables, ModBlocks.PAYDIRT.defaultBlockState())), 3));
        context.register(GOLDRUST, new OreFeature(List.of(BlockReplacement.replace(goldrustReplaceables, ModBlocks.GOLDRUST.defaultBlockState())), 5));
        context.register(FERRISOIL, new OreFeature(List.of(BlockReplacement.replace(ferrisoilReplaceables, ModBlocks.FERRISOIL.defaultBlockState())), 6));

    }

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
    }

}
