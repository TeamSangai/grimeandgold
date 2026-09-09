package geoves.grimeandgold.entities.custom;

import com.google.common.collect.Lists;
import geoves.grimeandgold.blocks.entities.FlynestBlockEntity;
import geoves.grimeandgold.blocks.entities.ModBlockEntities;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdultSiftGrubEntity extends Animal {
    public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AdultSiftGrubEntity.class, EntityDataSerializers.BYTE);;
    private static final int FLAG_HAS_NECTAR = 8;
    private static final int STING_DEATH_COUNTDOWN = 1200;
    private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 600;
    private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;
    private static final int MAX_CROPS_GROWABLE = 10;
    private static final int TOO_FAR_DISTANCE = 48;
    private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
    private static final int RESTRICTED_WANDER_DISTANCE_REDUCTION = 24;
    private static final int DEFAULT_WANDER_DISTANCE_REDUCTION = 16;
    private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
    private static final int HIVE_SEARCH_DISTANCE = 20;
    public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
    public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
    public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
    public static final String TAG_HAS_NECTAR = "HasNectar";
    public static final String TAG_FLOWER_POS = "flower_pos";
    public static final String TAG_HIVE_POS = "hive_pos";
    public static final boolean DEFAULT_HAS_NECTAR = false;
    private static final int DEFAULT_TICKS_SINCE_POLLINATION = 0;
    private static final int DEFAULT_CANNOT_ENTER_HIVE_TICKS = 0;
    private static final int DEFAULT_CROPS_GROWN_SINCE_POLLINATION = 0;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    private int ticksWithoutNectarSinceExitingHive = 0;
    private int stayOutOfHiveCountdown = 0;
    private int numCropsGrownSincePollination = 0;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
    private int remainingCooldownBeforeLocatingNewHive;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
    private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
    private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
    private int remainingCooldownBeforeLocatingNewFlower;
    private @Nullable BlockPos savedFlowerPos;
    private @Nullable BlockPos hivePos;
    private FlyPollinateGoal flyPollinateGoal;
    private FlyGoToHiveGoal goToHiveGoal;
    private FlyGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;

    protected AdultSiftGrubEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createFlyAttributes() {
         return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.ARMOR, 2)
                 .add(Attributes.FLYING_SPEED, 0.3).add(Attributes.MOVEMENT_SPEED, 1);
    }
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FlyEnterHiveGoal());
        this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.25F, (i) -> i.is(ItemTags.BEE_FOOD), false));
        this.goalSelector.addGoal(3, new ValidateHiveGoal());
        this.goalSelector.addGoal(3, new ValidateFlowerGoal());
        this.flyPollinateGoal = new FlyPollinateGoal();
        this.goalSelector.addGoal(4, this.flyPollinateGoal);
        this.goalSelector.addGoal(5, new FollowParentGoal(this, (double)1.25F));
        this.goalSelector.addGoal(5, new FlyLocateHiveGoal());
        this.goToHiveGoal = new FlyGoToHiveGoal();
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new FlyGoToKnownFlowerGoal();
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new FlyGrowCropGoal());
        this.goalSelector.addGoal(8, new FlyWanderGoal());
        this.goalSelector.addGoal(9, new FloatGoal(this));
    }

    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.storeNullable("hive_pos", BlockPos.CODEC, this.hivePos);
        output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
        output.putBoolean("HasNectar", this.hasNectar());
        output.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        output.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        output.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
    }

    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHasNectar(input.getBooleanOr("HasNectar", false));
        this.ticksWithoutNectarSinceExitingHive = input.getIntOr("TicksSincePollination", 0);
        this.stayOutOfHiveCountdown = input.getIntOr("CannotEnterHiveTicks", 0);
        this.numCropsGrownSincePollination = input.getIntOr("CropsGrownSincePollination", 0);
        this.hivePos = (BlockPos)input.read("hive_pos", BlockPos.CODEC).orElse( null);
        this.savedFlowerPos = (BlockPos)input.read("flower_pos", BlockPos.CODEC).orElse(null);
    }

    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - (double)0.3F, this.getX() + (double)0.3F, this.getZ() - (double)0.3F, this.getZ() + (double)0.3F, this.getY((double)0.5F), ParticleTypes.FALLING_NECTAR);
            }
        }
    }

    private void spawnFluidParticle(final Level level, final double x1, final double x2, final double z1, final double z2, final double y, final ParticleOptions dripParticle) {
        level.addParticle(dripParticle, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), (double)0.0F, (double)0.0F, (double)0.0F);
    }

    private boolean doesHiveHaveSpace(final BlockPos hivePos) {
        BlockEntity blockEntity = this.level().getBlockEntity(hivePos);
        if (blockEntity instanceof BeehiveBlockEntity beehiveBlockEntity) {
            return !beehiveBlockEntity.isFull();
        } else {
            return false;
        }
    }

    public void setStayOutOfHiveCountdown(final int ticks) {
        this.stayOutOfHiveCountdown = ticks;
    }

    @Override
    protected void checkFallDamage(final double ya, final boolean onGround, final BlockState onState, final BlockPos pos) {
    }
    @Override

    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected boolean omnidirectionalAirMover() {
        return true;
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    public void setSavedFlowerPos(final @Nullable BlockPos savedFlowerPos) {
        this.savedFlowerPos = savedFlowerPos;
    }


    private boolean isTooFarAway(final BlockPos targetPos) {
        return !this.closerThan(targetPos, 48);
    }

    private @Nullable FlynestBlockEntity getFlynestBlockEntity() {
        if (this.hivePos == null) {
            return null;
        } else {
            return this.isTooFarAway(this.hivePos) ? null : (FlynestBlockEntity) this.level().getBlockEntity(this.hivePos, ModBlockEntities.FLYNEST_BE).orElse( null);
        }
    }
    public boolean hasHive() {
        return this.hivePos != null;
    }

    public List<BlockPos> getBlacklistedHives() {
        return this.goToHiveGoal.blacklistedTargets;
    }

    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }

    private void dropHive() {
        this.hivePos = null;
        this.remainingCooldownBeforeLocatingNewHive = 200;
    }

    private void dropFlower() {
        this.savedFlowerPos = null;
        this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
    }

    private void pathfindRandomlyTowards(final BlockPos targetPos) {
        Vec3 targetVec = Vec3.atBottomCenterOf(targetPos);
        int yAdjust = 0;
        BlockPos beePos = this.blockPosition();
        int yDelta = (int)targetVec.y - beePos.getY();
        if (yDelta > 2) {
            yAdjust = 4;
        } else if (yDelta < -2) {
            yAdjust = -4;
        }

        int xzDist = 6;
        int yDist = 8;
        int dist = beePos.distManhattan(targetPos);
        if (dist < 15) {
            xzDist = dist / 2;
            yDist = dist / 2;
        }

        Vec3 nextPosTowards = AirRandomPos.getPosTowards(this, xzDist, yDist, yAdjust, targetVec, (double)((float)Math.PI / 10F));
        if (nextPosTowards != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(nextPosTowards.x, nextPosTowards.y, nextPosTowards.z, (double)1.0F);
        }
    }
    public static boolean attractsFlies(final BlockState state) {
        if (state.is(BlockTags.BEE_ATTRACTIVE)) {
            if ((Boolean)state.getValueOrElse(BlockStateProperties.WATERLOGGED, false)) {
                return false;
            } else if (state.is(Blocks.SUNFLOWER)) {
                return state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public void setHivePos(final BlockPos hivePos) {
        this.hivePos = hivePos;
    }

    private boolean wantsToEnterHive() {
        boolean pollinating = flyPollinateGoal.isPollinating();
        if (this.stayOutOfHiveCountdown <= 0 && !pollinating && this.getTarget() == null) {
            return this.hasNectar() || this.isTiredOfLookingForNectar() || this.level().environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, this.position());
        } else {
            return false;
        }
    }

    private boolean isHiveValid() {
        return this.getFlynestBlockEntity() != null;
    }
    private int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    private void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            if (this.stayOutOfHiveCountdown > 0) {
                --this.stayOutOfHiveCountdown;
            }

            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                --this.remainingCooldownBeforeLocatingNewHive;
            }

            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                --this.remainingCooldownBeforeLocatingNewFlower;
            }
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }

    }
    private boolean getFlag(final int flag) {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
    }

    private void setFlag(final int flag, final boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) | flag));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) & ~flag));
        }

    }
    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }


    private boolean closerThan(final BlockPos targetPos, final int distance) {
        return targetPos.closerThan(blockPosition(), distance);
    }
    public boolean hasNectar() {
        return this.getFlag(8);
    }

    private void setHasNectar(final boolean hasNectar) {
        if (hasNectar) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }

        this.setFlag(8, hasNectar);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.BEE_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }


    private class FlyWanderGoal extends Goal {
        public FlyWanderGoal() {
            super();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return  AdultSiftGrubEntity.this.navigation.isDone() &&  AdultSiftGrubEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return  AdultSiftGrubEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 targetPos = this.findPos();
            if (targetPos != null) {
                 AdultSiftGrubEntity.this.navigation.moveTo( AdultSiftGrubEntity.this.navigation.createPath(BlockPos.containing(targetPos), 1), (double)1.0F);
            }

        }

        private @Nullable Vec3 findPos() {
            Vec3 wanderDirection;
            if ( AdultSiftGrubEntity.this.isHiveValid() && ! AdultSiftGrubEntity.this.closerThan( AdultSiftGrubEntity.this.hivePos, this.getWanderThreshold())) {
                Vec3 hivePosVec = Vec3.atCenterOf( AdultSiftGrubEntity.this.hivePos);
                wanderDirection = hivePosVec.subtract( AdultSiftGrubEntity.this.position()).normalize();
            } else {
                wanderDirection =  AdultSiftGrubEntity.this.getViewVector(0.0F);
            }

            int xzDist = 8;
            Vec3 groundBasedPosition = HoverRandomPos.getPos( AdultSiftGrubEntity.this, 8, 7, wanderDirection.x, wanderDirection.z, ((float)Math.PI / 2F), 3, 1);
            return groundBasedPosition != null ? groundBasedPosition : AirAndWaterRandomPos.getPos( AdultSiftGrubEntity.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, (double)((float)Math.PI / 2F));
        }

        private int getWanderThreshold() {
            int distanceReduction = ! AdultSiftGrubEntity.this.hasHive() && ! AdultSiftGrubEntity.this.hasSavedFlowerPos() ? 16 : 24;
            return 48 - distanceReduction;
        }
    }


    private abstract class BaseFlyGoal extends Goal {
        private BaseFlyGoal() {
            super();
        }

        public abstract boolean canFlyUse();

        public abstract boolean canFlyContinueToUse();

        public boolean canUse() {return this.canFlyUse();}

        public boolean canContinueToUse() {return this.canFlyContinueToUse();}
    }

    public @Nullable BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public class FlyGoToHiveGoal extends BaseFlyGoal {
        public static final int MAX_TRAVELLING_TICKS = 2400;
        private int travellingTicks;
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        private final List<BlockPos> blacklistedTargets;
        private @Nullable Path lastPath;
        private static final int TICKS_BEFORE_HIVE_DROP = 60;
        private int ticksStuck;

        public FlyGoToHiveGoal() {
            super();
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canFlyUse() {
            return AdultSiftGrubEntity.this.hivePos != null && !AdultSiftGrubEntity.this.isTooFarAway(AdultSiftGrubEntity.this.hivePos) && !AdultSiftGrubEntity.this.hasHome() && AdultSiftGrubEntity.this.wantsToEnterHive() && !this.hasReachedTarget(AdultSiftGrubEntity.this.hivePos) && AdultSiftGrubEntity.this.level().getBlockState(AdultSiftGrubEntity.this.hivePos).is(BlockTags.BEEHIVES);
        }

        public boolean canFlyContinueToUse() {
            return this.canFlyUse();
        }

        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            AdultSiftGrubEntity.this.navigation.stop();
            AdultSiftGrubEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (AdultSiftGrubEntity.this.hivePos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(2400)) {
                    this.dropAndBlacklistHive();
                } else if (!AdultSiftGrubEntity.this.navigation.isInProgress()) {
                    if (!AdultSiftGrubEntity.this.closerThan(AdultSiftGrubEntity.this.hivePos, 16)) {
                        if (AdultSiftGrubEntity.this.isTooFarAway(AdultSiftGrubEntity.this.hivePos)) {
                            AdultSiftGrubEntity.this.dropHive();
                        } else {
                            AdultSiftGrubEntity.this.pathfindRandomlyTowards(AdultSiftGrubEntity.this.hivePos);
                        }
                    } else {
                        boolean canReachAllTheWayToTarget = this.pathfindDirectlyTowards(AdultSiftGrubEntity.this.hivePos);
                        if (!canReachAllTheWayToTarget) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && AdultSiftGrubEntity.this.navigation.getPath().sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > 60) {
                                AdultSiftGrubEntity.this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = AdultSiftGrubEntity.this.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean pathfindDirectlyTowards(final BlockPos targetPos) {
            int closeEnough = AdultSiftGrubEntity.this.closerThan(targetPos, 3) ? 1 : 2;
            AdultSiftGrubEntity.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            AdultSiftGrubEntity.this.navigation.moveTo((double)targetPos.getX(), (double)targetPos.getY(), (double)targetPos.getZ(), closeEnough, (double)1.0F);
            return AdultSiftGrubEntity.this.navigation.getPath() != null && AdultSiftGrubEntity.this.navigation.getPath().canReach();
        }

        private boolean isTargetBlacklisted(final BlockPos targetPos) {
            return this.blacklistedTargets.contains(targetPos);
        }

        private void blacklistTarget(final BlockPos targetPos) {
            this.blacklistedTargets.add(targetPos);

            while(this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }

        }






        private void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (AdultSiftGrubEntity.this.hivePos != null) {
                this.blacklistTarget(AdultSiftGrubEntity.this.hivePos);
            }

            AdultSiftGrubEntity.this.dropHive();
        }

        private boolean hasReachedTarget(final BlockPos targetPos) {
            if (AdultSiftGrubEntity.this.closerThan(targetPos, 2)) {
                return true;
            } else {
                Path path = AdultSiftGrubEntity.this.navigation.getPath();
                return path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone();
            }
        }
    }

    public class FlyGoToKnownFlowerGoal extends BaseFlyGoal {
        private static final int MAX_TRAVELLING_TICKS = 2400;
        private int travellingTicks;

        public FlyGoToKnownFlowerGoal() {
            super();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }



        public boolean canFlyUse() {
            return AdultSiftGrubEntity.this.savedFlowerPos != null && !AdultSiftGrubEntity.this.hasHome() && this.wantsToGoToKnownFlower() && !AdultSiftGrubEntity.this.closerThan(AdultSiftGrubEntity.this.savedFlowerPos, 2);
        }

        public boolean canFlyContinueToUse() {
            return this.canFlyUse();
        }

        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            AdultSiftGrubEntity.this.navigation.stop();
            AdultSiftGrubEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (AdultSiftGrubEntity.this.savedFlowerPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(2400)) {
                    AdultSiftGrubEntity.this.dropFlower();
                } else if (!AdultSiftGrubEntity.this.navigation.isInProgress()) {
                    if (AdultSiftGrubEntity.this.isTooFarAway(AdultSiftGrubEntity.this.savedFlowerPos)) {
                        AdultSiftGrubEntity.this.dropFlower();
                    } else {
                        AdultSiftGrubEntity.this.pathfindRandomlyTowards(AdultSiftGrubEntity.this.savedFlowerPos);
                    }
                }
            }
        }

        private boolean wantsToGoToKnownFlower() {
            return AdultSiftGrubEntity.this.ticksWithoutNectarSinceExitingHive > 600;
        }
    }

    private class FlyPollinateGoal extends BaseFlyGoal {
        private static final int MIN_POLLINATION_TICKS = 400;
        private static final double ARRIVAL_THRESHOLD = 0.1;
        private static final int POSITION_CHANGE_CHANCE = 25;
        private static final float SPEED_MODIFIER = 0.35F;
        private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
        private static final float HOVER_POS_OFFSET = 0.33333334F;
        private static final int FLOWER_SEARCH_RADIUS = 5;
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        private @Nullable Vec3 hoverPos;
        private int pollinatingTicks;
        private static final int MAX_POLLINATING_TICKS = 600;
        private Long2LongOpenHashMap unreachableFlowerCache;

        public FlyPollinateGoal() {
            Objects.requireNonNull(AdultSiftGrubEntity.this);
            super();
            this.unreachableFlowerCache = new Long2LongOpenHashMap();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canFlyUse() {
            if (AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            } else if (AdultSiftGrubEntity.this.hasNectar()) {
                return false;
            } else {
                Optional<BlockPos> nearbyPos = this.findNearbyFlower();
                if (nearbyPos.isPresent()) {
                    AdultSiftGrubEntity.this.savedFlowerPos = (BlockPos)nearbyPos.get();
                    AdultSiftGrubEntity.this.navigation.moveTo((double)AdultSiftGrubEntity.this.savedFlowerPos.getX() + (double)0.5F, (double)AdultSiftGrubEntity.this.savedFlowerPos.getY() + (double)0.5F, (double)AdultSiftGrubEntity.this.savedFlowerPos.getZ() + (double)0.5F, (double)1.2F);
                    return true;
                } else {
                    AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(AdultSiftGrubEntity.this.random, 20, 60);
                    return false;
                }
            }
        }

        public boolean canFlyContinueToUse() {
            if (!this.pollinating) {
                return false;
            } else if (!AdultSiftGrubEntity.this.hasSavedFlowerPos()) {
                return false;
            } else if (this.hasPollinatedLongEnough()) {
                return AdultSiftGrubEntity.this.random.nextFloat() < 0.2F;
            } else {
                return true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }

        private boolean isPollinating() {
            return this.pollinating;
        }

        private void stopPollinating() {
            this.pollinating = false;
        }

        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            AdultSiftGrubEntity.this.resetTicksWithoutNectarSinceExitingHive();
        }

        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                AdultSiftGrubEntity.this.setHasNectar(true);
            }

            this.pollinating = false;
            AdultSiftGrubEntity.this.navigation.stop();
            AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewFlower = 200;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (AdultSiftGrubEntity.this.hasSavedFlowerPos()) {
                ++this.pollinatingTicks;
                if (this.pollinatingTicks > 600) {
                    AdultSiftGrubEntity.this.dropFlower();
                    this.pollinating = false;
                    AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewFlower = 200;
                } else {
                    Vec3 flowerPos = Vec3.atBottomCenterOf(AdultSiftGrubEntity.this.savedFlowerPos).add((double)0.0F, (double)0.6F, (double)0.0F);
                    if (flowerPos.distanceTo(AdultSiftGrubEntity.this.position()) > (double)1.0F) {
                        this.hoverPos = flowerPos;
                        this.setWantedPos();
                    } else {
                        if (this.hoverPos == null) {
                            this.hoverPos = flowerPos;
                        }

                        boolean arrivedAtHoverPos = AdultSiftGrubEntity.this.position().distanceTo(this.hoverPos) <= 0.1;
                        boolean shouldSetWantedPos = true;
                        if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
                            AdultSiftGrubEntity.this.dropFlower();
                        } else {
                            if (arrivedAtHoverPos) {
                                boolean shouldChangeHoverPositions = AdultSiftGrubEntity.this.random.nextInt(25) == 0;
                                if (shouldChangeHoverPositions) {
                                    this.hoverPos = new Vec3(flowerPos.x() + (double)this.getOffset(), flowerPos.y(), flowerPos.z() + (double)this.getOffset());
                                    AdultSiftGrubEntity.this.navigation.stop();
                                } else {
                                    shouldSetWantedPos = false;
                                }

                                AdultSiftGrubEntity.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
                            }

                            if (shouldSetWantedPos) {
                                this.setWantedPos();
                            }

                            ++this.successfulPollinatingTicks;
                            if (AdultSiftGrubEntity.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                                this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                                AdultSiftGrubEntity.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                            }

                        }
                    }
                }
            }
        }

        private void setWantedPos() {
            AdultSiftGrubEntity.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), (double)0.35F);
        }

        private float getOffset() {
            return (AdultSiftGrubEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findNearbyFlower() {
            Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(AdultSiftGrubEntity.this.blockPosition(), 5, 5, 5);
            Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();

            for(BlockPos pos : closestNearbyFlowers) {
                long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
                if (AdultSiftGrubEntity.this.level().getGameTime() < unreachableUntilTime) {
                    tempCache.put(pos.asLong(), unreachableUntilTime);
                } else if (AdultSiftGrubEntity.attractsFlies(AdultSiftGrubEntity.this.level().getBlockState(pos))) {
                    Path path = AdultSiftGrubEntity.this.navigation.createPath(pos, 1);
                    if (path != null && path.canReach()) {
                        return Optional.of(pos);
                    }

                    tempCache.put(pos.asLong(), AdultSiftGrubEntity.this.level().getGameTime() + 600L);
                }
            }

            this.unreachableFlowerCache = tempCache;
            return Optional.empty();
        }
    }

    private class FlyLocateHiveGoal extends BaseFlyGoal {
        private FlyLocateHiveGoal() {
            super();
        }

        public boolean canFlyUse() {
            return AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewHive == 0 && !AdultSiftGrubEntity.this.hasHive() && AdultSiftGrubEntity.this.wantsToEnterHive();
        }

        public boolean canFlyContinueToUse() {
            return false;
        }

        public void start() {
            AdultSiftGrubEntity.this.remainingCooldownBeforeLocatingNewHive = 200;
            List<BlockPos> hivesWithSpace = this.findNearbyHivesWithSpace();
            if (!hivesWithSpace.isEmpty()) {
                for(BlockPos posToCheck : hivesWithSpace) {
                    if (!AdultSiftGrubEntity.this.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
                        AdultSiftGrubEntity.this.hivePos = posToCheck;
                        return;
                    }
                }

                AdultSiftGrubEntity.this.goToHiveGoal.clearBlacklist();
                AdultSiftGrubEntity.this.hivePos = (BlockPos)hivesWithSpace.get(0);
            }
        }

        private List findNearbyHivesWithSpace() {
            BlockPos flyPos = AdultSiftGrubEntity.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)AdultSiftGrubEntity.this.level()).getPoiManager();
            Stream<PoiRecord> nearbyHives = poiManager.getInRange((p) -> p.is(PoiTypeTags.BEE_HOME), flyPos, 20, PoiManager.Occupancy.ANY);
            Stream<BlockPos> var10000 = nearbyHives.map(PoiRecord::getPos);
            AdultSiftGrubEntity var10001 = AdultSiftGrubEntity.this;
            return var10000.filter(var10001::doesHiveHaveSpace).sorted(Comparator.comparingDouble((pos) -> pos.distSqr(flyPos))).collect(Collectors.toList());
        }
    }


    private class FlyGrowCropGoal extends BaseFlyGoal {
        private static final int GROW_CHANCE = 30;

        private FlyGrowCropGoal() {
            super();
        }

        public boolean canFlyUse() {
            if (AdultSiftGrubEntity.this.getCropsGrownSincePollination() >= 10) {
                return false;
            } else if (AdultSiftGrubEntity.this.random.nextFloat() < 0.3F) {
                return false;
            } else {
                return AdultSiftGrubEntity.this.hasNectar() && AdultSiftGrubEntity.this.isHiveValid();
            }
        }

        public boolean canFlyContinueToUse() {
            return this.canFlyUse();
        }

        public void tick() {
            if (AdultSiftGrubEntity.this.random.nextInt(this.adjustedTickDelay(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos belowPos = AdultSiftGrubEntity.this.blockPosition().below(i);
                    BlockState belowState = AdultSiftGrubEntity.this.level().getBlockState(belowPos);
                    Block belowBlock = belowState.getBlock();
                    BlockState growState = null;
                    if (belowState.is(BlockTags.BEE_GROWABLES)) {
                        if (belowBlock instanceof CropBlock) {
                            CropBlock cropBlockBelow = (CropBlock)belowBlock;
                            if (!cropBlockBelow.isMaxAge(belowState)) {
                                growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1);
                            }
                        } else if (belowBlock instanceof StemBlock) {
                            int age = belowState.getValue(StemBlock.AGE);
                            if (age < 7) {
                                growState = belowState.setValue(StemBlock.AGE, age + 1);
                            }
                        } else if (belowState.is(Blocks.SWEET_BERRY_BUSH)) {
                            int age = (Integer)belowState.getValue(SweetBerryBushBlock.AGE);
                            if (age < 3) {
                                growState = (BlockState)belowState.setValue(SweetBerryBushBlock.AGE, age + 1);
                            }
                        } else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT)) {
                            BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
                            if (bonemealableBlock.isValidBonemealTarget(AdultSiftGrubEntity.this.level(), belowPos, belowState)) {
                                bonemealableBlock.performBonemeal((ServerLevel)AdultSiftGrubEntity.this.level(), AdultSiftGrubEntity.this.random, belowPos, belowState);
                                growState = AdultSiftGrubEntity.this.level().getBlockState(belowPos);
                            }
                        }

                        if (growState != null) {
                            AdultSiftGrubEntity.this.level().levelEvent(2011, belowPos, 15);
                            AdultSiftGrubEntity.this.level().setBlockAndUpdate(belowPos, growState);
                            AdultSiftGrubEntity.this.incrementNumCropsGrownSincePollination();
                        }
                    }
                }

            }
        }
    }

    private class FlyEnterHiveGoal extends BaseFlyGoal {
        private FlyEnterHiveGoal() {
            super();
        }

        public boolean canFlyUse() {
            if (AdultSiftGrubEntity.this.hivePos != null && AdultSiftGrubEntity.this.wantsToEnterHive() && AdultSiftGrubEntity.this.hivePos.closerToCenterThan(AdultSiftGrubEntity.this.position(), (double)2.0F)) {
                FlynestBlockEntity flynestBlockEntity = AdultSiftGrubEntity.this.getFlynestBlockEntity();
                if (flynestBlockEntity != null) {
                    if (!flynestBlockEntity.isFull()) {
                        return true;
                    }

                    AdultSiftGrubEntity.this.hivePos = null;
                }
            }

            return false;
        }

        public boolean canFlyContinueToUse() {
            return false;
        }

        public void start() {
            FlynestBlockEntity flynestBlockEntity = AdultSiftGrubEntity.this.getFlynestBlockEntity();
            if (flynestBlockEntity != null) {
                flynestBlockEntity.addOccupant(AdultSiftGrubEntity.this);
            }

        }
    }

    public class ValidateFlowerGoal extends BaseFlyGoal {
        private final int validateFlowerCooldown;
        private long lastValidateTick;

        private ValidateFlowerGoal() {
            super();
            this.validateFlowerCooldown = Mth.nextInt(AdultSiftGrubEntity.this.random, 20, 40);
            this.lastValidateTick = -1L;
        }

        public void start() {
            if (AdultSiftGrubEntity.this.savedFlowerPos != null && AdultSiftGrubEntity.this.level().isLoaded(AdultSiftGrubEntity.this.savedFlowerPos) && !this.isFlower(AdultSiftGrubEntity.this.savedFlowerPos)) {
                AdultSiftGrubEntity.this.dropFlower();
            }

        this.lastValidateTick = AdultSiftGrubEntity.this.level().getGameTime();
    }

    public boolean canFlyUse() {
        return AdultSiftGrubEntity.this.level().getGameTime() > this.lastValidateTick + (long)this.validateFlowerCooldown;
    }

    public boolean canFlyContinueToUse() {
        return false;
    }

    private boolean isFlower(final BlockPos flowerPos) {
        return AdultSiftGrubEntity.attractsFlies(AdultSiftGrubEntity.this.level().getBlockState(flowerPos));
    }
}

private class ValidateHiveGoal extends BaseFlyGoal {
    private final int VALIDATE_HIVE_COOLDOWN;
    private long lastValidateTick;

    private ValidateHiveGoal() {
        super();
        this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(AdultSiftGrubEntity.this.random, 20, 40);
        this.lastValidateTick = -1L;
    }

    public void start() {
        if (AdultSiftGrubEntity.this.hivePos != null && AdultSiftGrubEntity.this.level()
                .isLoaded(AdultSiftGrubEntity.this.hivePos) && !AdultSiftGrubEntity.this.isHiveValid()) {
            AdultSiftGrubEntity.this.dropHive();
        }

        this.lastValidateTick = AdultSiftGrubEntity.this.level().getGameTime();
    }

    public boolean canFlyUse() {
        return AdultSiftGrubEntity.this.level().getGameTime() > this.lastValidateTick + (long) this.VALIDATE_HIVE_COOLDOWN;
    }

    public boolean canFlyContinueToUse() {
        return false;
    }
    }
}
