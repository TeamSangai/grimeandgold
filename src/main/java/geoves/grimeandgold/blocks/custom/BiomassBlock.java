package geoves.grimeandgold.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MudBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BiomassBlock extends Block {
    public static final MapCodec<BiomassBlock> CODEC = simpleCodec(BiomassBlock::new);
    private static final VoxelShape SHAPE = Block.column(16.0F, 0.0F, 13.0F);

    public MapCodec<BiomassBlock> codec() {
        return CODEC;
    }


    public BiomassBlock(Properties properties) {
        super(properties);
    }

    protected VoxelShape getCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    protected VoxelShape getBlockSupportShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return Shapes.block();
    }

    protected VoxelShape getVisualShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return Shapes.block();
    }

    protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return false;
    }

    protected float getShadeBrightness(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return 0.2F;
    }

    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos relativePos = pos.relative(direction);
            BlockState blockState = level.getBlockState(relativePos);
            if (!state.canOcclude() || !blockState.isFaceSturdy(level, relativePos, direction.getOpposite())) {
                double xx = pos.getX();
                double yy = pos.getY();
                double zz = pos.getZ();
                if (direction == Direction.DOWN) {
                    yy -= 0.05;
                    xx += random.nextDouble();
                    zz += random.nextDouble();
                } else {
                    yy += random.nextDouble() * 0.8;
                    if (direction.getAxis() == Direction.Axis.X) {
                        zz += random.nextDouble();
                        if (direction == Direction.EAST) {
                            ++xx;
                        } else {
                            xx += 0.05;
                        }
                    } else {
                        xx += random.nextDouble();
                        if (direction == Direction.SOUTH) {
                            ++zz;
                        } else {
                            zz += 0.05;
                        }
                    }
                }

                level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, xx, yy, zz, 0.0F, 0.0F, 0.0F);
            }
        }
    }

}
