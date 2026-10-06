package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
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

public class DrySproutCropBlock extends CropBlock {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape[] SHAPES = Block.boxes(7, age -> Block.column(4.0, 0.0, 2 + age * 2));


    public DrySproutCropBlock(Properties properties) {
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
            int seed = random.nextInt(5);
            GrimeAndGold.LOGGER.info(String.valueOf(seed));
            switch (seed){
                case 0 -> level.setBlock(pos, ModBlocks.DESERT_LAVENDER.defaultBlockState(), 11);
                case 1 -> level.setBlock(pos, ModBlocks.DESERT_PRIMROSE.defaultBlockState(), 11);
                case 2 -> level.setBlock(pos, ModBlocks.DESERT_POPPY.defaultBlockState().setValue(FlowerBedBlock.AMOUNT, 4), 11);
                case 3 -> level.setBlock(pos, ModBlocks.GLOBE_THISTLE.defaultBlockState(), 11);
                case 4 -> {
                    if (level.getBlockState(pos.north()).is(BlockTags.AIR) && level.getBlockState(pos.south()).is(BlockTags.AIR)
                            && level.getBlockState(pos.west()).is(BlockTags.AIR) && level.getBlockState(pos.east()).is(BlockTags.AIR) && level.getBlockState(pos.below()).is(BlockTags.SAND)) {
                        level.setBlock(pos, Blocks.CACTUS.defaultBlockState(), 11);
                    } else {level.setBlock(pos, ModBlocks.GLOBE_THISTLE.defaultBlockState(), 11);}
                }
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getMaxAge()==this.getAge(state)){
            int seed = random.nextInt(5);
            GrimeAndGold.LOGGER.info(String.valueOf(seed));
            switch (seed){
                case 0 -> level.setBlock(pos, ModBlocks.DESERT_LAVENDER.defaultBlockState(), 11);
                case 1 -> level.setBlock(pos, ModBlocks.DESERT_PRIMROSE.defaultBlockState(), 11);
                case 2 -> level.setBlock(pos, ModBlocks.DESERT_POPPY.defaultBlockState().setValue(FlowerBedBlock.AMOUNT, 4), 11);
                case 3 -> level.setBlock(pos, ModBlocks.GLOBE_THISTLE.defaultBlockState(), 11);
                case 4 -> {
                    if (level.getBlockState(pos.north()).is(BlockTags.AIR) && level.getBlockState(pos.south()).is(BlockTags.AIR)
                            && level.getBlockState(pos.west()).is(BlockTags.AIR) && level.getBlockState(pos.east()).is(BlockTags.AIR)) {
                        level.setBlock(pos, Blocks.CACTUS.defaultBlockState(), 11);
                    }
                    else {level.setBlock(pos, ModBlocks.GLOBE_THISTLE.defaultBlockState(), 11);}
                }
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

    private static boolean mayPlaceOn(final BlockGetter level, final BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.SUPPORTS_DRY_VEGETATION);
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        BlockPos below = pos.below();
        return mayPlaceOn(level, below);
    }
}
