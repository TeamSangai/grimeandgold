package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.blocks.entities.GrimeBarrelBlockEntity;
import geoves.grimeandgold.blocks.entities.ModBlockEntities;
import geoves.grimeandgold.blocks.entities.SlagFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class SlagFurnaceBlock extends AbstractFurnaceBlock {


    public SlagFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
                                                                            BlockEntityType<T> type) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(type, ModBlockEntities.SLAG_FURNACE_BE,
                (level1, pos, state, entity) -> entity.tick(level1, pos, state));
    }



    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof SlagFurnaceBlockEntity slagFurnaceBlockEntity){
            player.openMenu(slagFurnaceBlockEntity);
        }
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new SlagFurnaceBlockEntity(worldPosition, blockState);
    }
}
