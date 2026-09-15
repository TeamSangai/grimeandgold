package geoves.grimeandgold.blocks.custom;

import com.mojang.serialization.MapCodec;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class GrimeBlock extends Block implements SiftPickup {
    public static final MapCodec<GrimeBlock> CODEC = simpleCodec(GrimeBlock::new);
    private static final VoxelShape SHAPE = DoubleAquaticFlowerBlock.column(16.0F, 0.0F, 14.0F);

    public MapCodec<GrimeBlock> codec() {
        return CODEC;
    }

    public GrimeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected VoxelShape getCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.is(ItemTags.SHOVELS)) {
            assert !level.isClientSide();
            level.setBlock(pos, ModBlocks.GRIMEBARREL.defaultBlockState(), 11);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
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
            level.levelEvent(2001, pos, DoubleAquaticFlowerBlock.getId(state));
        }
        assert user != null;
        return new ItemStack(ModItems.COPPER_SIFT_FULL_GRIME);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
