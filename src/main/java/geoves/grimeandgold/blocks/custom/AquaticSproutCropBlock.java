package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import static geoves.grimeandgold.blocks.custom.DoubleAquaticFlowerBlock.HALF;

public class AquaticSproutCropBlock extends CropBlock implements LiquidBlockContainer {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape[] SHAPES = Block.boxes(7, age -> Block.column(4.0, 0.0, 2 + age * 2));


    public AquaticSproutCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.AQUATIC_MIXED_SEEDS;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float growthSpeed;
        int age;
        if (level.getRawBrightness(pos, 0) >= 9 && (age = this.getAge(state)) < this.getMaxAge() && random.nextInt((int)(25.0f / (growthSpeed = CropBlock.getGrowthSpeed(this, level, pos))) + 1) == 0) {
            level.setBlock(pos, this.getStateForAge(age + 1), 2);
        }
        if (getMaxAge()==this.getAge(state)){
            level.scheduleTick(pos, this, 200);
        }
    }
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, BonemealSource source) {
        return true;
    }
    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
        this.growCrops(level, pos, state);
        if (getMaxAge()==this.getAge(state)){
            int seed = random.nextInt(7);
            GrimeAndGold.LOGGER.info(String.valueOf(seed));
            switch (seed) {
                case 0 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.ANCHOR_BLOSSOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.ANCHOR_BLOSSOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.AQUATIC_SPIN_ROSE.defaultBlockState(), 11);}
                }
                case 1 -> level.setBlock(pos, ModBlocks.AQUATIC_SPIN_ROSE.defaultBlockState(), 11);
                case 2 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.SPIRAL_DAFFODIL.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.SPIRAL_DAFFODIL.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.GHOST_OF_THE_SEA.defaultBlockState(), 11);}
                }
                case 3 -> level.setBlock(pos, ModBlocks.GHOST_OF_THE_SEA.defaultBlockState(), 11);
                case 4 -> level.setBlock(pos, ModBlocks.DEEP_SEA_ROCKET.defaultBlockState(), 11);
                case 5 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.COCOA_BLOOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.COCOA_BLOOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.GLACIER_HOLLY.defaultBlockState(), 11);}
                }
                case 6 -> level.setBlock(pos, ModBlocks.GLACIER_HOLLY.defaultBlockState(), 11);
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getMaxAge()==this.getAge(state)){
            int seed = random.nextInt(7);
            GrimeAndGold.LOGGER.info(String.valueOf(seed));
            switch (seed) {
                case 0 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.ANCHOR_BLOSSOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.ANCHOR_BLOSSOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.AQUATIC_SPIN_ROSE.defaultBlockState(), 11);}
                }
                case 1 -> level.setBlock(pos, ModBlocks.AQUATIC_SPIN_ROSE.defaultBlockState(), 11);
                case 2 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.SPIRAL_DAFFODIL.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.SPIRAL_DAFFODIL.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.GHOST_OF_THE_SEA.defaultBlockState(), 11);}
                }
                case 3 -> level.setBlock(pos, ModBlocks.GHOST_OF_THE_SEA.defaultBlockState(), 11);
                case 4 -> level.setBlock(pos, ModBlocks.DEEP_SEA_ROCKET.defaultBlockState(), 11);
                case 5 -> {
                    if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
                        level.setBlock(pos, ModBlocks.COCOA_BLOOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 11);
                        level.setBlock(pos.above(), ModBlocks.COCOA_BLOOM.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 11);
                    } else {level.setBlock(pos, ModBlocks.GLACIER_HOLLY.defaultBlockState(), 11);}
                }
                case 6 -> level.setBlock(pos, ModBlocks.GLACIER_HOLLY.defaultBlockState(), 11);
            }
        }
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAge(state)];
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        return false;
    }

    private static boolean mayPlaceOn(final BlockGetter level, final BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.SUPPORTS_AQUATIC_FLOWERS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return fluidState.is(FluidTags.WATER) && fluidState.isFull() ? super.getStateForPlacement(context) : null;
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        BlockPos below = pos.below();
        return mayPlaceOn(level, below);
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        return Fluids.WATER.getSource(false);
    }


    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
