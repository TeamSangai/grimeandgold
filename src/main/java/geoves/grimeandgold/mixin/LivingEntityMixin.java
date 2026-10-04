package geoves.grimeandgold.mixin;

import geoves.grimeandgold.tags.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Final
    @Mutable
    @Shadow
    protected final EntityEquipment equipment;


    @Shadow
    protected abstract void doHurtEquipment(DamageSource damageSource, float damage, EquipmentSlot... slots);

    public LivingEntityMixin(EntityType<?> type, Level level, EntityEquipment equipment) {
        super(type, level);
        this.equipment = equipment;
    }


    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Inject(method = "hurtServer", at = @At(value = "TAIL"))
    public void hurtServer(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (source.is(ModTags.DamageTypes.DAMAGES_BOOTS) && !this.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
            this.hurtFeet(source, damage * 3);
        }
    }

    @Unique
    private void hurtFeet(DamageSource damageSource, float damage) {
        this.doHurtEquipment(damageSource, damage, EquipmentSlot.FEET);
    }


}
