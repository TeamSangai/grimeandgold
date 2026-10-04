package geoves.grimeandgold.datagen.recipe;

import geoves.grimeandgold.recipe.custom.FrostingRecipe;
import geoves.grimeandgold.recipe.custom.SlagSmelting;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class FrostingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final Ingredient ingredient;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;

    private FrostingRecipeBuilder(RecipeCategory category, Ingredient ingredient, ItemStackTemplate result) {
        this.category = category;
        this.result = result;
        this.ingredient = ingredient;
    }

    public static FrostingRecipeBuilder frostingRecipe(RecipeCategory category, Ingredient ingredient, ItemLike outputitem, int resultcount) {
        return new FrostingRecipeBuilder(category, ingredient, new ItemStackTemplate(outputitem.asItem(), resultcount));
    }

    public static FrostingRecipeBuilder frostingRecipe(RecipeCategory category, Ingredient ingredient, ItemLike outputitem) {
        return new FrostingRecipeBuilder(category, ingredient, new ItemStackTemplate(outputitem.asItem()));
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        FrostingRecipe recipe = new FrostingRecipe(this.ingredient, this.result);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
