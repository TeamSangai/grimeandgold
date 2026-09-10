package geoves.grimeandgold.entities;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypes {

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }
    public static void registerModEntityTypes() {
        GrimeAndGold.LOGGER.info("Registering EntityTypes for " + GrimeAndGold.MOD_ID);
    }

    public static void registerAttributes() {
    }
}
