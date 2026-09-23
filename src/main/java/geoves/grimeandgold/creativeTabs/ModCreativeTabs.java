package geoves.grimeandgold.creativeTabs;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.items.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab GRIMEANDGOLD_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "grimeandgold"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COPPER_SIFT_FULL_GRIME))
                    .title(Component.translatable("creativemodetab.grimeandgold.grimeandgold"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.BIOMASS);
                        output.accept(ModBlocks.HULL_PLATING);
                        output.accept(ModBlocks.HULL_PLATING_SLAB);
                        output.accept(ModBlocks.HULL_PLATING_STAIRS);

                        output.accept(ModBlocks.BENTHIC_LOG);
                        output.accept(ModBlocks.BENTHIC_WOOD);

                        output.accept(ModBlocks.COOLED_COPPER_SLAG);
                        output.accept(ModBlocks.COPPER_SLAG);
                        output.accept(ModBlocks.COOLED_IRON_SLAG);
                        output.accept(ModBlocks.IRON_SLAG);
                        output.accept(ModBlocks.COOLED_GOLD_SLAG);
                        output.accept(ModBlocks.GOLD_SLAG);

                        output.accept(ModBlocks.DECOMPOSTER);
                        output.accept(ModBlocks.SLAG_FURNACE);

                        output.accept(ModBlocks.GRIMEBARREL);
                        output.accept(ModBlocks.FLY_NEST);

                        output.accept(ModBlocks.AQUATIC_SPIN_ROSE);
                        output.accept(ModBlocks.ANCHOR_BLOSSOM);
                        output.accept(ModBlocks.SPIRAL_DAFFODIL);
                        output.accept(ModItems.BENTHIC_BRANCH_ITEM);
                        output.accept(ModBlocks.BENTHIC_SAPLING);

                        output.accept(ModBlocks.DESERT_LAVENDER);
                        output.accept(ModBlocks.DESERT_PRIMROSE);
                        output.accept(ModBlocks.DESERT_POPPY);
                        output.accept(ModBlocks.GLOBE_THISTLE);
                        output.accept(ModBlocks.DRY_BUSH);
                        output.accept(ModBlocks.DRY_FERN);
                        output.accept(ModBlocks.DRY_TALL_FERN);

                        output.accept(ModItems.COPPER_SIFT_EMPTY);
                        output.accept(ModItems.COPPER_SIFT_FULL_GRIME);
                        output.accept(ModBlocks.GRIME_BRICKS);
                        output.accept(ModItems.COPPER_SIFT_FULL_FERRISOIL);
                        output.accept(ModItems.COPPER_SIFT_FULL_GOLDRUST);
                        output.accept(ModItems.COPPER_SIFT_FULL_PAYDIRT);

                        output.accept(ModItems.DIAMOND_SHARD);

                        output.accept(ModItems.SIFT_FLY_SPAWN_EGG);
                        output.accept(ModItems.SIFT_GRUB_SPAWN_EGG);
                    }).build());


    public static void registerModCreativeModeTabs() {
        GrimeAndGold.LOGGER.info("Registering Creative Mode Tabs for " + GrimeAndGold.MOD_ID);
    }
}
