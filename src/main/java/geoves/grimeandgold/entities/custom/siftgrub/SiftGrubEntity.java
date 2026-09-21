package geoves.grimeandgold.entities.custom.siftgrub;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import net.tslat.smartbrainlib.api.internal.SmartBrainProvider;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SiftGrubEntity extends AgeableWaterCreature implements SmartBrainOwner<SiftGrubEntity>, Bucketable {
    private static final boolean USE_SLB = GrimeAndGold.USE_SBL;
    private static final Brain.Provider<SiftGrubEntity> BRAIN_PROVIDER = SiftGrubAi.getBrainProvider();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SiftGrubEntity.class, EntityDataSerializers.BOOLEAN);

    public SiftGrubEntity(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super(type, level);
        if (level.isClientSide()) {
            this.walkAnimationState.start(this.age);
            this.idleAnimationState.start(this.age);
        }
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.navigation = new AmphibiousPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Feel free to mess with these whenever you want. Might add more attributes later
        return Animal.createAnimalAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.1)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.14F);
    }
    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected Brain<? extends LivingEntity> makeBrain(Brain.Packed packedBrain) {
        if (USE_SLB) {
            return new SmartBrainProvider<>(this).makeBrain(this, packedBrain);
        }
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<SiftGrubEntity> getBrain() {
        return (Brain<SiftGrubEntity>) super.getBrain();
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SiftGrubEntity siftGrubEntity) {
        return SiftGrubAi.getSensors(siftGrubEntity);
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SiftGrubEntity owner) {
        return SiftGrubAi.getAlwaysRunningBehaviours(owner);
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SiftGrubEntity owner) {
        return SiftGrubAi.getIdleBehaviours(owner);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (USE_SLB) {
            this.getBrain().tick(level, this);
        }
        ProfilerFiller profiler = Profiler.get();
        profiler.push("siftGrubBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("siftGrubActivityUpdate");
        SiftGrubAi.updateActivity(this);
        profiler.pop();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.animate();
        }
    }

    private void animate() {
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFromBucket(input.getBooleanOr("FromBucket", false));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SIFT_GRUB_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_TADPOLE;  // Todo: this ok?
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }
}
