package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;


import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.getRK(ModBlocks.COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.GOLD_SLAG))
                .add(ModBlocks.getRK(ModBlocks.IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_GOLD_SLAG))
                .add(ModBlocks.getRK(ModBlocks.EMERALD_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.QUARTZ_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.LAPIS_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.DIAMOND_ROUGH));
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.getRK(ModBlocks.COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.GOLD_SLAG))
                .add(ModBlocks.getRK(ModBlocks.IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_GOLD_SLAG));
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.getRK(ModBlocks.EMERALD_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.QUARTZ_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.LAPIS_ROUGH))
                .add(ModBlocks.getRK(ModBlocks.DIAMOND_ROUGH));
        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.getRK(ModBlocks.HULL_PLATING)).add(ModBlocks.getRK(ModBlocks.HULL_PLATING_SLAB))
                .add(ModBlocks.getRK(ModBlocks.HULL_PLATING_STAIRS));
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.getRK(ModBlocks.HULL_PLATING)).add(ModBlocks.getRK(ModBlocks.HULL_PLATING_STAIRS))
                .add(ModBlocks.getRK(ModBlocks.HULL_PLATING_SLAB));
        tag(ModTags.Blocks.SIFT_LARVA_CAN_TRANSMUTE).add(ModBlocks.getRK(ModBlocks.GRIME));
        tag(ModTags.Blocks.TURNS_INTO_CLAY_BURROW).add(Objects.requireNonNull(Blocks.CLAY.properties().blockId()));
        tag(ModTags.Blocks.TURNS_INTO_MUD_BURROW).add(Objects.requireNonNull(Blocks.MUD.properties().blockId()));
        tag(ModTags.Blocks.TURNS_INTO_SNOW_BURROW).add(Objects.requireNonNull(Blocks.SNOW_BLOCK.properties().blockId()));
        tag(ModTags.Blocks.TURNS_INTO_GRIME_BURROW).add(ModBlocks.getRK(ModBlocks.GRIME));
        tag(ModTags.Blocks.AQUATIC_POLLINATOR_FLOWERS).add(ModBlocks.getRK(ModBlocks.AQUATIC_SPIN_ROSE)).add(ModBlocks.getRK(ModBlocks.GHOST_OF_THE_SEA))
                .add(ModBlocks.getRK(ModBlocks.ANCHOR_BLOSSOM)).add(ModBlocks.getRK(ModBlocks.SPIRAL_DAFFODIL)).add(ModBlocks.getRK(ModBlocks.DEEP_SEA_ROCKET));
        tag(ModTags.Blocks.BENTHIC_LOGS).add(ModBlocks.getRK(ModBlocks.BENTHIC_LOG)).add(ModBlocks.getRK(ModBlocks.BENTHIC_WOOD));

        tag(ModTags.Blocks.OVERWORLD_COOLED_SLAG_REPLACEABLE).add(Objects.requireNonNull(Blocks.SNOW_BLOCK.properties().blockId()))
                .add(Objects.requireNonNull(Blocks.POWDER_SNOW.properties().blockId()));
        tag(ModTags.Blocks.NETHER_SLAG_REPLACEABLE).add(Objects.requireNonNull(Blocks.SOUL_SAND.properties().blockId()))
                .add(Objects.requireNonNull(Blocks.SOUL_SAND.properties().blockId())).add(Objects.requireNonNull(Blocks.GRAVEL.properties().blockId()));

    }
}
