package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.enums.Output;
import geoves.grimeandgold.items.ModItems;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;


public class DecomposterBlock extends Block implements WorldlyContainerHolder {
    public static final IntegerProperty VEGETATION = IntegerProperty.create("vegetation", 0, 10);
    public static final IntegerProperty FLESH = IntegerProperty.create("flesh", 0, 10);
    public static final IntegerProperty CALCIUM = IntegerProperty.create("calcium", 0, 10);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final EnumProperty<Output> OUTPUT = EnumProperty.create("output", Output.class, List.of(Output.TBD, Output.BONEMEAL,
            Output.DIRT, Output.CALCITE, Output.GRIME, Output.BIOMASS));
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public DecomposterBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(VEGETATION, 0).setValue(FLESH, 0)
                .setValue(CALCIUM, 0).setValue(OUTPUT, Output.TBD).setValue(ACTIVE, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VEGETATION, FLESH, CALCIUM, OUTPUT, ACTIVE, FACING);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NonNull WorldlyContainer getContainer(BlockState state, LevelAccessor level, BlockPos pos) {
        return null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        assert !level.isClientSide();

        int VegValue = state.getValue(VEGETATION);
        int fleshValue = state.getValue(FLESH);
        int calciumValue = state.getValue(CALCIUM);
        if (state.getValue(ACTIVE) && state.getValue(OUTPUT) == Output.TBD) {
            if (VegValue == 10 && fleshValue == 10 && calciumValue == 0){
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(OUTPUT, Output.BIOMASS));
                GrimeAndGold.LOGGER.info(String.valueOf(state.getValue(OUTPUT)));
            }
            else if (VegValue == 10 && fleshValue == 0 && calciumValue == 0){
                state.setValue(OUTPUT, Output.BONEMEAL);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(OUTPUT, Output.BONEMEAL));
                GrimeAndGold.LOGGER.info(String.valueOf(state.getValue(OUTPUT)));
            }
            else if (VegValue == 0 && fleshValue == 10 && calciumValue == 0){
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(OUTPUT, Output.DIRT));
                GrimeAndGold.LOGGER.info(String.valueOf(state.getValue(OUTPUT)));
            }
            else if (VegValue == 5 && fleshValue == 5 && calciumValue == 5){
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(OUTPUT, Output.GRIME));
                GrimeAndGold.LOGGER.info(String.valueOf(state.getValue(OUTPUT)));
            }
            else if (VegValue == 0 && fleshValue == 0 && calciumValue == 10){
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(OUTPUT, Output.CALCITE));
                GrimeAndGold.LOGGER.info(String.valueOf(state.getValue(OUTPUT)));
            }
        }
    }

    public void ResetValues(Level level, BlockState state, BlockPos pos) {
        level.setBlockAndUpdate(pos, state.setValue(VEGETATION, 0).setValue(FLESH, 0)
                .setValue(CALCIUM, 0).setValue(OUTPUT, Output.TBD));
    }

    public void MakeOutPut(Level level, ItemStack itemStack, Integer count, BlockPos pos){
        Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5F, 1.01, 0.5F).offsetRandomXZ(level.getRandom(), 0.7F);
        itemStack.setCount(count);
        ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), itemStack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack, BlockState state, Level level,
                                                   BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (state.getValue(OUTPUT) == Output.GRIME){
            if (itemStack.is(ModItems.COPPER_SIFT_EMPTY)){
                int durability = itemStack.getDamageValue();
                player.setItemInHand(player.getUsedItemHand(), new ItemStack(ModItems.COPPER_SIFT_FULL_GRIME));
                player.getItemInHand(player.getUsedItemHand()).setDamageValue(durability);
                this.ResetValues(level,state,pos);
                return InteractionResult.SUCCESS;
            }
        }

        if (!state.getValue(ACTIVE) && state.getValue(VEGETATION) != 10) {
            if (itemStack.is(ModTags.Items.DECOMPOSTABLE_VEG_AVERAGE)) {
                if (!level.isClientSide()) {
                    int CurrentVegetation = state.getValue(VEGETATION);
                    level.setBlockAndUpdate(pos, state.setValue(VEGETATION, min(10, CurrentVegetation + 2)));
                }
                if (!player.isCreative()) {itemStack.consume(1, player);}
                return InteractionResult.SUCCESS;
            }if (itemStack.is(ModTags.Items.DECOMPOSTABLE_VEG_LOW)) {
                if (!level.isClientSide()) {
                    int CurrentVegetation = state.getValue(VEGETATION);
                    level.setBlockAndUpdate(pos, state.setValue(VEGETATION, min(10, CurrentVegetation + 1)));
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




    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        assert !level.isClientSide();



        if (state.getValue(OUTPUT) == Output.TBD && !state.getValue(ACTIVE)) {
            int VegValue = state.getValue(VEGETATION);
            int fleshValue = state.getValue(FLESH);
            int calciumValue = state.getValue(CALCIUM);
            if (VegValue == 10 && fleshValue == 10 && calciumValue == 0){
                level.scheduleTick(pos, this, 1600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            }
            if (VegValue == 10 && fleshValue == 0 && calciumValue == 0){
                level.scheduleTick(pos, this, 1600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            }
            if (VegValue == 0 && fleshValue == 10 && calciumValue == 0){
                level.scheduleTick(pos, this, 1600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            }
            if (VegValue == 5 && fleshValue == 5 && calciumValue == 5){
                level.scheduleTick(pos, this, 1600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            }
            if (VegValue == 0 && fleshValue == 0 && calciumValue == 10){
                level.scheduleTick(pos, this, 1600);
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                return InteractionResult.SUCCESS;
            }
        }
        else if (state.getValue(OUTPUT) == Output.BONEMEAL) {
            ItemStack Bonemeal = new ItemStack(Items.BONE_MEAL);
            this.MakeOutPut(level, Bonemeal, 6, pos);
            this.ResetValues(level, state, pos);
            return InteractionResult.SUCCESS;
        }
        else if (state.getValue(OUTPUT) == Output.CALCITE) {
            ItemStack calcite = new ItemStack(Blocks.CALCITE);
            this.MakeOutPut(level, calcite, 2, pos);
            this.ResetValues(level, state, pos);
            return InteractionResult.SUCCESS;
        }
        else if (state.getValue(OUTPUT) == Output.DIRT) {
            ItemStack dirt = new ItemStack(Blocks.DIRT);
            this.MakeOutPut(level, dirt, 2, pos);
            this.ResetValues(level, state, pos);
            return InteractionResult.SUCCESS;
        }
        else if (state.getValue(OUTPUT) == Output.BIOMASS) {
            ItemStack bmass = new ItemStack(ModBlocks.BIOMASS);
            this.MakeOutPut(level, bmass, 2, pos);
            this.ResetValues(level, state, pos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

}
