package geoves.grimeandgold.entities.custom.siftfly;

import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.sounds.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SiftFlyEntity extends Animal {
    private static final Brain.Provider<SiftFlyEntity> BRAIN_PROVIDER = Brain.provider(
            List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.IS_IN_WATER, SensorType.FOOD_TEMPTATIONS),
            siftFly -> SiftFlyAi.getActivities()
    );

    public SiftFlyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl<>(this, 20, true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.navigation.setCanFloat(false);
    }

    @Override
    protected Brain<? extends LivingEntity> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<SiftFlyEntity> getBrain() {
        return (Brain<SiftFlyEntity>) super.getBrain();
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Feel free to mess with these whenever you want. Might add more attributes later
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.OXYGEN_BONUS, 3.0F);  // Idk how much this affects it
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("siftFlyBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("siftFlyActivityUpdate");
        SiftFlyAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.GRASS_BLOCK);  // breeding item, change this to whatever
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntityTypes.SIFT_FLY.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        SiftFlyAi.initMemories(this, level.getRandom());
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.FLY_AMBIENT;
    }
}
