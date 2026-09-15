package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;

public class SpiralDafodilBlock extends DoubleAquaticFlowerBlock implements LiquidBlockContainer {
    public SpiralDafodilBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(ModBlocks.SPIRAL_DAFFODIL);
    }

}
