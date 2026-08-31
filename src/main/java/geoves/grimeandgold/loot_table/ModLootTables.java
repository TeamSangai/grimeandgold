package geoves.grimeandgold.loot_table;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static class Sifting {
        public static final ResourceKey<LootTable> GRIME_SIFT = createTable("sift_grime");
    }
    private static ResourceKey<LootTable> createTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "sifting/" + name));
    }
}
