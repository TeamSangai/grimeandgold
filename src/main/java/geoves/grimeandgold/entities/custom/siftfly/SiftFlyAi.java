package geoves.grimeandgold.entities.custom.siftfly;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import geoves.grimeandgold.entities.ModEntityTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class SiftFlyAi {

    protected static void initMemories(SiftFlyEntity siftFly, RandomSource random) {

    }

    protected static List<ActivityData<SiftFlyEntity>> getActivities() {
        return List.of(initCoreActivity(), initIdleActivity());
    }

    private static ActivityData<SiftFlyEntity> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new AnimalPanic<>(2.0F, 5),
                        new LookAtTargetSink(45, 90),
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
                        Pair.of(1, new FollowTemptation(s -> 1.25F)),
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


