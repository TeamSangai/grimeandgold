package geoves.grimeandgold.datagen.recipe;

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

public class SlagSmeltingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStackTemplate result;
    private final ItemStackTemplate byproduct;
    private final Ingredient ingredient;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private @Nullable String group;

    private SlagSmeltingRecipeBuilder(RecipeCategory category, Ingredient ingredient, ItemStackTemplate result, ItemStackTemplate byproduct) {
        this.category = category;
        this.result = result;
        this.ingredient = ingredient;
        this.byproduct = byproduct;
    }

    public static SlagSmeltingRecipeBuilder slagSmeltingRecipe(RecipeCategory category, Ingredient ingredient, ItemLike outputitem, ItemLike byproductitem, int resultcount,int byproductcount) {
        return new SlagSmeltingRecipeBuilder(category, ingredient, new ItemStackTemplate(outputitem.asItem(), resultcount), new ItemStackTemplate(byproductitem.asItem(), byproductcount));
    }

    public static SlagSmeltingRecipeBuilder slagSmeltingRecipe(RecipeCategory category, Ingredient ingredient, ItemLike outputitem, ItemLike byproductitem) {
        return new SlagSmeltingRecipeBuilder(category, ingredient, new ItemStackTemplate(outputitem.asItem()), new ItemStackTemplate(byproductitem.asItem()));
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
        SlagSmelting recipe = new SlagSmelting(this.ingredient, this.result, this.byproduct);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }
}
