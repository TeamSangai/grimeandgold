package geoves.grimeandgold.items.custom;


import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class EmptyCopperSiftItem extends Item {

    public EmptyCopperSiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        if (clickedBlock.defaultBlockState().is(ModTags.Blocks.SIFTABLE_GRIME) && !level.isClientSide()) {
            BlockState blockState = level.getBlockState(context.getClickedPos());
            Block var13 = blockState.getBlock();
            if (var13 instanceof SiftPickup) {
                SiftPickup siftPickupBlock = (SiftPickup) var13;
                ItemStack taken = siftPickupBlock.pickupBlock(context.getPlayer(), level, context.getClickedPos(), blockState);
                if (!taken.isEmpty()) {
                    assert context.getPlayer() != null;
                    siftPickupBlock.getPickupSound().ifPresent((soundEvent) -> context.getPlayer().playSound(soundEvent, 1.0F, 1.0F));
                    level.gameEvent(context.getPlayer(), GameEvent.FLUID_PICKUP, context.getClickedPos());
                    ItemStack result = ItemUtils.createFilledResult(context.getItemInHand(), context.getPlayer(), taken);
                    return InteractionResult.SUCCESS.heldItemTransformedTo(result);
                }
            }
        }
        else {return InteractionResult.FAIL;};
        return InteractionResult.PASS;
    }
}
