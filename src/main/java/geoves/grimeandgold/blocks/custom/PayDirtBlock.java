package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class PayDirtBlock extends Block implements SiftPickup {
    public PayDirtBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack pickupBlock(@Nullable LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        if (!level.isClientSide()) {
            level.levelEvent(2001, pos, Block.getId(state));
        }
        assert user != null;
        return new ItemStack(ModItems.COPPER_SIFT_FULL_PAYDIRT);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
