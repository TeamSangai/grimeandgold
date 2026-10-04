package geoves.grimeandgold.blocks.entities;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class ModBlockEntities {
    public static final BlockEntityType<GrimeBarrelBlockEntity> GRIMEBARREL_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "grimebarrel_be"),
                    FabricBlockEntityTypeBuilder.create(GrimeBarrelBlockEntity::new, ModBlocks.GRIMEBARREL).build());

    public static final BlockEntityType<MudBurrowBlockEntity> MUD_BURROW_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "mud_burrow_be"),
                    FabricBlockEntityTypeBuilder.create(MudBurrowBlockEntity::new, ModBlocks.MUD_BURROW).build());

    public static final BlockEntityType<ClayBurrowBlockEntity> CLAY_BURROW_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "clay_burrow_be"),
                    FabricBlockEntityTypeBuilder.create(ClayBurrowBlockEntity::new, ModBlocks.CLAY_BURROW).build());

    public static final BlockEntityType<SnowBurrowBlockEntity> SNOW_BURROW_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "snow_burrow_be"),
                    FabricBlockEntityTypeBuilder.create(SnowBurrowBlockEntity::new, ModBlocks.SNOW_BURROW).build());

    public static final BlockEntityType<FrosterBlockEntity> FROSTER_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "froster_be"),
                    FabricBlockEntityTypeBuilder.create(FrosterBlockEntity::new, ModBlocks.FROSTER).build());

    public static final BlockEntityType<SlagFurnaceBlockEntity> SLAG_FURNACE_BE =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "slag_furnace_be"),
                    FabricBlockEntityTypeBuilder.create(SlagFurnaceBlockEntity::new, ModBlocks.SLAG_FURNACE).build());

    public static void registerBlockEntities() {
        GrimeAndGold.LOGGER.info("Registering ModBlockEntities for " + GrimeAndGold.MOD_ID);
    }
}
