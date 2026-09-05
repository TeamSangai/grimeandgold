package geoves.grimeandgold.items.custom;


import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.items.ModItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class EmptyCopperSiftItem extends Item {

    public EmptyCopperSiftItem(Properties properties) {
        super(properties);
    }


    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide()) {
        BlockState blockState = level.getBlockState(context.getClickedPos());
        Block var13 = blockState.getBlock();
        if (var13 instanceof SiftPickup siftPickupBlock) {
            ItemStack taken = siftPickupBlock.pickupBlock(context.getPlayer(), level, context.getClickedPos(), blockState);
            if (!taken.isEmpty()) {
                assert context.getPlayer() != null;
                ItemStack product = ItemUtils.createFilledResult(context.getItemInHand(), context.getPlayer(), taken);
                GrimeAndGold.LOGGER.info(product.toString());
                return InteractionResult.SUCCESS.heldItemTransformedTo(product);
            }
            else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;}
        return InteractionResult.PASS;
    }
}
