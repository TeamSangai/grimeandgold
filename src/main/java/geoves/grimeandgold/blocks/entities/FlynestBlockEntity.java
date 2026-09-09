package geoves.grimeandgold.blocks.entities;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import geoves.grimeandgold.blocks.custom.FlynestBlock;
import geoves.grimeandgold.entities.custom.AdultSiftGrubEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.Bees;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FlynestBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TAG_FLOWER_POS = "flower_pos";
    private static final String FLIES = "flies";
    private static final List<String> IGNORED_BEE_TAGS = Arrays.asList("Air", "drop_chances", "equipment", "Brain", "CanPickUpLoot", "DeathTime", "fall_distance", "FallFlying", "Fire", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "sleeping_pos", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "hive_pos", "Passengers", "leash", "UUID");
    public static final int MAX_OCCUPANTS = 3;
    private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;
    private static final int MIN_OCCUPATION_TICKS_NECTAR = 2400;
    public static final int MIN_OCCUPATION_TICKS_NECTARLESS = 600;
    private final List<FlynestBlockEntity.FlyData> stored = Lists.newArrayList();
    private @Nullable BlockPos savedFlowerPos;;


    public FlynestBlockEntity(final BlockPos worldPosition, final BlockState blockState) {
        super(ModBlockEntities.FLYNEST_BE, worldPosition, blockState);
    }

    public void setChanged() {
        this.emptyAllLivingFromHive((Player)null, this.level.getBlockState(this.getBlockPos()), FlynestBlockEntity.FlyReleaseStatus.EMERGENCY);


        super.setChanged();
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public boolean isFull() {
        return this.stored.size() == 3;
    }

    public void emptyAllLivingFromHive(final @Nullable Player player, final BlockState state, final FlynestBlockEntity.FlyReleaseStatus releaseReason) {
        List<Entity> releasedFromHive = this.releaseAllOccupants(state, releaseReason);
        if (player != null) {
            for(Entity released : releasedFromHive) {
                if (released instanceof AdultSiftGrubEntity) {
                    AdultSiftGrubEntity fly = (AdultSiftGrubEntity) released;
                    if (player.position().distanceToSqr(released.position()) <= (double)16.0F) {
                        if (!this.isSedated()) {
                            fly.setTarget(player);
                        } else {
                            fly.setStayOutOfHiveCountdown(400);
                        }
                    }
                }
            }
        }

    }

    private List<Entity> releaseAllOccupants(final BlockState state, final FlynestBlockEntity.FlyReleaseStatus releaseStatus) {
        List<Entity> spawned = Lists.newArrayList();
        this.stored.removeIf((occupantEntry) -> releaseOccupant(this.level, this.worldPosition, state, occupantEntry.toOccupant(), spawned, releaseStatus, this.savedFlowerPos));
        if (!spawned.isEmpty()) {
            super.setChanged();
        }

        return spawned;
    }

    @VisibleForDebug
    public int getOccupantCount() {
        return this.stored.size();
    }

    public static int getHoneyLevel(final BlockState blockState) {
        return (Integer)blockState.getValue(FlynestBlock.HONEY_LEVEL);
    }

    @VisibleForDebug
    public boolean isSedated() {
        return CampfireBlock.isSmokeyPos(this.level, this.getBlockPos());
    }

    public void addOccupant(final AdultSiftGrubEntity fly) {
        if (this.stored.size() < 3) {
            fly.stopRiding();
            fly.ejectPassengers();
            fly.dropLeash();
            this.storeFly(FlynestBlockEntity.Occupant.of(fly));
            if (this.level != null) {
                if (fly.hasSavedFlowerPos() && (!this.hasSavedFlowerPos() || this.level.getRandom().nextBoolean())) {
                    this.savedFlowerPos = fly.getSavedFlowerPos();
                }

                BlockPos blockPos = this.getBlockPos();
                this.level.playSound((Entity)null, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(fly, this.getBlockState()));
            }

            fly.discard();
            super.setChanged();
        }
    }

    public void storeFly(final FlynestBlockEntity.Occupant occupant) {
        this.stored.add(new FlynestBlockEntity.FlyData(occupant));
    }

    private static boolean releaseOccupant(final Level level, final BlockPos blockPos, final BlockState state, final FlynestBlockEntity.Occupant flyData, final @Nullable List<Entity> spawned, final FlynestBlockEntity.FlyReleaseStatus releaseStatus, final @Nullable BlockPos savedFlowerPos) {
        if ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, blockPos) && releaseStatus != FlynestBlockEntity.FlyReleaseStatus.EMERGENCY) {
            return false;
        } else {
            Direction facing = (Direction)state.getValue(FlynestBlock.FACING);
            BlockPos facingPos = blockPos.relative(facing);
            boolean frontBlocked = !level.getBlockState(facingPos).getCollisionShape(level, facingPos).isEmpty();
            if (frontBlocked && releaseStatus != FlynestBlockEntity.FlyReleaseStatus.EMERGENCY) {
                return false;
            } else {
                Entity entity = Occupant.createEntity(level, blockPos);
                if (entity != null) {
                    if (entity instanceof AdultSiftGrubEntity) {
                        AdultSiftGrubEntity fly = (AdultSiftGrubEntity) entity;
                        RandomSource random = level.getRandom();
                        if (savedFlowerPos != null && !fly.hasSavedFlowerPos() && random.nextFloat() < 0.9F) {
                            fly.setSavedFlowerPos(savedFlowerPos);
                        }

                        if (releaseStatus == FlynestBlockEntity.FlyReleaseStatus.HONEY_DELIVERED) {
                            fly.dropOffNectar();
                            if (state.is(BlockTags.BEEHIVES, (s) -> s.hasProperty(FlynestBlock.HONEY_LEVEL))) {
                                int honeyLevel = getHoneyLevel(state);
                                if (honeyLevel < 5) {
                                    int levelIncrease = random.nextInt(100) == 0 ? 2 : 1;
                                    if (honeyLevel + levelIncrease > 5) {
                                        --levelIncrease;
                                    }

                                    level.setBlockAndUpdate(blockPos, (BlockState)state.setValue(FlynestBlock.HONEY_LEVEL, honeyLevel + levelIncrease));
                                }
                            }
                        }

                        if (spawned != null) {
                            spawned.add(fly);
                        }

                        float bbWidth = entity.getBbWidth();
                        double delta = frontBlocked ? (double)0.0F : 0.55 + (double)(bbWidth / 2.0F);
                        double spawnX = (double)blockPos.getX() + (double)0.5F + delta * (double)facing.getStepX();
                        double spawnY = (double)blockPos.getY() + (double)0.5F - (double)(entity.getBbHeight() / 2.0F);
                        double spawnZ = (double)blockPos.getZ() + (double)0.5F + delta * (double)facing.getStepZ();
                        entity.snapTo(spawnX, spawnY, spawnZ, entity.getYRot(), entity.getXRot());
                    }

                    level.playSound((Entity)null, blockPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, level.getBlockState(blockPos)));
                    return level.addFreshEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    private static void tickOccupants(final Level level, final BlockPos pos, final BlockState state, final List<FlynestBlockEntity.FlyData> stored, final @Nullable BlockPos savedFlowerPos) {
        boolean changed = false;
        Iterator<FlynestBlockEntity.FlyData> iterator = stored.iterator();

        while(iterator.hasNext()) {
            FlynestBlockEntity.FlyData data = iterator.next();
            if (data.tick()) {
                FlynestBlockEntity.FlyReleaseStatus releaseStatus = data.hasNectar() ? FlynestBlockEntity.FlyReleaseStatus.HONEY_DELIVERED : FlyReleaseStatus.FLY_RELEASED;
                if (releaseOccupant(level, pos, state, data.toOccupant(), null, releaseStatus, savedFlowerPos)) {
                    changed = true;
                    iterator.remove();
                }
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }

    }

    public static void serverTick(final Level level, final BlockPos blockPos, final BlockState state, final FlynestBlockEntity entity) {
        tickOccupants(level, blockPos, state, entity.stored, entity.savedFlowerPos);
        if (!entity.stored.isEmpty() && level.getRandom().nextDouble() < 0.005) {
            double x = (double)blockPos.getX() + (double)0.5F;
            double y = (double)blockPos.getY();
            double z = (double)blockPos.getZ() + (double)0.5F;
            level.playSound((Entity)null, x, y, z, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

    }

    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.stored.clear();
        (input.read("flies", FlynestBlockEntity.Occupant.LIST_CODEC).orElse(List.of())).forEach(this::storeFly);
        this.savedFlowerPos = input.read("flower_pos", BlockPos.CODEC).orElse((BlockPos) null);
    }

    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        output.store("flies", FlynestBlockEntity.Occupant.LIST_CODEC, this.getFlies());
        output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
    }

    protected void applyImplicitComponents(final DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.stored.clear();
        List<Occupant> flies = (components.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
        flies.forEach(this::storeFly);
    }

    protected void collectImplicitComponents(final DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.BEES, new Flies(this.getFlies()));
    }


    public void removeComponentsFromTag(final ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("flies");
    }

    private List<FlynestBlockEntity.Occupant> getFlies() {
        return this.stored.stream().map(FlynestBlockEntity.FlyData::toOccupant).toList();
    }

    public static enum FlyReleaseStatus {
        HONEY_DELIVERED,
        FLY_RELEASED,
        EMERGENCY;

        private FlyReleaseStatus() {
        }
    }

    private static class FlyData {
        private final FlynestBlockEntity.Occupant occupant;
        private int ticksInHive;

        private FlyData(final FlynestBlockEntity.Occupant occupant) {
            this.occupant = occupant;
            this.ticksInHive = occupant.ticksInHive();
        }

        public boolean tick() {
            return this.ticksInHive++ > this.occupant.minTicksInHive;
        }

        public FlynestBlockEntity.Occupant toOccupant() {
            return new FlynestBlockEntity.Occupant(this.occupant.entityData, this.ticksInHive, this.occupant.minTicksInHive);
        }

        public boolean hasNectar() {
            return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false);
        }
    }

    public static record Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) {
        public static final Codec<FlynestBlockEntity.Occupant> CODEC = RecordCodecBuilder.create((i) -> i.group(TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(FlynestBlockEntity.Occupant::entityData), Codec.INT.fieldOf("ticks_in_hive").forGetter(FlynestBlockEntity.Occupant::ticksInHive), Codec.INT.fieldOf("min_ticks_in_hive").forGetter(FlynestBlockEntity.Occupant::minTicksInHive)).apply(i, FlynestBlockEntity.Occupant::new));
        public static final Codec<List<FlynestBlockEntity.Occupant>> LIST_CODEC;
        public static final StreamCodec<RegistryFriendlyByteBuf, FlynestBlockEntity.Occupant> STREAM_CODEC;

        public static FlynestBlockEntity.Occupant of(final Entity entity) {
            FlynestBlockEntity.Occupant var5;
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), FlynestBlockEntity.LOGGER)) {
                TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
                entity.save(output);
                Objects.requireNonNull(output);
                FlynestBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
                CompoundTag entityTag = output.buildResult();
                boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
                var5 = new FlynestBlockEntity.Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
            }

            return var5;
        }

        public static FlynestBlockEntity.Occupant create(final int ticksInHive) {
            return new FlynestBlockEntity.Occupant(TypedEntityData.of(EntityTypes.BEE, new CompoundTag()), ticksInHive, 600);
        }


        public @Nullable Entity createEntity(final Level level, final BlockPos hivePos) {
            CompoundTag entityTag = this.entityData.copyTagWithoutId();
            Objects.requireNonNull(entityTag);
            FlynestBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
            Entity entity = EntityType.loadEntityRecursive((this.entityData.type()), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
            if (entity != null && entity.is(EntityTypeTags.BEEHIVE_INHABITORS)) {
                entity.setNoGravity(true);
                if (entity instanceof AdultSiftGrubEntity) {
                    AdultSiftGrubEntity fly = (AdultSiftGrubEntity) entity;
                    fly.setHivePos(hivePos);
                    setFlyReleaseData(this.ticksInHive, fly);
                }

                return entity;
            } else {
                return null;
            }
        }

        private static void setFlyReleaseData(final int ticksInHive, final AdultSiftGrubEntity fly) {
            updateFlyAge(ticksInHive, fly);
            fly.setInLoveTime(Math.max(0, fly.getInLoveTime() - ticksInHive));
        }

        private static void updateFlyAge(final int ticksInHive, final AdultSiftGrubEntity fly) {
            if (!fly.isAgeLocked()) {
                int age = fly.getAge();
                if (age < 0) {
                    fly.setAge(Math.min(0, age + ticksInHive));
                } else if (age > 0) {
                    fly.setAge(Math.max(0, age - ticksInHive));
                }

            }
        }

        static {
            LIST_CODEC = CODEC.listOf();
            STREAM_CODEC = StreamCodec.composite(TypedEntityData.streamCodec(EntityType.STREAM_CODEC), FlynestBlockEntity.Occupant::entityData, ByteBufCodecs.VAR_INT, FlynestBlockEntity.Occupant::ticksInHive, ByteBufCodecs.VAR_INT, FlynestBlockEntity.Occupant::minTicksInHive, FlynestBlockEntity.Occupant::new);
        }
    }
}
