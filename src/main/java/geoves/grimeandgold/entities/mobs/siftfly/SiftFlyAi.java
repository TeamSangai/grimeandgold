package geoves.grimeandgold.entities.mobs.siftfly;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.entities.ModEntityTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class SiftFlyAi {
    // Feel free to change these
    private static final float PANIC_SPEED_MULTIPLIER = 2.0F;
    private static final int PANIC_FLY_HEIGHT = 5;
    private static final float TEMPTATION_SPEED_MULTIPLIER = 1.25F;
    private static final int LOOK_DURATION_MIN = 45;
    private static final int LOOK_DURATION_MAX = 90;

    protected static Brain.Provider<SiftFlyEntity> getBrainProvider() {
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
                SiftFlyAi::getActivities
        );
    }

    protected static void initMemories(SiftFlyEntity siftFly, RandomSource random) {
        GrimeAndGold.LOGGER.info("SIFTFLYAI: yo wassup");
    }

    protected static List<ActivityData<SiftFlyEntity>> getActivities(SiftFlyEntity siftFly) {
        return List.of(initCoreActivity(), initIdleActivity());
    }

    private static ActivityData<SiftFlyEntity> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic<>(PANIC_SPEED_MULTIPLIER, PANIC_FLY_HEIGHT),
                        new LookAtTargetSink(LOOK_DURATION_MIN, LOOK_DURATION_MAX),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
                )
        );
    }

    private static ActivityData<SiftFlyEntity> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new AnimalMakeLove(ModEntityTypes.SIFT_FLY)),
                        Pair.of(1, new FollowTemptation(s -> TEMPTATION_SPEED_MULTIPLIER)),
                        Pair.of(
                                2,
                                new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.fly(1.0F), 1),
                                                Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivity(SiftFlyEntity body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}


