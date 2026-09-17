package geoves.grimeandgold.entities.custom.siftgrub;

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
import java.util.Set;

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
        return List.of(initCoreActivity(), initIdleActivity());
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

    private static ActivityData<SiftGrubEntity> initIdleActivity() {  // Todo: add sifting behaviour
        return ActivityData.create(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(
                                0,
                                new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.stroll(1.0F, 5, 3), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)  // Todo: should be in water
                )
        );
    }

    public static void updateActivity(SiftGrubEntity body) {
        body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}
