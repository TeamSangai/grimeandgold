package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
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
                .add(ModBlocks.getRK(ModBlocks.COOLED_GOLD_SLAG));
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.getRK(ModBlocks.COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.GOLD_SLAG))
                .add(ModBlocks.getRK(ModBlocks.IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_COPPER_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_IRON_SLAG))
                .add(ModBlocks.getRK(ModBlocks.COOLED_GOLD_SLAG));
        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.getRK(ModBlocks.HULL_PLATING)).add(ModBlocks.getRK(ModBlocks.HULL_PLATING_SLAB))
                .add(ModBlocks.getRK(ModBlocks.HULL_PLATING_STAIRS));
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.getRK(ModBlocks.HULL_PLATING)).add(ModBlocks.getRK(ModBlocks.HULL_PLATING_STAIRS))
                .add(ModBlocks.getRK(ModBlocks.HULL_PLATING_SLAB));
        tag(ModTags.Blocks.SIFT_LARVA_CAN_TRANSMUTE).add(ModBlocks.getRK(ModBlocks.GRIME));
    }
}
