package geoves.grimeandgold.recipe;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.recipe.custom.SlagSmelting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RecipeSerializer<SlagSmelting> SLAG_SMELTING_RECIPE_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "slag_smelting"),
            new RecipeSerializer<>(SlagSmelting.CODEC, SlagSmelting.STREAM_CODEC));
    public static final RecipeType<SlagSmelting> SLAG_SMELTING_RECIPE_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "slag_smelting"),
            new RecipeType<SlagSmelting>() {
                @Override
                public String toString() {
                    return "slag_smelting";
                }
            });

    public static void registerModRecipes() {
        GrimeAndGold.LOGGER.info("Registering ModRecipes for " + GrimeAndGold.MOD_ID);
    }
}
