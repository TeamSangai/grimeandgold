package geoves.grimeandgold.blocks.entities;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.custom.SlagFurnaceBlock;
import geoves.grimeandgold.menu.SlagFurnaceMenu;
import geoves.grimeandgold.recipe.ModRecipes;
import geoves.grimeandgold.recipe.custom.SlagSmelting;
import geoves.grimeandgold.recipe.custom.SlagSmeltingInput;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SlagFurnaceBlockEntity extends BaseContainerBlockEntity implements ExtendedMenuProvider<BlockPos> {
    private static final Component DEFAULT_NAME = Component.translatable("container.slag_furnace");
    private final short DEFAULT_LIT_TIME_REMAINING = 0;
    private final short DEFAULT_LIT_TOTAL_TIME = 0;
    private NonNullList<ItemStack> items;
    private final ContainerData data;
    private int litTimeRemaining;
    private  int litTotalTime;
    private int cookingTimer;
    private int cookingTotalTime;
    private float speedMultiplier;
    private final int INPUT_SLOT = 0;
    private final int FUEL_SLOT = 1;
    private final int OUTPUT_SLOT = 2;
    private final int BYPRODUCT_SLOT = 3;
    public final int CONTAINER_SIZE = 4;
    private final RecipeManager.CachedCheck<SlagSmeltingInput, SlagSmelting> quickCheck = RecipeManager.createCheck(ModRecipes.SLAG_SMELTING_RECIPE_TYPE);;
    private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed;


    public SlagFurnaceBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(ModBlockEntities.SLAG_FURNACE_BE, worldPosition, blockState);
        this.recipesUsed = new Reference2IntOpenHashMap<>();
        this.data = new ContainerData() {

            public int get(final int dataId) {
                switch (dataId) {
                    case 0 -> {
                        return litTimeRemaining;
                    }
                    case 1 -> {
                        return litTotalTime;
                    }
                    case 2 -> {
                        return cookingTimer;
                    }
                    case 3 -> {
                        return cookingTotalTime;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(final int dataId, final int value) {
                switch (dataId) {
                    case 0 -> litTimeRemaining = value;
                    case 1 -> litTotalTime = value;
                    case 2 -> cookingTimer = value;
                    case 3 -> cookingTotalTime = value;
                }

            }
            public int getCount() {
                return 4;
            }
        };
        items = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.grimeandgold.slag_furnace");
    }



    @Override
    protected @NonNull Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items = items;
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("slag_furnace.progress", cookingTimer);
        output.putInt("slag_furnace.max_progress", cookingTotalTime);

        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        cookingTimer = input.getIntOr("slag_furnace.progress", 0);
        cookingTotalTime = input.getIntOr("slag_furnace.max_progress", 124);

        ContainerHelper.loadAllItems(input, this.items);
    }


    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SlagFurnaceMenu(containerId, inventory, this, this.data);
    }


    private Optional<RecipeHolder<SlagSmelting>> getCurrentRecipe(){
        assert level != null;
        return ((ServerLevel) level).recipeAccess()
                .getRecipeFor(ModRecipes.SLAG_SMELTING_RECIPE_TYPE, new SlagSmeltingInput(this.items.get(INPUT_SLOT)), level);
    }

    public void ServerTick(ServerLevel level, BlockPos pos, BlockState state){
        boolean hasFuel;
        boolean isLit;
        boolean wasLit;
        boolean changed = false;
        if (litTimeRemaining > 0) {
            wasLit = true;
            --litTimeRemaining;
            isLit = litTimeRemaining > 0;
        } else {
            wasLit = false;
            isLit = false;
        }
        ItemStack fuel = items.get(1);
        ItemStack ingredient = items.get(0);
        boolean hasIngredient = !ingredient.isEmpty();
        boolean bl = hasFuel = !fuel.isEmpty();
        if (isLit || hasFuel && hasIngredient) {
            if (hasIngredient) {
                SlagSmeltingInput SlagSmeltingInput = new SlagSmeltingInput(ingredient);
                RecipeHolder<SlagSmelting> recipe = quickCheck.getRecipeFor(SlagSmeltingInput, level).orElse(null);
                if (recipe != null) {
                    int maxStackSize = this.getMaxStackSize();
                    ItemStack burnResult = (recipe.value()).assemble(SlagSmeltingInput);
                    if (!burnResult.isEmpty() && canBurn(items, maxStackSize, burnResult)) {
                        if (!isLit) {
                            int newLitTime = this.getBurnDuration(level, fuel);
                            float newSpeedMultiplier = this.getSpeedMultiplier(level, fuel);
                            litTimeRemaining = newLitTime;
                            litTotalTime = newLitTime;
                            speedMultiplier = newSpeedMultiplier;
                            if (cookingTotalTime > 0 && cookingTimer < cookingTotalTime) {
                                float completionRatio = (float)cookingTimer / (float)cookingTotalTime;
                                cookingTotalTime = getTotalCookTime(recipe, this);
                                cookingTimer = (int)Math.ceil(completionRatio * (float) cookingTotalTime);
                            }
                            if (newLitTime > 0) {
                                consumeFuel(level, pos, items, fuel);
                                isLit = true;
                                changed = true;
                            }
                        }
                        if (isLit) {
                            ++cookingTimer;
                            if (cookingTimer >= cookingTotalTime) {
                                GrimeAndGold.LOGGER.info(String.valueOf(cookingTimer));
                                cookingTimer = 0;
                                cookingTotalTime = this.getTotalCookTime(recipe, this);
                                craftItem();
                                setRecipeUsed(recipe);
                                changed = true;
                            }
                        } else {
                            cookingTimer = 0;
                        }
                    } else {
                        cookingTimer = 0;
                    }
                }
            } else {
                cookingTimer = 0;
            }
        } else if (cookingTimer > 0) {
            cookingTimer = Mth.clamp(cookingTimer - 2, 0, cookingTotalTime);
        }
        if (wasLit != isLit) {
            changed = true;
            state = state.setValue(SlagFurnaceBlock.LIT, isLit);
            level.setBlockAndUpdate(pos, state);
        }
        if (changed) {
            SlagFurnaceBlockEntity.setChanged(level, pos, state);
        }
    }


    private boolean canBurn(NonNullList<ItemStack> items, int maxStackSize, ItemStack burnResult) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult)) {
            return false;
        }
        int resultCount = resultItemStack.getCount() + burnResult.count();
        return resultCount <= Math.min(maxStackSize, burnResult.getMaxStackSize());
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeUsed) {
        if (recipeUsed != null) {
            ResourceKey<Recipe<?>> id = recipeUsed.id();
            this.recipesUsed.addTo(id, 1);
        }
    }

    protected int getBurnDuration(ServerLevel level, ItemStack fuelItem) {
        return ResolvableInt.getFromItem(fuelItem, DataComponents.COOKING_FUEL, CookingFuel::burnTime, this.getLootContext(level), 0);
    }

    protected float getSpeedMultiplier(ServerLevel level, ItemStack fuelItem) {
        return ResolvableFloat.getFromItem(fuelItem, DataComponents.COOKING_FUEL, CookingFuel::speedMultiplier, this.getLootContext(level), 1.0f);
    }

    private void consumeFuel(ServerLevel level, BlockPos pos, NonNullList<ItemStack> items, ItemStack fuel) {
        Item fuelItem = fuel.getItem();
        ItemStackTemplate remainder = fuelItem.getCraftingRemainder();
        ItemStack newFuel = fuel;
        fuel.shrink(1);
        if (remainder != null) {
            if (fuel.isEmpty()) {
                newFuel = remainder.create();
            } else {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder.create());
            }
        }
        items.set(1, newFuel);
    }
    private int getTotalCookTime(RecipeHolder<? extends SlagSmelting> recipe, SlagFurnaceBlockEntity entity) {
        int cookingTotalTime = 200;
        return speedMultiplier > 0.0f ? (int)Math.ceil((float)cookingTotalTime / speedMultiplier) : cookingTotalTime;
    }



    private void craftItem() {
        Optional<RecipeHolder<SlagSmelting>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().assemble(new SlagSmeltingInput(this.items.get(INPUT_SLOT)));
        ItemStack byproduct = recipe.get().value().byproduct().apply(1, DataComponentPatch.EMPTY);

        GrimeAndGold.LOGGER.info(String.valueOf(byproduct));
        this.items.set(INPUT_SLOT, this.items.get(INPUT_SLOT).copyWithCount(this.items.get(INPUT_SLOT).getCount() - 1));
        this.items.set(OUTPUT_SLOT, output.copyWithCount(this.items.get(OUTPUT_SLOT).getCount() + output.getCount()));
        this.items.set(BYPRODUCT_SLOT, byproduct.copyWithCount(this.items.get(BYPRODUCT_SLOT).getCount() + byproduct.getCount()));

    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    /**
     * @return
     */
    @Override
    public int getContainerSize() {
        return 4;
    }
}
