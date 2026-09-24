package geoves.grimeandgold.entities.mobs.siftgrub;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.ai.ActivityTypes;
import geoves.grimeandgold.entities.ai.MemoryModuleTypes;
import geoves.grimeandgold.entities.ai.behaviours.Sifting;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.Panic;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;

public class SiftGrubAi {
    // Feel free to change these
    private static final float PANIC_SPEED_MULTIPLIER = 2.0F;
    private static final int LOOK_DURATION_MIN = 45;
    private static final int LOOK_DURATION_MAX = 90;
    private static final int SIFT_DURATION_MIN = 60;
    private static final int SIFT_DURATION_MAX = 100;
    private static final int SIFT_MIN_COOLDOWN = 69;
    private static final int SIFT_MAX_COOLDOWN = 96;

    protected static Brain.Provider<SiftGrub> getBrainProvider() {
        return Brain.provider(
                List.of(
                        // memory stuff
                ),
                List.of(
                        SensorType.NEAREST_LIVING_ENTITIES,
                        SensorType.HURT_BY,
                        SensorType.IS_IN_WATER,
                        SensorType.FOOD_TEMPTATIONS
                ),
                SiftGrubAi::getActivities
        );
    }

    protected static void initMemories(SiftGrub body, RandomSource random) {
        body.getBrain().setMemory(MemoryModuleTypes.SIFT_COOLDOWN, 69);
        GrimeAndGold.LOGGER.info("SiftGrubAI: yo wassup");
    }

    protected static List<ActivityData<SiftGrub>> getActivities(SiftGrub SiftGrub) {
        return List.of(initCoreActivity(), initIdleActivity(), initFindWaterActivity(), initSearchingActivity());
    }

    private static ActivityData<SiftGrub> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic<>(PANIC_SPEED_MULTIPLIER),
                        new LookAtTargetSink(LOOK_DURATION_MIN, LOOK_DURATION_MAX),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleTypes.SIFT_COOLDOWN),
                        new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS)
                        )
        );
    }

    /**
     * Just chilling
     */
    private static ActivityData<SiftGrub> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RandomLookAround(UniformInt.of(14, 39), 30.0F, 0.0F, 0.0F))
                ),
                ImmutableSet.of(
                        Pair.of(
                                MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT
                        )
                )
        );
    }

    /**
     * walk around and sift
     */
    private static ActivityData<SiftGrub> initSearchingActivity() {
        return ActivityData.create(
                Activity.INVESTIGATE,
                ImmutableList.of(
                        Pair.of(
                                0,
                                new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.SHUFFLED,
                                        GateBehavior.RunningPolicy.RUN_ONE,
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.swim(1.0F), 3),
                                                Pair.of(new DoNothing(5, 30), 2),
                                                Pair.of(new Sifting<>(UniformInt.of(SIFT_DURATION_MIN, SIFT_DURATION_MAX), UniformInt.of(SIFT_MIN_COOLDOWN, SIFT_MAX_COOLDOWN)), 2),
                                                Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), 5)  // idk what this is for
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(
                                MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT
                        ),
                        Pair.of(
                                MemoryModuleTypes.SIFT_COOLDOWN, MemoryStatus.VALUE_ABSENT
                        )
                )
        );
    }

    private static ActivityData<SiftGrub> initFindWaterActivity() {
        return ActivityData.create(
                ActivityTypes.SEEK_WATER,
                ImmutableList.of(
                        Pair.of(0, TryFindLiquid.create(10, 1.0F, FluidTags.WATER)),
                        Pair.of(1, RandomStroll.stroll(1.0F, false))
                ),
                ImmutableSet.of(
                        Pair.of(
                                MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT
                        )
                )
        );
    }

    public static void updateActivity(SiftGrub body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ActivityTypes.SEEK_WATER, Activity.INVESTIGATE, Activity.IDLE));
    }

     // <=================== SBL STUFF BELOW ====================>

    protected static List<? extends ExtendedSensor<?>> getSensors(SiftGrub siftGrub) {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>(),
                new InWaterSensor<>()
        );
    }

    protected static List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SiftGrub owner) {
        return List.of(
                new LookAtTarget<>().runFor(45, 90),
                new MoveToWalkTarget<>()
        );
    }

    protected static List<? extends BehaviorControl<?>> getIdleBehaviours(SiftGrub owner) {
        return List.of(
                new FirstApplicableBehaviour<>(
                        new Panic<>().speedModifier(2).setRadius(5, 5),
                        new OneRandomBehaviour<>(
                                new SetRandomWalkTarget<>(),
                                new Idle<>()
                                        .runFor(e -> e.getRandom().nextInt(30, 60))

                        )
                ),
                new SetRandomLookTarget<>()
        );
    }
}
