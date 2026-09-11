package geoves.grimeandgold.entities.custom;

import geoves.grimeandgold.entities.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SiftFlyEntity extends Animal {
    public SiftFlyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    // Feel free to mess with this whenever you want. Might add more attributes later
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.OXYGEN_BONUS, 3.0F);  // Idk how much this affects it
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BreedGoal(this, 1.0));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.GRASS_BLOCK);  // breeding item, change this to whatever
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntityTypes.SIFT_FLY.create(level, EntitySpawnReason.BREEDING);
    }
}
