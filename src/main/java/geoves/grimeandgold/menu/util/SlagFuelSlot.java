package geoves.grimeandgold.menu.util;

import geoves.grimeandgold.menu.SlagFurnaceMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SlagFuelSlot extends Slot {
    private final SlagFurnaceMenu menu;

    public SlagFuelSlot(final SlagFurnaceMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return this.menu.isFuel(itemStack) || isBucket(itemStack);
    }

    @Override
    public int getMaxStackSize(final ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isBucket(final ItemStack itemStack) {
        return itemStack.is(Items.BUCKET);
    }
}
