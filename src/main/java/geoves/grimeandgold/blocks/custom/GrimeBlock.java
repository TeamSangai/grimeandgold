package geoves.grimeandgold.blocks.custom;

import com.mojang.serialization.MapCodec;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class GrimeBlock extends Block implements SiftPickup {
    public static final MapCodec<MudBlock> CODEC = simpleCodec(MudBlock::new);
    private static final VoxelShape SHAPE = Block.column((double)16.0F, (double)0.0F, (double)14.0F);

    public MapCodec<MudBlock> codec() {
        return CODEC;
    }

    public GrimeBlock(final BlockBehaviour.Properties properties) {
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

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        if (!level.isClientSide()) {
            level.levelEvent(2001, pos, Block.getId(state));
        }
        return new ItemStack(ModItems.COPPER_SIFT_FULL_GRIME.asItem());
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
