package geoves.grimeandgold.entities.ai.behaviours;

import com.google.common.collect.ImmutableMap;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.entities.ai.MemoryModuleTypes;
import geoves.grimeandgold.entities.mobs.siftgrub.SiftGrub;
import geoves.grimeandgold.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Prediction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.TransportItemsBetweenContainers;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Store<E extends SiftGrub> extends Behavior<E> {
    private IntProvider storeAttemptCooldown;

    public Store(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
        super(entryCondition, minDuration, maxDuration);
    }

    public Store(IntProvider duration, IntProvider cooldown) {
        super(
                ImmutableMap.of(
                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypes.STORE_COOLDOWN, MemoryStatus.VALUE_ABSENT
                ),
                duration.minInclusive(),
                duration.maxInclusive()
        );
        this.storeAttemptCooldown = cooldown;
    }

    public Store(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
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
            body.updateState(SiftGrub.State.STORING);
        }
        else {
            this.stop(level, body, timestamp);
        };
    }

    @Override
    protected void stop(ServerLevel level, E body, long timestamp) {
        super.stop(level, body, timestamp);
        ItemStack itemStack = body.getItemBySlot(EquipmentSlot.MAINHAND);
        if (itemStack.isEmpty()) {
            body.updateState(SiftGrub.State.IDLING);
            body.getBrain().setMemory(MemoryModuleTypes.STORE_COOLDOWN, storeAttemptCooldown.sample(level.getRandom()));
        } else if (itemStack.is(ModTags.Items.SIFT_LARVA_STORES)) {
            BlockPos pos = body.getOnPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (block.defaultBlockState().is(ModTags.Blocks.BURROWS)){
                BlockEntity blockEntity= level.getBlockEntity(pos);
            }
        }

    }


}
