package geoves.grimeandgold.blocks.entities;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class ModBlockEntities {
    public static final BlockEntityType<GrimeBarrelBlockEntity> GRIMEBARREL_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "grimebarrel_be"),
                    FabricBlockEntityTypeBuilder.create(GrimeBarrelBlockEntity::new, ModBlocks.GRIMEBARREL).build());


    public static void registerBlockEntities() {
        GrimeAndGold.LOGGER.info("Registering ModBlockEntities for " + GrimeAndGold.MOD_ID);
    }
}
