package geoves.grimeandgold.effects;

import geoves.grimeandgold.datagen.ModDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FeralWoundEffect extends MobEffect {
    public static final int DAMAGE_INTERVAL = 225;

    protected FeralWoundEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        mob.hurtServer(level, ModDamageTypes.create(level, ModDamageTypes.FERAL_WOUND), 2.0f);
        if (mob instanceof Player) {
            Player player = (Player)mob;
            player.causeFoodExhaustion(0.005f * (float)(amplification + 1));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        int interval = 225 >> amplification;
        if (interval > 0) {
            return tickCount % interval == 0;
        }
        return true;
    }
}
