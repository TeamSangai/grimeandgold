package geoves.grimeandgold.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class SiftFlyEntity extends Mob {

    protected SiftFlyEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }
}
