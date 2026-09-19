package geoves.grimeandgold.datagen;

import geoves.grimeandgold.entities.ModEntityTypeIds;
import geoves.grimeandgold.entities.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public ModEntityTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.AQUATIC).add(ModEntityTypeIds.SIFT_GRUB);
        this.tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add(ModEntityTypeIds.SIFT_GRUB);
    }
}
