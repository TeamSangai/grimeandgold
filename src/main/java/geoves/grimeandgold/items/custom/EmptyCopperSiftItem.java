package geoves.grimeandgold.items.custom;


import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

public class EmptyCopperSiftItem extends Item {

    public EmptyCopperSiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        int durability = itemStack.getDamageValue();
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos pos = hitResult.getBlockPos();
            Direction direction = hitResult.getDirection();
            BlockPos directionOffsetPos = pos.relative(direction);
            if (level.mayInteract(player, pos) && player.mayUseItemAt(directionOffsetPos, direction, itemStack)) {
                if (!level.isClientSide()) {
                    BlockState blockState = level.getBlockState(pos);
                    Block var13 = blockState.getBlock();
                    if (var13 instanceof SiftPickup siftPickupBlock) {
                        ItemStack taken = siftPickupBlock.pickupBlock(player, level, pos, blockState);
                        if (!taken.isEmpty()) {
                            if (!player.isCreative()) {
                                player.setItemInHand(player.getUsedItemHand(), taken);
                                player.getItemInHand(player.getUsedItemHand()).setDamageValue(durability);
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.SUCCESS;
                        }
                        else {
                            return InteractionResult.FAIL;
                        }
                    }
                    return InteractionResult.PASS;}
            }
        }
        return InteractionResult.PASS;
    }
}
