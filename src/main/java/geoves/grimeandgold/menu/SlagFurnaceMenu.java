package geoves.grimeandgold.menu;

import geoves.grimeandgold.blocks.entities.SlagFurnaceBlockEntity;
import geoves.grimeandgold.menu.util.SlagFuelSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SlagFurnaceMenu extends AbstractContainerMenu {
    public static final int INGREDIENT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int BYPRODUCT_SLOT = 3;
    public static final int SLOT_COUNT = 4;
    public static final int DATA_COUNT = 5;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;
    public final SlagFurnaceBlockEntity blockEntity;
    private final Container inventory;
    private final ContainerData data;

    public SlagFurnaceMenu(int pContainerId, Inventory inv, BlockPos blockPos) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(blockPos), new SimpleContainerData(4));
    }

    public SlagFurnaceMenu(int pContainerID, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.SLAG_FURNACE_MENU, pContainerID);
        blockEntity = ((SlagFurnaceBlockEntity) entity);
       
        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.inventory = blockEntity;
        assert this.blockEntity.getLevel() != null;
        this.data = data;

        this.addSlot(new Slot(inventory,0, 56, 17));

        this.addSlot(new SlagFuelSlot(this, inventory,1, 56, 53));
        // Output
        this.addSlot(new Slot(inventory,2, 116, 20) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });
        // byproduct
        this.addSlot(new Slot(inventory,3, 116, 53) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });

        addDataSlots(data);
    }


    public void fillCraftSlotsStackedContents(final StackedItemContents stackedContents) {
        Container var3 = this.inventory;
        if (var3 instanceof StackedContentsCompatible stackedContentsCompatible) {
            stackedContentsCompatible.fillStackedContents(stackedContents);
        }

    }

    public Slot getResultSlot() {
        return this.slots.get(2);
    }
    public Slot getByproductSlot() {
        return this.slots.get(3);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }



    public boolean isFuel(final ItemStack itemStack) {
        return itemStack.has(DataComponents.COOKING_FUEL);
    }


    public float getBurnProgress() {
        int current = this.data.get(2);
        int total = this.data.get(3);
        return total != 0 && current != 0 ? Mth.clamp((float)current / (float)total, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int litDuration = this.data.get(1);
        if (litDuration == 0) {
            litDuration = 200;
        }

        return Mth.clamp((float)this.data.get(0) / (float)litDuration, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
