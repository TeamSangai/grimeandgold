package geoves.grimeandgold.blocks;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class ModBlocks {
    public static final Block BIOMASS = registerBlock("biomass", properties -> new BiomassBlock(properties.sound(SoundType.WET_SPONGE)));
    public static final Block GRIME = registerBlock("grime", properties -> new GrimeBlock(properties.sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_CYAN).strength(0.5f, 0.5f)));
    public static final Block GRIME_BRICKS = registerBlock("grime_bricks", properties -> new Block(properties.sound(SoundType.PACKED_MUD).mapColor(MapColor.TERRACOTTA_CYAN).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final Block PAYDIRT = registerBlock("paydirt", properties -> new PayDirtBlock(properties.sound(SoundType.GRAVEL).strength(0.5f, 0.5f)));
    public static final Block FERRISOIL = registerBlock("ferrisoil", properties -> new FerrisoilBlock(properties.sound(SoundType.GRAVEL).strength(0.6f, 0.75f)));
    public static final Block GOLDRUST = registerBlock("goldrust", properties -> new GoldrustBlock(properties.sound(SoundType.GRAVEL).strength(0.6f, 0.75f)));


    public static final Block GOLD_SLAG = registerBlock("gold_slag", properties -> new MagmaBlock(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(2.0f, 1.5f)));
    public static final Block IRON_SLAG = registerBlock("iron_slag", properties -> new MagmaBlock(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(2.0f, 1.5f)));
    public static final Block COPPER_SLAG = registerBlock("copper_slag", properties -> new MagmaBlock(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(2.0f, 1.5f)));
    public static final Block COOLED_GOLD_SLAG = registerBlock("cooled_gold_slag", properties -> new Block(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(3.0f, 3.5f)));
    public static final Block COOLED_IRON_SLAG = registerBlock("cooled_iron_slag", properties -> new Block(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(3.0f, 3.5f)));
    public static final Block COOLED_COPPER_SLAG = registerBlock("cooled_copper_slag", properties -> new Block(properties.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops().strength(3.0f, 3.5f)));


    public static final Block DECOMPOSTER = registerBlock("decomposter", properties -> new DecomposterBlock(properties.sound(SoundType.COPPER_GRATE).strength(0.6f, 0.75f).requiresCorrectToolForDrops()));
    public static final Block SLAG_FURNACE = registerBlock("slag_furnace", properties -> new SlagFurnaceBlock(properties.sound(SoundType.COPPER_GRATE).strength(0.6f, 0.75f).requiresCorrectToolForDrops()));

    public static final Block GRIMEBARREL = registerBlock("grimebarrel", properties -> new GrimeBarrelBlock(properties.sound(SoundType.MUD).mapColor(MapColor.TERRACOTTA_CYAN).strength(0.6f, 0.75f)));
    public static final Block FLY_NEST = registerBlock("fly_nest", properties -> new FlynestBlock(properties.sound(SoundType.PACKED_MUD).mapColor(MapColor.TERRACOTTA_CYAN).strength(1.0f, 3.0f)));
    public static final Block FLY_LARVA_EGG = registerBlock("sift_larva_egg", properties -> new SiftLarvaEggBlock(properties.mapColor(MapColor.WATER).instabreak().noOcclusion().sound(SoundType.FROGSPAWN).pushReaction(PushReaction.POPPED)));

    public static final Block AQUATIC_SPIN_ROSE = registerBlock("aquatic_spin_rose", properties -> new AquaticFlowerBlock(MobEffects.DOLPHINS_GRACE, 4.0F, properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block ANCHOR_BLOSSOM = registerBlock("anchor_blossom", properties -> new DoubleAquaticFlowerBlock(properties.mapColor(MapColor.WATER).replaceable().noCollision().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));
    public static final Block SPIRAL_DAFFODIL = registerBlock("spiral_daffodil", properties -> new SpiralDafodilBlock(properties.mapColor(MapColor.WATER).replaceable().noCollision().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));

    public static final Block BENTHIC_LOG = registerBlock("benthic_log", properties -> new RotatedPillarBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block BENTHIC_WOOD = registerBlock("benthic_wood", properties -> new RotatedPillarBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block BENTHIC_BRANCH = registerBlock("benthic_branch", properties -> new BenthicBranchBlock(properties.mapColor(MapColor.STONE).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block BENTHIC_WALL_BRANCH = registerBlock("benthic_wall_branch", properties -> new BenthicWallBranchBlock(properties.mapColor(MapColor.STONE).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block BENTHIC_SAPLING = registerBlock("benthic_sapling", properties -> new BenthicSaplingBlock(TreeGrower.MANGROVE, properties.mapColor(MapColor.STONE).instabreak().noCollision().sound(SoundType.WOOD).ignitedByLava()));

    public static final Block DESERT_LAVENDER = registerBlock("desert_lavender", properties -> new DryFlowerBlock(MobEffects.ABSORPTION, 60.0F, properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block GLOBE_THISTLE = registerBlock("globe_thistle", properties -> new DryFlowerBlock(MobEffects.INSTANT_HEALTH, 0.4F, properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block DESERT_PRIMROSE = registerBlock("desert_primrose", properties -> new DryFlowerBlock(MobEffects.INSTANT_HEALTH, 0.4F, properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block DESERT_POPPY = registerBlock("desert_poppy", properties -> new DryFlowerBedBlock(properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.POPPED), 2));;
    public static final Block DRY_BUSH = registerBlock("dry_bush", properties -> new DryBushBlock(properties.mapColor(MapColor.COLOR_YELLOW).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block DRY_FERN = registerBlock("dry_fern", properties -> new DryFernBlock(properties.mapColor(MapColor.COLOR_YELLOW).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;
    public static final Block DRY_TALL_FERN = registerBlock("dry_tall_fern", properties -> new TallDryFernBlock(properties.mapColor(MapColor.COLOR_YELLOW).noCollision().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED)));;

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name), toRegister);
    }

    public static ResourceKey<Block> getRK(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        GrimeAndGold.LOGGER.info("Registering Mod Blocks for " + GrimeAndGold.MOD_ID);
    }
}