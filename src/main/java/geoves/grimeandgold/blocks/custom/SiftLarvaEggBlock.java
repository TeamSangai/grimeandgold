package geoves.grimeandgold.blocks.custom;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.MapCodec;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class SiftLarvaEggBlock extends Block implements LiquidBlockContainer {
    private static final int MIN_TADPOLES_SPAWN = 1;
    private static final int MAX_TADPOLES_SPAWN = 2;
    private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;
    private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;
    private static final VoxelShape SHAPE = Block.column(4.0F, 0.0F, 4.0F);
    private static int minHatchTickDelay = 3600;
    private static int maxHatchTickDelay = 12000;


    public SiftLarvaEggBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        return mayPlaceOn(level, pos.below());
    }

    @Override
    protected void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
        level.scheduleTick(pos, this, getSiftLarvaEggHatchDelay(level.getRandom()));
    }


    private static int getSiftLarvaEggHatchDelay(final RandomSource random) {
        return random.nextInt(minHatchTickDelay, maxHatchTickDelay);
    }
    @Override
    protected BlockState updateShape(final BlockState state, final LevelReader level, final ScheduledTickAccess ticks, final BlockPos pos, final Direction directionToNeighbour, final BlockPos neighbourPos, final BlockState neighbourState, final RandomSource random) {
        return !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        if (!this.canSurvive(state, level, pos)) {
            this.destroyBlock(level, pos);
        } else {
            this.hatchSiftLarvaEgg(level, pos, random);
        }
    }

    @Override
    protected void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
        if (entity.is(EntityTypes.FALLING_BLOCK)) {
            this.destroyBlock(level, pos);
        }

    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return fluidState.is(FluidTags.WATER) && fluidState.isFull() ? super.getStateForPlacement(context) : null;
    }


    private static boolean mayPlaceOn(final BlockGetter level, final BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        return (fluidState.is(FluidTags.SUPPORTS_FROGSPAWN) || level.getBlockState(pos).is(ModTags.Blocks.SUPPORTS_SIFT_LARVA_EGGS));
    }

    private void hatchSiftLarvaEgg(final ServerLevel level, final BlockPos pos, final RandomSource random) {
        this.destroyBlock(level, pos);
        level.playSound(null, pos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.spawnTadpoles(level, pos, random);
    }

    private void destroyBlock(final Level level, final BlockPos pos) {
        level.destroyBlock(pos, false);
    }

    private void spawnTadpoles(final ServerLevel level, final BlockPos pos, final RandomSource random) {
        int tadpoleAmount = random.nextInt(1, 2);

        for(int i = 1; i <= tadpoleAmount; ++i) {
            Tadpole tadpole = EntityTypes.TADPOLE.create(level, EntitySpawnReason.BREEDING);
            if (tadpole != null) {
                double xPos = (double)pos.getX() + this.getRandomTadpolePositionOffset(random);
                double zPos = (double)pos.getZ() + this.getRandomTadpolePositionOffset(random);
                int yRot = random.nextInt(1, 361);
                tadpole.snapTo(xPos, (double)pos.getY() - (double)0.5F, zPos, (float)yRot, 0.0F);
                tadpole.setPersistenceRequired();
                level.addFreshEntity(tadpole);
            }
        }

    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        return Fluids.WATER.getSource(false);
    }


    private double getRandomTadpolePositionOffset(final RandomSource random) {
        double tadpoleHitboxCenter = 0.2F;
        return Mth.clamp(random.nextDouble(), 0.2F, 0.7999999970197678);
    }

    @VisibleForTesting
    public static void setHatchDelay(final int minDelay, final int maxDelay) {
        minHatchTickDelay = minDelay;
        maxHatchTickDelay = maxDelay;
    }

    @VisibleForTesting
    public static void setDefaultHatchDelay() {
        minHatchTickDelay = 3600;
        maxHatchTickDelay = 12000;
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
