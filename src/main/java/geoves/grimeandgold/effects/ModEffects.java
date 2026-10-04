package geoves.grimeandgold.effects;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {

    public static final Holder<MobEffect> FERAL_WOUND = registerMobEffect("feral_wound", new FeralWoundEffect(MobEffectCategory.HARMFUL, 0x5d3a1c));

    private static Holder<MobEffect> registerMobEffect(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name), effect);
    }

    public static void registerEffects() {
        GrimeAndGold.LOGGER.info("Registering Effects For " + GrimeAndGold.MOD_ID);
    }
}
