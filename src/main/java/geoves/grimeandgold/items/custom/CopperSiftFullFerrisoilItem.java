package geoves.grimeandgold.items.custom;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.items.ModItems;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CopperSiftFullFerrisoilItem extends BlockItem {
    public static final ResourceKey<LootTable> FERRISIOL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "sifting_ferrisiol"));

    public CopperSiftFullFerrisoilItem(Block block, Properties properties) {
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
                    level.playSound(null, pos, this.getPlaceSound(placedState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));
                    assert player != null;
                    itemStack.consume(1, player);
                    if (!player.isCreative()) {player.addItem(ModItems.COPPER_SIFT_EMPTY.getDefaultInstance());}
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
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
            if (player.isInFluid(FluidTags.WATER)) {
                player.startUsingItem(hand);
                return InteractionResult.SUCCESS;
            }
    return InteractionResult.PASS;
    }
    @Override
    public ItemStack finishUsingItem(final ItemStack itemStack, final @NonNull Level level, final @NonNull LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide()) {
            LootParams params = (new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).withLuck(entity.getLuck()).create(LootContextParamSets.COMMAND));
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(FERRISIOL_LOOT);
            List<ItemStack> items = lootTable.getRandomItems(params, level.getRandom());
            for(ItemStack loot : items) {
                player.addItem(loot);
            }
            ItemStack newsift = new ItemStack(ModItems.COPPER_SIFT_EMPTY);
            newsift.hurtAndBreak(1 + itemStack.getDamageValue(), player, player.getUsedItemHand());;
            return newsift;
        }
        return itemStack;
    }
}
