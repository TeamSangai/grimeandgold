package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.datagen.recipe.SlagSmeltingRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                SlagSmeltingRecipeBuilder.slagSmeltingRecipe(RecipeCategory.MISC, Ingredient.of(Items.IRON_AXE, Items.IRON_BOOTS, Items.IRON_CHESTPLATE
                                , Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_NAUTILUS_ARMOR, Items.IRON_HELMET, Items.CHAINMAIL_CHESTPLATE
                                , Items.IRON_LEGGINGS, Items.IRON_SPEAR, Items.IRON_SWORD, Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_HELMET
                                , Items.CHAINMAIL_LEGGINGS, Items.IRON_SHOVEL), ModBlocks.IRON_SLAG.asItem(), ModBlocks.IRON_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.IRON_SLAG), has(ModBlocks.IRON_SLAG.asItem())).save(output, "grimeandgold:slag_from_iron_equipment");
            }
        };
    }

    @Override
    public String getName() {
        return "Grime&Gold Recipes";
    }
}
