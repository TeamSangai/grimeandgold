package geoves.grimeandgold.entities.ai;

import geoves.grimeandgold.GrimeAndGold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;

public class ActivityTypes extends Activity {
    public static final Activity SEEK_WATER = register("seek_water");
    public static final Activity SEARCH = register("search");

    public ActivityTypes(String name) {
        super(name);
    }

    private static Activity register(final String name) {
        return Registry.register(BuiltInRegistries.ACTIVITY, name, new Activity(name));
    }

    public static void registerCustomActivities() {
        GrimeAndGold.LOGGER.info("Registering custom activities for " + GrimeAndGold.MOD_ID);
    }
}
