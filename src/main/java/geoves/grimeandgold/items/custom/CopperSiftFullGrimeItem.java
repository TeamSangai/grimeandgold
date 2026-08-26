package geoves.grimeandgold.items.custom;

import geoves.grimeandgold.items.ModItems;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

public class CopperSiftFullGrimeItem extends BlockItem {

    public CopperSiftFullGrimeItem(Block block, Properties properties) {
        super(block, properties);
    }
    @Override
    protected boolean canPlace(final BlockPlaceContext context, final BlockState stateForPlacement) {
        Player player = context.getPlayer();
        assert player != null;
        return (!player.isInFluid(FluidTags.WATER) && !this.mustSurvive() || !player.isInFluid(FluidTags.WATER) && stateForPlacement.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(stateForPlacement, context.getClickedPos(), CollisionContext.placementContext(player));
    }
    @Override
    public InteractionResult place(final BlockPlaceContext placeContext) {
        if (!this.getBlock().isEnabled(placeContext.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!placeContext.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext updatedPlaceContext = this.updatePlacementContext(placeContext);
            if (updatedPlaceContext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState placementState = this.getPlacementState(updatedPlaceContext);
                if (placementState == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(updatedPlaceContext, placementState)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos pos = updatedPlaceContext.getClickedPos();
                    Level level = updatedPlaceContext.getLevel();
                    Player player = updatedPlaceContext.getPlayer();
                    ItemStack itemStack = updatedPlaceContext.getItemInHand();
                    BlockState placedState = level.getBlockState(pos);
                    if (placedState.is(placementState.getBlock())) {
                        this.updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
                        placedState.getBlock().setPlacedBy(level, pos, placedState, player, itemStack);
                        if (player instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, itemStack);
                        }
                    }

                    SoundType soundType = placedState.getSoundType();
                    level.playSound(player, pos, this.getPlaceSound(placedState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));
                    assert player != null;
                    itemStack.consume(1, player);
                    player.addItem(ModItems.COPPER_SIFT_EMPTY.getDefaultInstance());
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }



    @Override
    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 175;
    }

    @Override
    public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack itemStack, final int ticksRemaining) {
        if (ticksRemaining == 0 && livingEntity instanceof Player player) {
            if (player.isInFluid(FluidTags.WATER)) {
                itemStack.transmuteCopy(ModItems.COPPER_SIFT_EMPTY);
                return;
            }
            livingEntity.releaseUsingItem();
        }
        livingEntity.releaseUsingItem();
    }

}
