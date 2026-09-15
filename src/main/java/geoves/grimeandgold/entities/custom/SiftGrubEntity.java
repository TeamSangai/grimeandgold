package geoves.grimeandgold.entities.custom;

import geoves.grimeandgold.entities.ModEntityTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreedWithPartner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.Panic;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import net.tslat.smartbrainlib.api.internal.SmartBrainProvider;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SiftGrubEntity extends AgeableWaterCreature implements SmartBrainOwner<SiftGrubEntity>, Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SiftGrubEntity.class, EntityDataSerializers.BOOLEAN);

    public SiftGrubEntity(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super(type, level);
    }
    public static AttributeSupplier.Builder createAttributes() {
        // Feel free to mess with these whenever you want. Might add more attributes later
        return Animal.createAnimalAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.1)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }
    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected Brain<? extends LivingEntity> makeBrain(Brain.Packed packedBrain) {
        return new SmartBrainProvider<>(this).makeBrain(this, packedBrain);
    }

    @Override
    public Brain<SiftGrubEntity> getBrain() {
        return (Brain<SiftGrubEntity>) super.getBrain();
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SiftGrubEntity siftGrubEntity) {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>(),
                new InWaterSensor<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SiftGrubEntity owner) {
        return List.of(
                new LookAtTarget<>().runFor(45, 90),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SiftGrubEntity owner) {
        return List.of(
                new FirstApplicableBehaviour<>(
                        new Panic<>().speedModifier(2).setRadius(5, 5),
                        new OneRandomBehaviour<>(
                                new SetRandomWalkTarget<>(),
                                new Idle<>().runFor(e -> e.getRandom().nextInt(30, 60))
                        )
                ),
                new SetRandomLookTarget<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(SiftGrubEntity owner) {
        return List.of(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<>(0)
        );
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        // Idk what the profiler stuff is, or if it's necessary with SBL
//        ProfilerFiller profiler = Profiler.get();
//        profiler.push("siftGrubBrain");
        this.getBrain().tick(level, this);
//        profiler.pop();
//        profiler.push("siftGrubActivityUpdate");
//        SiftFlyAi.updateActivity(this);
//        profiler.pop();
    }

    @Override
    protected boolean shouldTakeDrowningDamage() {
        return false;
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
        return null;  // Todo
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;  // Todo
    }

    protected SoundEvent getFloppingOnLandSound() {
        return null;  // Todo
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }
}
