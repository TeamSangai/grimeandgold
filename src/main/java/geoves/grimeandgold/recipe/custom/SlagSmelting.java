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

public record SlagSmelting(Ingredient inputItem, ItemStackTemplate output, ItemStackTemplate byproduct) implements Recipe<SlagSmeltingInput> {
    public static final MapCodec<SlagSmelting> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(SlagSmelting::inputItem),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(SlagSmelting::output),
                    ItemStackTemplate.CODEC.fieldOf("byproduct").forGetter(SlagSmelting::output)
            ).apply(instance, SlagSmelting::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SlagSmelting> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    SlagSmelting::inputItem,

                    ItemStackTemplate.STREAM_CODEC,
                    SlagSmelting::output,
                    ItemStackTemplate.STREAM_CODEC,
                    SlagSmelting::byproduct,

                    SlagSmelting::new);


    @Override
    public boolean matches(SlagSmeltingInput input, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(SlagSmeltingInput input) {
        return output.create().copy();
    }


    /**
     * @return
     */
    @Override
    public boolean showNotification() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public String group() {
        return "Slag Smelting";
    }

    /**
     * @return
     */
    @Override
    public RecipeSerializer<? extends Recipe<SlagSmeltingInput>> getSerializer() {
        return ModRecipes.SLAG_SMELTING_RECIPE_SERIALIZER;
    }

    /**
     * @return
     */
    @Override
    public RecipeType<? extends Recipe<SlagSmeltingInput>> getType() {
        return ModRecipes.SLAG_SMELTING_RECIPE_TYPE;
    }

    /**
     * @return
     */
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    /**
     * @return
     */
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
