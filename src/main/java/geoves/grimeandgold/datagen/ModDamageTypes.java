package geoves.grimeandgold.datagen;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> SHARP_FLOOR = ResourceKey.create(Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "sharp_floor"));
    public static final ResourceKey<DamageType> FERAL_WOUND = ResourceKey.create(Registries.DAMAGE_TYPE,
            Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "feral_wound"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(SHARP_FLOOR, new DamageType("sharp_floor", 0.1f, DamageEffects.POKING));
        context.register(FERAL_WOUND, new DamageType("feral_wound", 2.5f, DamageEffects.HURT));
    }


    public static DamageSource create(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }
}
