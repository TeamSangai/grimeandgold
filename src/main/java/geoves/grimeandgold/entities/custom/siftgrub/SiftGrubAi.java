package geoves.grimeandgold.entities.custom.siftgrub;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.ActivityTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
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

    protected static Brain.Provider<SiftGrubEntity> getBrainProvider() {
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

    protected static void initMemories(SiftGrubEntity SiftGrub, RandomSource random) {
        GrimeAndGold.LOGGER.info("SiftGrubAI: yo wassup");
    }

    protected static List<ActivityData<SiftGrubEntity>> getActivities(SiftGrubEntity SiftGrub) {
        return List.of(initCoreActivity(), initIdleActivity(), initFindWaterActivity());
    }

    private static ActivityData<SiftGrubEntity> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic<>(PANIC_SPEED_MULTIPLIER),
                        new LookAtTargetSink(LOOK_DURATION_MIN, LOOK_DURATION_MAX),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
                )
        );
    }

    /**
     * walk around
     * sift
     *      if sifty:
     *          take back to burrow, reduce time to pupate
     * idle
     * repeat
     */
    private static ActivityData<SiftGrubEntity> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(
                                0,
                                new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.ORDERED,
                                        GateBehavior.RunningPolicy.TRY_ALL,
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.swim(1.0F), 2),
                                                Pair.of(SetWalkTargetFromLookTarget.create(0.5F, 3), 3),
                                                Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), 5)
                                        )
                                )
                        ),
                        Pair.of(
                                1,
                                new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.swim(1.0F), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(
                                MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT
                        )
                )
        );
    }

    private static ActivityData<SiftGrubEntity> initFindWaterActivity() {
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

    public static void updateActivity(SiftGrubEntity body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ActivityTypes.SEEK_WATER, Activity.IDLE));
    }

     // <=================== SBL STUFF BELOW ====================>

    protected static List<? extends ExtendedSensor<?>> getSensors(SiftGrubEntity siftGrubEntity) {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>(),
                new HurtBySensor<>(),
                new InWaterSensor<>()
        );
    }

    protected static List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SiftGrubEntity owner) {
        return List.of(
                new LookAtTarget<>().runFor(45, 90),
                new MoveToWalkTarget<>()
        );
    }

    protected static List<? extends BehaviorControl<?>> getIdleBehaviours(SiftGrubEntity owner) {
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
