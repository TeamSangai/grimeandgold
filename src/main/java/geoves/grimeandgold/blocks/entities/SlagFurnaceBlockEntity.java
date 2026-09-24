package geoves.grimeandgold.blocks.entities;

import geoves.grimeandgold.blocks.custom.SlagFurnaceBlock;
import geoves.grimeandgold.menu.SlagFurnaceMenu;
import geoves.grimeandgold.recipe.ModRecipes;
import geoves.grimeandgold.recipe.custom.SlagSmelting;
import geoves.grimeandgold.recipe.custom.SlagSmeltingInput;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SlagFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements ExtendedMenuProvider<BlockPos> {
    private static final Component DEFAULT_NAME = Component.translatable("container.slag_furnace");
    private static final short DEFAULT_LIT_TIME_REMAINING = 0;
    private static final short DEFAULT_LIT_TOTAL_TIME = 0;
    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;
    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int BYPRODUCT_SLOT = 3;
    public static final int CONTAINER_SIZE = 4;


    public SlagFurnaceBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(ModBlockEntities.SLAG_FURNACE_BE, worldPosition, blockState, RecipeType.SMELTING);
        this.data = new ContainerData() {
            @Override
            public int get(int dataId) {
                return switch (dataId) {
                    case 0 -> SlagFurnaceBlockEntity.this.progress;
                    case 1 -> SlagFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int dataId, int value) {
                switch (dataId) {
                    case 0: SlagFurnaceBlockEntity.this.progress = value;
                    case 1: SlagFurnaceBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.grimeandgold.slag_furnace");
    }



    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("slag_furnace.progress", progress);
        output.putInt("slag_furnace.max_progress", maxProgress);

        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr("slag_furnace.progress", 0);
        maxProgress = input.getIntOr("slag_furnace.max_progress", 72);

        ContainerHelper.loadAllItems(input, this.items);
    }


    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SlagFurnaceMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            increaseCraftingProgress();
            level.setBlockAndUpdate(pos, state.setValue(SlagFurnaceBlock.LIT, true));
            setChanged(level, pos, state);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
            level.setBlockAndUpdate(pos, state.setValue(SlagFurnaceBlock.LIT, false));
        }
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<SlagSmelting>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().assemble(new SlagSmeltingInput(this.items.get(INPUT_SLOT)));
        boolean isItemOutputRight = canInsertItemIntoOutputSlot(output);
        boolean isAmountRight = canInsertAmountIntoOutputSlot(output.getCount());

        return isItemOutputRight && isAmountRight;
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.items.get(OUTPUT_SLOT).isEmpty() ? 64 : this.items.get(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = this.items.get(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.items.get(OUTPUT_SLOT).isEmpty() ||
                this.items.get(OUTPUT_SLOT).is(output.getItem());
    }


    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this.items);
    }

    private Optional<RecipeHolder<SlagSmelting>> getCurrentRecipe(){
        assert level != null;
        return ((ServerLevel) level).recipeAccess()
                .getRecipeFor(ModRecipes.SLAG_SMELTING_RECIPE_TYPE, new SlagSmeltingInput(this.items.get(INPUT_SLOT)), level);
    }

    private void craftItem() {
        Optional<RecipeHolder<SlagSmelting>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().assemble(new SlagSmeltingInput(this.items.get(INPUT_SLOT)));
        ItemStack byproduct = recipe.get().value().assemble(new SlagSmeltingInput(this.items.get(INPUT_SLOT)));

        this.items.set(INPUT_SLOT, this.items.get(INPUT_SLOT).copyWithCount(this.items.get(INPUT_SLOT).getCount() - 1));
        this.items.set(OUTPUT_SLOT, output.copyWithCount(this.items.get(OUTPUT_SLOT).getCount() + output.getCount()));
        this.items.set(BYPRODUCT_SLOT, output.copyWithCount(this.items.get(BYPRODUCT_SLOT).getCount() + byproduct.getCount()));

    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.items.get(OUTPUT_SLOT).isEmpty() ||
                this.items.get(OUTPUT_SLOT).getCount() < this.items.get(OUTPUT_SLOT).getMaxStackSize();
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }
}
