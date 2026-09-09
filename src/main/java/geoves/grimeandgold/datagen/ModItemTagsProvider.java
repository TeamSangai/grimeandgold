package geoves.grimeandgold.datagen;

import geoves.grimeandgold.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.nbt.Tag;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ModTags.Items.DECOMPOSTABLE_CALCIUM_LOW).add(ItemIds.BONE_MEAL);
        tag(ModTags.Items.DECOMPOSTABLE_FLESH_LOW).add(ItemIds.ROTTEN_FLESH);
        tag(ModTags.Items.DECOMPOSTABLE_VEG_LOW).add(BlockItemIds.WHEAT_CROP.item());

        tag(ModTags.Items.DECOMPOSTABLE_VEG_AVERAGE).add(BlockItemIds.CARROT_CROP.item());
        tag(ModTags.Items.DECOMPOSTABLE_CALCIUM_AVERAGE).add(ItemIds.BONE);

        tag(ModTags.Items.DECOMPOSTABLE_CALCIUM_AVERAGEINBUCKET).add(ItemIds.MILK_BUCKET);


        tag(ModTags.Items.DECOMPOSTABLE_CALCIUM_HIGH).add(BlockItemIds.BONE_BLOCK.item());

    }
}
