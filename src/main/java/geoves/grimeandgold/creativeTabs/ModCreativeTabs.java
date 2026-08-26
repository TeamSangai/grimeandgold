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
            Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "fluorite_blocks"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COPPER_SIFT_FULL_GRIME))
                    .title(Component.translatable("creativemodetab.grimeandgold.grimeandgold"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.COPPER_SIFT_EMPTY);
                        output.accept(ModItems.COPPER_SIFT_FULL_GRIME);
                        output.accept(ModBlocks.PAYDIRT);
                        output.accept(ModItems.DIAMOND_SHARD);


                    }).build());


    public static void registerModCreativeModeTabs() {
        GrimeAndGold.LOGGER.info("Registering Creative Mode Tabs for " + GrimeAndGold.MOD_ID);
    }
}
