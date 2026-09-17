package geoves.grimeandgold.blocks.custom;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class AquaticFlowerBlock extends VegetationBlock implements LiquidBlockContainer, SuspiciousEffectHolder {

    private static final VoxelShape SHAPE = Block.column(6.0F, 0.0F, 10.0F);
    private final SuspiciousStewEffects suspiciousStewEffects;

    public AquaticFlowerBlock(final Holder<MobEffect> suspiciousStewEffect, final float effectSeconds, final BlockBehaviour.Properties properties) {
        this(makeEffectList(suspiciousStewEffect, effectSeconds), properties);
    }

    public AquaticFlowerBlock(final SuspiciousStewEffects suspiciousStewEffects, final BlockBehaviour.Properties properties) {
        super(properties);
        this.suspiciousStewEffects = suspiciousStewEffects;
    }

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE.move(state.getOffset(pos));
    }
    protected static SuspiciousStewEffects makeEffectList(final Holder<MobEffect> suspiciousStewEffect, final float effectSeconds) {
        return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(suspiciousStewEffect, Mth.floor(effectSeconds * 20.0F))));
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
        return false;
    }
    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return fluidState.is(FluidTags.WATER) && fluidState.isFull() ? super.getStateForPlacement(context) : null;
    }


    private static boolean mayPlaceOn(final BlockGetter level, final BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.SUPPORTS_AQUATIC_FLOWERS);
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

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return this.suspiciousStewEffects;
    }

}
