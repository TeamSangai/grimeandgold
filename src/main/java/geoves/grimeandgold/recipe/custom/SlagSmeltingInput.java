package geoves.grimeandgold.recipe.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record SlagSmeltingInput(ItemStack input) implements RecipeInput {

    /**
     * @param index
     * @return
     */
    @Override
    public ItemStack getItem(int index) {
        return input;
    }

    /**
     * @return
     */
    @Override
    public int size() {
        return 1;
    }
}
