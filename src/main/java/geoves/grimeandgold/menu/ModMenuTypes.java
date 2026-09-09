package geoves.grimeandgold.menu;

import geoves.grimeandgold.GrimeAndGold;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final MenuType<GrimeBarrelMenu> GRIME_BARREL_MENU =
            Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "grimebarrel_menu"),
                    new ExtendedMenuType<>(GrimeBarrelMenu::new, BlockPos.STREAM_CODEC));

    public static void registerModMenuTypes() {
        GrimeAndGold.LOGGER.info("Registering ModMenuTypes for " + GrimeAndGold.MOD_ID);
    }
}
