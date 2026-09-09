package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;


public class DecomposterBlock extends Block implements WorldlyContainerHolder {
    public static final IntegerProperty VEGETATION = IntegerProperty.create("vegetation", 0, 10);
    public static final IntegerProperty FLESH = IntegerProperty.create("flesh", 0, 10);
    public static final IntegerProperty CALCIUM = IntegerProperty.create("calcium", 0, 10);
    public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, 2);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public DecomposterBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(VEGETATION, 0).setValue(FLESH, 0)
                .setValue(CALCIUM, 0).setValue(PROGRESS, 0).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VEGETATION, FLESH, CALCIUM, PROGRESS, ACTIVE);
    }

    @Override
    public @NonNull WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        assert !level.isClientSide();
        if (state.getValue(ACTIVE) && state.getValue(PROGRESS) < 2) {
            level.scheduleTick(pos, this, 600);
            int currentProgress = state.getValue(PROGRESS);
            GrimeAndGold.LOGGER.info("Decomposter tick");
            level.setBlockAndUpdate(pos, state.setValue(PROGRESS,currentProgress + 1));
        } else if (state.getValue(PROGRESS) == 2){
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
        }
    }

    public void ResetValues(Level level, BlockState state, BlockPos pos) {
        level.setBlockAndUpdate(pos, state.setValue(VEGETATION, 0).setValue(FLESH, 0)
                .setValue(CALCIUM, 0).setValue(PROGRESS, 0));
    }


    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, BlockState state, Level level,
                                                   BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!state.getValue(ACTIVE) && state.getValue(VEGETATION) != 10) {
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_VEG_AVERAGE)) {
                if (!level.isClientSide()) {
                    int CurrentVegetation = state.getValue(VEGETATION);
                    level.setBlockAndUpdate(pos, state.setValue(VEGETATION, min(10, CurrentVegetation + 2)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }
        }
        if (!state.getValue(ACTIVE) && state.getValue(FLESH) != 10) {
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_FLESH_LOW)) {
                if (!level.isClientSide()) {
                    int CurrentFlesh = state.getValue(FLESH);
                    level.setBlockAndUpdate(pos, state.setValue(FLESH, min(10, CurrentFlesh + 1)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }
        }
        if (!state.getValue(ACTIVE) && state.getValue(CALCIUM) != 10) {
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_CALCIUM_HIGH)) {
                if (!level.isClientSide()) {
                    int CurrentCalcium = state.getValue(CALCIUM);
                    level.setBlockAndUpdate(pos, state.setValue(CALCIUM, min(10, CurrentCalcium + 3)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_CALCIUM_AVERAGE)) {
                if (!level.isClientSide()) {
                    int CurrentCalcium = state.getValue(CALCIUM);
                    level.setBlockAndUpdate(pos, state.setValue(CALCIUM, min(10, CurrentCalcium + 2)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_CALCIUM_AVERAGEINBUCKET)) {
                if (!level.isClientSide()) {
                    int CurrentCalcium = state.getValue(CALCIUM);
                    level.setBlockAndUpdate(pos, state.setValue(CALCIUM, min(10, CurrentCalcium + 2)));
                }
                if (!player.isCreative()) {
                    ItemStack EmptyBucket = new ItemStack(Items.BUCKET.asItem(), 1);
                    player.setItemInHand(hand, EmptyBucket);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_CALCIUM_LOW)) {
                if (!level.isClientSide()) {
                    int CurrentCalcium = state.getValue(CALCIUM);
                    level.setBlockAndUpdate(pos, state.setValue(CALCIUM, min(10, CurrentCalcium + 1)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    public void MakeOutPut(Level level, ItemStack itemStack, Integer count, BlockPos pos){
        Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5F, 1.01, 0.5F).offsetRandomXZ(level.getRandom(), 0.7F);
        itemStack.setCount(count);
        ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), itemStack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int VegValue = state.getValue(VEGETATION);
        int fleshValue = state.getValue(FLESH);
        int calciumValue = state.getValue(CALCIUM);
        int progressValue = state.getValue(PROGRESS);

        if (!state.getValue(ACTIVE) && VegValue == 10 && fleshValue == 10 && calciumValue == 0) {
            assert !level.isClientSide();
            if (progressValue != 2) {
                level.scheduleTick(pos, this, 600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            } else {
                ItemStack BiomassStack = new ItemStack(ModBlocks.BIOMASS.asItem());
                this.MakeOutPut(level, BiomassStack, 1, pos);
                this.ResetValues(level,state,pos);
            }
        }
        if (!state.getValue(ACTIVE) && VegValue == 10 && fleshValue == 0 && calciumValue == 0) {
            assert !level.isClientSide();
            if (progressValue != 2) {
                level.scheduleTick(pos, this, 600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            } else {
                ItemStack bonemealStack = new ItemStack(Items.BONE_MEAL);
                this.MakeOutPut(level, bonemealStack, 6, pos);
                this.ResetValues(level,state,pos);
            }
        }
        if (!state.getValue(ACTIVE) && calciumValue == 10 && fleshValue == 0 && VegValue == 0) {
            assert !level.isClientSide();
            if (progressValue != 2) {
                level.scheduleTick(pos, this, 600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            } else {
                ItemStack calciteStack = new ItemStack(Blocks.CALCITE.asItem());
                this.MakeOutPut(level, calciteStack, 2, pos);
                this.ResetValues(level,state,pos);
            }
        }
        if (!state.getValue(ACTIVE) && fleshValue == 10 && calciumValue == 0 && VegValue == 0) {
            assert !level.isClientSide();
            if (progressValue != 2) {
                level.scheduleTick(pos, this, 600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            } else {
                ItemStack DirtStack = new ItemStack(Blocks.DIRT.asItem());
                this.MakeOutPut(level, DirtStack, 2, pos);
                this.ResetValues(level,state,pos);
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

}
