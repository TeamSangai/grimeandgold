package geoves.grimeandgold.loot_table;


import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {
    public static final ResourceKey<LootTable> SIFTING_GRIME = register(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("sifting/grime_sift")));
    private static ResourceKey<LootTable> register(final ResourceKey<LootTable> location) {
            return location;
    }

    public static void registerModRecipes() {
        GrimeAndGold.LOGGER.info("Registering ModLootTables for " + GrimeAndGold.MOD_ID);
    }
}
