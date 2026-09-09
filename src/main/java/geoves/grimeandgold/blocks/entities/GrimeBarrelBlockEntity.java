package geoves.grimeandgold.blocks.entities;

import geoves.grimeandgold.menu.GrimeBarrelMenu;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class GrimeBarrelBlockEntity extends RandomizableContainerBlockEntity implements ExtendedMenuProvider<BlockPos> {
    public static final int CONTAINER_SIZE = 9;
    private static final Component DEFAULT_NAME = Component.translatable("container.grimeandgold.grimebarrel");
    private NonNullList<ItemStack> items;


    public GrimeBarrelBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.GRIMEBARREL_BE, worldPosition, blockState);
        this.items = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, this.items);
        }

    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, this.items);
        }

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(final NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(final int containerId, final Inventory inventory) {
        return new GrimeBarrelMenu(containerId, inventory, this);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }
}
