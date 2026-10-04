package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.datagen.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class CopperNailsBlock extends Block {

    private static final VoxelShape SHAPE = Block.column(14.0F, 0.0F, 2.0F);


    public CopperNailsBlock ( Properties properties) {
        super(properties);
    }

    protected @NonNull VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof LivingEntity) {
                entity.hurtServer(serverLevel, ModDamageTypes.create(level, ModDamageTypes.SHARP_FLOOR), 1);
            }
        }
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }
}
