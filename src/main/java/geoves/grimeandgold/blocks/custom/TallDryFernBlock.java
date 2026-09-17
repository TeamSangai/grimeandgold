package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.TallDryGrassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TallDryFernBlock extends TallDryGrassBlock implements BonemealableBlock {
    public TallDryFernBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(final LevelReader level, final BlockPos pos, final BlockState state, final BonemealSource source) {
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, ModBlocks.DRY_FERN.defaultBlockState());
    }

    @Override
    public boolean isBonemealSuccess(final Level level, final RandomSource random, final BlockPos pos, final BlockState state, final BonemealSource source) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource random, final BlockPos pos, final BlockState state, final BonemealSource source) {
        BonemealableBlock.findSpreadableNeighbourPos(level, pos, ModBlocks.DRY_FERN.defaultBlockState()).ifPresent((blockPos) -> level.setBlockAndUpdate(blockPos, Blocks.SHORT_DRY_GRASS.defaultBlockState()));
    }
}
