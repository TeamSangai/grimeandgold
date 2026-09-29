package geoves.grimeandgold.entities.ai.behaviours;

import com.google.common.collect.ImmutableMap;
import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.blocks.ModBlocks;
import geoves.grimeandgold.blocks.interfaces.SiftPickup;
import geoves.grimeandgold.entities.ai.MemoryModuleTypes;
import geoves.grimeandgold.entities.mobs.siftgrub.SiftGrub;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Prediction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class Sifting<E extends SiftGrub> extends Behavior<E> {
    public static final ResourceKey<LootTable> GRIME_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "larva_sifting_grime"));
    public static final ResourceKey<LootTable> GOLDRUST_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "larva_sifting_goldrust"));
    public static final ResourceKey<LootTable> FERRISOIL_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "larva_sifting_ferrisoil"));
    public static final ResourceKey<LootTable> PAYDIRT_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "larva_sifting_paydirt"));

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
        BlockPos pos = body.getOnPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof SiftPickup siftable) {
            ItemStack itemStack = body.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemStack.isEmpty()) {
                body.drop(itemStack, true, Prediction.PREDICTED);
            }
            if (state.is(ModBlocks.GOLDRUST)) {
                LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(GOLDRUST_LOOT);
                LootParams params = new LootParams.Builder(level)  // Is there a better way to do this lol
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(body.getOnPos()))  // Idk if this is right
                        .withParameter(LootContextParams.INTERACTING_ENTITY, body)
                        .withParameter(LootContextParams.BLOCK_STATE, body.getBlockStateOn())
                        .create(LootContextParamSets.BLOCK_INTERACT);
                List<ItemStack> drops = lootTable.getRandomItems(params);
                body.setItemSlot(EquipmentSlot.MAINHAND, drops.get(level.getRandom().nextInt(drops.size())));

            } else if (state.is(ModBlocks.GRIME)) {
                LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(GRIME_LOOT);
                LootParams params = new LootParams.Builder(level)  // Is there a better way to do this lol
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(body.getOnPos()))  // Idk if this is right
                        .withParameter(LootContextParams.INTERACTING_ENTITY, body)
                        .withParameter(LootContextParams.BLOCK_STATE, body.getBlockStateOn())
                        .create(LootContextParamSets.BLOCK_INTERACT);
                List<ItemStack> drops = lootTable.getRandomItems(params);
                body.setItemSlot(EquipmentSlot.MAINHAND, drops.get(level.getRandom().nextInt(drops.size())));

            } else if (state.is(ModBlocks.FERRISOIL)) {
                LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(FERRISOIL_LOOT);
                LootParams params = new LootParams.Builder(level)  // Is there a better way to do this lol
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(body.getOnPos()))  // Idk if this is right
                        .withParameter(LootContextParams.INTERACTING_ENTITY, body)
                        .withParameter(LootContextParams.BLOCK_STATE, body.getBlockStateOn())
                        .create(LootContextParamSets.BLOCK_INTERACT);
                List<ItemStack> drops = lootTable.getRandomItems(params);
                body.setItemSlot(EquipmentSlot.MAINHAND, drops.get(level.getRandom().nextInt(drops.size())));

            } else if (state.is(ModBlocks.PAYDIRT)) {
                LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(PAYDIRT_LOOT);
                LootParams params = new LootParams.Builder(level)  // Is there a better way to do this lol
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(body.getOnPos()))  // Idk if this is right
                        .withParameter(LootContextParams.INTERACTING_ENTITY, body)
                        .withParameter(LootContextParams.BLOCK_STATE, body.getBlockStateOn())
                        .create(LootContextParamSets.BLOCK_INTERACT);
                List<ItemStack> drops = lootTable.getRandomItems(params);
                body.setItemSlot(EquipmentSlot.MAINHAND, drops.get(level.getRandom().nextInt(drops.size())));

            }
        }
        GrimeAndGold.LOGGER.info(state.getBlock().toString());
        GrimeAndGold.LOGGER.info("Is siftable: " + (state.getBlock() instanceof SiftPickup));
        body.updateState(SiftGrub.State.IDLING);
        body.getBrain().setMemory(MemoryModuleTypes.SIFT_COOLDOWN, siftCooldown.sample(level.getRandom()));
    }
}
