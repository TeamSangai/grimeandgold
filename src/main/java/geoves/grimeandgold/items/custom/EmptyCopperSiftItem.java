package geoves.grimeandgold.items.custom;


import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EmptyCopperSiftItem extends Item {

    public EmptyCopperSiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        if(clickedBlock.defaultBlockState().is(ModTags.Blocks.SIFTABLE_GRIME) && !level.isClientSide()) {
            // We are on the Server!

            level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
            level.playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.MUD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            assert context.getPlayer() != null;
            context.getItemInHand().consume(1, context.getPlayer());
            context.getPlayer().addItem(ModItems.COPPER_SIFT_FULL_GRIME.getDefaultInstance());
            return InteractionResult.SUCCESS;
        }
        else {
            return InteractionResult.FAIL;
        }


    }


}
