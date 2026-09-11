package geoves.grimeandgold.entities;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeIds {
    public static final ResourceKey<EntityType<?>> SIFT_FLY = create("sift_fly");

    private static ResourceKey<EntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name));
    }
}
