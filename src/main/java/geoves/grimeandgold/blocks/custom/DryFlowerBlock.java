package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DryVegetationBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DryFlowerBlock extends DryVegetationBlock implements SuspiciousEffectHolder {
    private final SuspiciousStewEffects suspiciousStewEffects;
    private static final VoxelShape SHAPE = Block.column(6.0F, 0.0F, 10.0F);

    public DryFlowerBlock(final Holder<MobEffect> suspiciousStewEffect, final float effectSeconds, final BlockBehaviour.Properties properties) {
        this(properties, makeEffectList(suspiciousStewEffect, effectSeconds));
    }

    public DryFlowerBlock(Properties properties, SuspiciousStewEffects suspiciousStewEffects) {
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
    public SuspiciousStewEffects getSuspiciousEffects() {
        return this.suspiciousStewEffects;
    }
}
