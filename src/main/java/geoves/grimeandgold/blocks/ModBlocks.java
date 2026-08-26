package geoves.grimeandgold.blocks;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.custom.GrimeBlock;
import geoves.grimeandgold.blocks.custom.PayDirtBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block GRIME = registerBlock("grime", properties -> new GrimeBlock(properties.sound(SoundType.MUD).strength(0.5f, 0.5f)));
    public static final Block PAYDIRT = registerBlock("paydirt", properties -> new PayDirtBlock(properties.sound(SoundType.GRAVEL).strength(0.5f, 0.5f)));

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name), toRegister);
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