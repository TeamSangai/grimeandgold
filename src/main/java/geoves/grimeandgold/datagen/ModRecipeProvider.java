package geoves.grimeandgold.datagen;

import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.datagen.recipe.FrostingRecipeBuilder;
import geoves.grimeandgold.datagen.recipe.SlagSmeltingRecipeBuilder;
import geoves.grimeandgold.tags.ModTags;
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
                        .unlockedBy(getHasName(ModBlocks.IRON_SLAG.asItem()), has(ModBlocks.IRON_SLAG.asItem())).save(output, "grimeandgold:slag_from_iron_equipment");
                SlagSmeltingRecipeBuilder.slagSmeltingRecipe(RecipeCategory.MISC, Ingredient.of(Items.COPPER_AXE, Items.COPPER_BOOTS, Items.COPPER_CHESTPLATE
                                , Items.COPPER_HOE, Items.COPPER_PICKAXE, Items.COPPER_NAUTILUS_ARMOR, Items.COPPER_HELMET, Items.COPPER_LEGGINGS,
                                Items.COPPER_SPEAR, Items.COPPER_SWORD, Items.COPPER_SHOVEL), ModBlocks.COPPER_SLAG.asItem(), ModBlocks.COPPER_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.COPPER_SLAG.asItem()), has(ModBlocks.COPPER_SLAG.asItem())).save(output, "grimeandgold:slag_from_copper_equipment");
                SlagSmeltingRecipeBuilder.slagSmeltingRecipe(RecipeCategory.MISC, Ingredient.of(Items.GOLDEN_AXE, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE
                                , Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_NAUTILUS_ARMOR, Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS,
                                Items.GOLDEN_SPEAR, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL), ModBlocks.GOLD_SLAG.asItem(), ModBlocks.GOLD_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.GOLD_SLAG.asItem()), has(ModBlocks.GOLD_SLAG.asItem())).save(output, "grimeandgold:slag_from_golden_equipment");
                FrostingRecipeBuilder.frostingRecipe(RecipeCategory.MISC, Ingredient.of(Items.WATER_BUCKET), Items.POWDER_SNOW_BUCKET)
                        .unlockedBy(getHasName(Items.WATER_BUCKET), has(Items.WATER_BUCKET)).save(output, "grimeandgold:frosting_water_into_powder_snow");
                FrostingRecipeBuilder.frostingRecipe(RecipeCategory.MISC, Ingredient.of(ModBlocks.IRON_SLAG.asItem()), ModBlocks.COOLED_IRON_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.IRON_SLAG.asItem()), has(ModBlocks.IRON_SLAG.asItem())).save(output, "grimeandgold:frosting_iron_slag");
                FrostingRecipeBuilder.frostingRecipe(RecipeCategory.MISC, Ingredient.of(ModBlocks.COPPER_SLAG.asItem()), ModBlocks.COOLED_COPPER_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.COPPER_SLAG.asItem()), has(ModBlocks.COPPER_SLAG.asItem())).save(output, "grimeandgold:frosting_copper_slag");
                FrostingRecipeBuilder.frostingRecipe(RecipeCategory.MISC, Ingredient.of(ModBlocks.GOLD_SLAG.asItem()), ModBlocks.COOLED_GOLD_SLAG.asItem())
                        .unlockedBy(getHasName(ModBlocks.GOLD_SLAG.asItem()), has(ModBlocks.GOLD_SLAG.asItem())).save(output, "grimeandgold:frosting_gold_slag");

                shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BENTHIC_PLANKS.asItem(), 4).requires(tag(ModTags.Items.BENTHIC_LOGS))
                        .unlockedBy(getHasName(ModBlocks.BENTHIC_LOG.asItem()), has(ModBlocks.BENTHIC_LOG.asItem()))
                        .unlockedBy(getHasName(ModBlocks.BENTHIC_WOOD.asItem()), has(ModBlocks.BENTHIC_WOOD.asItem()))
                        .group("planks").save(output, "benthic_planks");

                shapeless(RecipeCategory.MISC, Items.DYE.pink()).requires(ModBlocks.AQUATIC_SPIN_ROSE.asItem())
                        .unlockedBy(getHasName(ModBlocks.AQUATIC_SPIN_ROSE.asItem()), has(ModBlocks.AQUATIC_SPIN_ROSE.asItem()))
                        .group("pink_dye").save(output, "pink_dye_from_spin_rose");
                shapeless(RecipeCategory.MISC, Items.DYE.lightBlue()).requires(ModBlocks.GHOST_OF_THE_SEA.asItem())
                        .unlockedBy(getHasName(ModBlocks.GHOST_OF_THE_SEA.asItem()), has(ModBlocks.GHOST_OF_THE_SEA.asItem()))
                        .group("light_blue_dye").save(output, "light_blue_dye_from_ghost_of_the_sea");
                shapeless(RecipeCategory.MISC, Items.DYE.purple()).requires(ModBlocks.DEEP_SEA_ROCKET.asItem())
                        .unlockedBy(getHasName(ModBlocks.DEEP_SEA_ROCKET.asItem()), has(ModBlocks.DEEP_SEA_ROCKET.asItem()))
                        .group("purple_dye").save(output, "purple_dye_from_deep_sea_rocket");
                shapeless(RecipeCategory.MISC, Items.DYE.purple()).requires(ModBlocks.GLOBE_THISTLE.asItem())
                        .unlockedBy(getHasName(ModBlocks.GLOBE_THISTLE.asItem()), has(ModBlocks.GLOBE_THISTLE.asItem()))
                        .group("purple_dye").save(output, "purple_dye_from_globe_thistle");
                shapeless(RecipeCategory.MISC, Items.DYE.magenta()).requires(ModBlocks.DESERT_LAVENDER.asItem())
                        .unlockedBy(getHasName(ModBlocks.DESERT_LAVENDER.asItem()), has(ModBlocks.DESERT_LAVENDER.asItem()))
                        .group("magenta_dye").save(output, "magenta_dye_from_desert_lavender");
                shapeless(RecipeCategory.MISC, Items.DYE.red()).requires(ModBlocks.GLACIER_HOLLY.asItem())
                        .unlockedBy(getHasName(ModBlocks.GLACIER_HOLLY.asItem()), has(ModBlocks.GLACIER_HOLLY.asItem()))
                        .group("red_dye").save(output, "red_dye_from_glacier_holly");
                shapeless(RecipeCategory.MISC, Items.DYE.orange()).requires(ModBlocks.DESERT_POPPY.asItem())
                        .unlockedBy(getHasName(ModBlocks.DESERT_POPPY.asItem()), has(ModBlocks.DESERT_POPPY.asItem()))
                        .group("orange_dye").save(output, "orange_dye_from_desert_poppy");
                shapeless(RecipeCategory.MISC, Items.DYE.yellow()).requires(ModBlocks.DESERT_PRIMROSE.asItem())
                        .unlockedBy(getHasName(ModBlocks.DESERT_PRIMROSE.asItem()), has(ModBlocks.DESERT_PRIMROSE.asItem()))
                        .group("yellow_dye").save(output, "yellow_dye_from_desert_primrose");

                shapeless(RecipeCategory.MISC, Items.DYE.purple(), 2).requires(ModBlocks.ANCHOR_BLOSSOM.asItem())
                        .unlockedBy(getHasName(ModBlocks.ANCHOR_BLOSSOM.asItem()), has(ModBlocks.ANCHOR_BLOSSOM.asItem()))
                        .group("purple_dye").save(output, "purple_dye_from_anchor_blossom");
                shapeless(RecipeCategory.MISC, Items.DYE.white(), 2).requires(ModBlocks.SPIRAL_DAFFODIL.asItem())
                        .unlockedBy(getHasName(ModBlocks.SPIRAL_DAFFODIL.asItem()), has(ModBlocks.SPIRAL_DAFFODIL.asItem()))
                        .group("white_dye").save(output, "white_dye_from_spiral_daffodil");
                shapeless(RecipeCategory.MISC, Items.DYE.brown(), 2).requires(ModBlocks.COCOA_BLOOM.asItem())
                        .unlockedBy(getHasName(ModBlocks.COCOA_BLOOM.asItem()), has(ModBlocks.COCOA_BLOOM.asItem()))
                        .group("brown_dye").save(output, "brown_dye_from_cocoa_bloom");


                stairBuilder(ModBlocks.BENTHIC_PLANKS_STAIRS.asItem(), Ingredient.of(ModBlocks.BENTHIC_PLANKS.asItem()));
                slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BENTHIC_PLANKS_SLAB.asItem(), Ingredient.of(ModBlocks.BENTHIC_PLANKS.asItem()));
                doorBuilder(ModBlocks.BENTHIC_DOOR.asItem(), Ingredient.of(ModBlocks.BENTHIC_PLANKS.asItem()));
                trapdoorBuilder(ModBlocks.BENTHIC_TRAPDOOR.asItem(), Ingredient.of(ModBlocks.BENTHIC_PLANKS.asItem()));

            }
        };
    }

    @Override
    public String getName() {
        return "Grime&Gold Recipes";
    }
}
