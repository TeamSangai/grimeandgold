package geoves.grimeandgold.sounds;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent FLY_AMBIENT = registerSoundEvent("fly_ambient");



    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        GrimeAndGold.LOGGER.info("Registering sounds for " + GrimeAndGold.MOD_ID);
    }
}
