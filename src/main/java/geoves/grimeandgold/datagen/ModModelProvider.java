package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.custom.DecomposterBlock;
import geoves.grimeandgold.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.DIAMOND_SHARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_GRIME, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_FERRISOIL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_GOLDRUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_PAYDIRT, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_EMPTY, ModelTemplates.FLAT_ITEM);


    }
}
