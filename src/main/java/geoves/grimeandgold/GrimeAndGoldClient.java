package geoves.grimeandgold;

import geoves.grimeandgold.menu.GrimeBarrelScreen;
import geoves.grimeandgold.menu.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class GrimeAndGoldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.GRIME_BARREL_MENU, GrimeBarrelScreen::new);
    }
}
