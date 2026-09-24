package geoves.grimeandgold.entities.ai.behaviours;

import com.google.common.collect.ImmutableMap;
import geoves.grimeandgold.entities.ai.MemoryModuleTypes;
import geoves.grimeandgold.entities.mobs.siftgrub.SiftGrub;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class Sifting<E extends SiftGrub> extends Behavior<E> {
    private IntProvider siftCooldown;

    public Sifting(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
        super(entryCondition, minDuration, maxDuration);
    }

    public Sifting(IntProvider duration, IntProvider cooldown) {
        super(
                ImmutableMap.of(
                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypes.SIFT_COOLDOWN, MemoryStatus.VALUE_ABSENT
                ),
                duration.minInclusive(),
                duration.maxInclusive()
        );
        this.siftCooldown = cooldown;
    }

    public Sifting(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        super(entryCondition);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
        return body.getRemovalReason() == null;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E body) {
        return body.onGround();
    }

    @Override
    protected void start(ServerLevel level, E body, long timestamp) {
        super.start(level, body, timestamp);
        if (body.onGround()) {
            body.updateState(SiftGrub.State.SIFTING);
        }
        else {
            this.stop(level, body, timestamp);
        };
    }

    @Override
    protected void stop(ServerLevel level, E body, long timestamp) {
        super.stop(level, body, timestamp);
        body.updateState(SiftGrub.State.IDLING);
        body.getBrain().setMemory(MemoryModuleTypes.SIFT_COOLDOWN, siftCooldown.sample(level.getRandom()));
    }
}
