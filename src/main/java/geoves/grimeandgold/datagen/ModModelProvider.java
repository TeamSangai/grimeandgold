package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;

import geoves.grimeandgold.blocks.custom.DecomposterBlock;
import geoves.grimeandgold.datagen.util.C6;
import geoves.grimeandgold.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;


public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(ModBlocks.COOLED_COPPER_SLAG);blockModelGenerators.createTrivialCube(ModBlocks.COOLED_IRON_SLAG);
        blockModelGenerators.createTrivialCube(ModBlocks.COOLED_GOLD_SLAG);blockModelGenerators.createTrivialCube(ModBlocks.COPPER_SLAG);
        blockModelGenerators.createTrivialCube(ModBlocks.IRON_SLAG);blockModelGenerators.createTrivialCube(ModBlocks.GOLD_SLAG);

        blockModelGenerators.family(ModBlocks.HULL_PLATING).slab(ModBlocks.HULL_PLATING_SLAB).stairs(ModBlocks.HULL_PLATING_STAIRS);

        blockModelGenerators.createFurnace(ModBlocks.SLAG_FURNACE, TexturedModel.ORIENTABLE);

        blockModelGenerators.createFlowerBed(ModBlocks.DESERT_POPPY);

        blockModelGenerators.createCoralFans(ModBlocks.BENTHIC_BRANCH, ModBlocks.BENTHIC_WALL_BRANCH);

        blockModelGenerators.createCrossBlock(ModBlocks.BENTHIC_SAPLING, BlockModelGenerators.PlantType.NOT_TINTED);blockModelGenerators.createCrossBlock(ModBlocks.DESERT_LAVENDER, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createCrossBlock(ModBlocks.DESERT_PRIMROSE, BlockModelGenerators.PlantType.NOT_TINTED);blockModelGenerators.createCrossBlock(ModBlocks.GLOBE_THISTLE, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createCrossBlock(ModBlocks.DRY_BUSH, BlockModelGenerators.PlantType.NOT_TINTED);blockModelGenerators.createCrossBlock(ModBlocks.DRY_TALL_FERN, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createCrossBlock(ModBlocks.DRY_FERN, BlockModelGenerators.PlantType.NOT_TINTED);
        //   blockModelGenerators.blockStateOutput.accept(
      //          MultiVariantGenerator.dispatch(ModBlocks.DECOMPOSTER).with(
       //                 C6.initial(
       //                         DecomposterBlock.FACING,
        //                        DecomposterBlock.ACTIVE,
       //                         DecomposterBlock.DONE,
         //                       DecomposterBlock.VEGETATION,
         //                       DecomposterBlock.CALCIUM,
         //                       DecomposterBlock.FLESH
         //               ).generate((facing, active, done, veg, calc, flesh) -> BlockModelGenerators.plainVariant(TexturedModel.ORIENTABLE.createWithSuffix(
         //                       ModBlocks.DECOMPOSTER,
         //                       "_" + (active ? "active" : "inactive") + "_" + (done ? "complete" : "incomplete") + "_" + veg + "_" + calc + "_" + flesh,
         //                       blockModelGenerators.modelOutput
         //               )))
        //        )
       // );
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModBlocks.DRY_TALL_FERN.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.BENTHIC_SAPLING.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.DRY_FERN.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.DESERT_LAVENDER.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.DESERT_PRIMROSE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.GLOBE_THISTLE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.DRY_BUSH.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DIAMOND_SHARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_GRIME, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_FERRISOIL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_GOLDRUST, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_FULL_PAYDIRT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SIFT_EMPTY, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModBlocks.AQUATIC_SPIN_ROSE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SIFT_FLY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SIFT_GRUB_BUCKET, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SIFT_GRUB_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }
}
