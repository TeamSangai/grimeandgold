package geoves.grimeandgold.entities.custom.siftfly;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.sounds.ModSounds;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.BreedWithPartner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.Panic;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomFlyTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import net.tslat.smartbrainlib.api.internal.SmartBrainProvider;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SiftFlyEntity extends Animal implements SmartBrainOwner {
    private static final boolean USE_SLB = GrimeAndGold.USE_SBL;
    private static final Brain.Provider<SiftFlyEntity> BRAIN_PROVIDER = SiftFlyAi.getBrainProvider();

    public SiftFlyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl<>(this, 20, true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.navigation = new FlyingPathNavigation(this, level);
        this.navigation.setCanFloat(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Feel free to mess with these whenever you want. Might add more attributes later
        return Animal.createAnimalAttributes()
//                .add(Attributes.OXYGEN_BONUS, 3.0F);  // Idk how much this affects it
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.96F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.TEMPT_RANGE, 69);
    }

    @Override
    protected Brain<? extends LivingEntity> makeBrain(Brain.Packed packedBrain) {
        if (USE_SLB) {
            return new SmartBrainProvider<>(this).makeBrain(this, packedBrain);
        }
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<SiftFlyEntity> getBrain() {
        return (Brain<SiftFlyEntity>) super.getBrain();
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(LivingEntity livingEntity) {
        return ObjectArrayList.of(
//                new NearbyLivingEntitySensor<>(),  // Todo: uncomment when sbl is fixed
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>(),
                new InWaterSensor<>(),
                new ItemTemptingSensor<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(LivingEntity owner) {
        return List.of(
                new LookAtTarget<>().runFor(45, 90),
                new MoveToWalkTarget<>(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(LivingEntity owner) {
        return List.of(
                new FirstApplicableBehaviour<>(
                        new Panic<>().speedModifier(2).setRadius(5, 5),
                        new BreedWithPartner<>().validPartners(ModEntityTypes.SIFT_FLY),
                        new FollowTemptation<>().speedModifier(1.25F),
                        new OneRandomBehaviour<>(
                                new SetRandomFlyTarget<>().setRadius(6),
                                new Idle<>()
                                        .runFor(e -> e.getRandom().nextInt(0, 30))
                                        .startCondition(e -> e.getRandom().nextFloat() > 0.5F)
                        )
                ),
                new SetRandomLookTarget<>()
        );
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        if (USE_SLB) {
            this.getBrain().tick(level, this);
        }
        else {
            ProfilerFiller profiler = Profiler.get();
            profiler.push("siftFlyBrain");
            this.getBrain().tick(level, this);
            profiler.pop();
            profiler.push("siftFlyActivityUpdate");
            SiftFlyAi.updateActivity(this);
            profiler.pop();
        }
        super.customServerAiStep(level);
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }


    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.GRASS_BLOCK);  // breeding item, change this to whatever
    }

    @Override
    protected boolean canBeABaby() {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        SiftFlyEntity s = ModEntityTypes.SIFT_FLY.create(level, EntitySpawnReason.BREEDING);
        if (!USE_SLB && s != null) {  // Todo: sthsth
            SiftFlyAi.initMemories(s, level.getRandom());
        }
        return s;
    }

    @Override
    public void spawnChildFromBreeding(final ServerLevel level, final Animal partner) {
        this.finalizeSpawnChildFromBreeding(level, partner, null);
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        if (!USE_SLB) {
            SiftFlyAi.initMemories(this, level.getRandom());
        }
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.FLY_AMBIENT;
    }

    @Override
    protected void checkFallDamage(final double ya, final boolean onGround, final BlockState onState, final BlockPos pos) {
    }

}
