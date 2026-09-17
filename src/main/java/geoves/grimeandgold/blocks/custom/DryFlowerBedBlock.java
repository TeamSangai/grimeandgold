package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DryFlowerBedBlock extends FlowerBedBlock implements BonemealableBlock, SegmentableBlock {
    public DryFlowerBedBlock(Properties properties, int shapeHeight) {
        super(properties, shapeHeight);

    }
    @Override
    protected boolean mayPlaceOn(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return state.is(BlockTags.SUPPORTS_DRY_VEGETATION);
    }
}
