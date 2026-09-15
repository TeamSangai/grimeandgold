package geoves.grimeandgold.blocks.custom;

import com.mojang.serialization.MapCodec;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DoubleAquaticFlowerBlock extends DoublePlantBlock implements LiquidBlockContainer {
    public static final MapCodec<DoubleAquaticFlowerBlock> CODEC = simpleCodec(DoubleAquaticFlowerBlock::new);
    public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
    private static final VoxelShape SHAPE = DoubleAquaticFlowerBlock.column(12.0, 0.0, 16.0);

    @Override
    public MapCodec<DoubleAquaticFlowerBlock> codec() {
        return CODEC;
    }

    public DoubleAquaticFlowerBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP) && state.is(ModTags.Blocks.SUPPORTS_AQUATIC_FLOWERS);
    }

    @Override
    protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(ModBlocks.ANCHOR_BLOSSOM);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos().above());
            if (fluidState.is(FluidTags.WATER) && fluidState.isFull()) {
                return state;
            }
        }

        return null;
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState belowState = level.getBlockState(pos.below());
            return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            FluidState fluidState = level.getFluidState(pos);
            return super.canSurvive(state, level, pos) && fluidState.is(FluidTags.WATER) && fluidState.isFull();
        }
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        return Fluids.WATER.getSource(false);
    }



    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
