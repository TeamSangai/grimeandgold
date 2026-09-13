package geoves.grimeandgold.items;

import geoves.grimeandgold.entities.ModEntityTypeIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

// Meniscus: I'm just copying how Minecraft did it for convenience
public class ModItemIds {
    public static final ResourceKey<Item> SIFT_FLY_SPAWN_EGG = createSpawnEgg(ModEntityTypeIds.SIFT_FLY);
    public static final ResourceKey<Item> SIFT_GRUB_SPAWN_EGG = createSpawnEgg(ModEntityTypeIds.SIFT_GRUB);

    private static ResourceKey<Item> createSpawnEgg(final ResourceKey<EntityType<?>> entity) {
        return entity.dependent(Registries.ITEM, "_spawn_egg");
    }
}
