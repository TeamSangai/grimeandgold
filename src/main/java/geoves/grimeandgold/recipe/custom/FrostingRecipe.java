package geoves.grimeandgold.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import geoves.grimeandgold.recipe.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record FrostingRecipe(Ingredient inputItem, ItemStackTemplate output) implements Recipe<FrostingRecipeInput> {
    public static final MapCodec<FrostingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(FrostingRecipe::inputItem),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(FrostingRecipe::output)
            ).apply(instance, FrostingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FrostingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    FrostingRecipe::inputItem,

                    ItemStackTemplate.STREAM_CODEC,
                    FrostingRecipe::output,

                    FrostingRecipe::new);



    @Override
    public boolean matches(FrostingRecipeInput input, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(FrostingRecipeInput input) {
        return output.create().copy();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "Crystallizing";
    }

    @Override
    public RecipeSerializer<? extends Recipe<FrostingRecipeInput>> getSerializer() {
        return ModRecipes.FROSTING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<FrostingRecipeInput>> getType() {
        return ModRecipes.FROSTING_RECIPE_TYPE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}