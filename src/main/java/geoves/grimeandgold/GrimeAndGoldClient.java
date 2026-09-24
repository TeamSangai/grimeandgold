package geoves.grimeandgold;

import geoves.grimeandgold.client.ModelLayers;
import geoves.grimeandgold.client.renderers.SiftFlyRenderer;
import geoves.grimeandgold.client.renderers.SiftGrubRenderer;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.menu.GrimeBarrelScreen;
import geoves.grimeandgold.menu.ModMenuTypes;
import geoves.grimeandgold.menu.SlagFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class GrimeAndGoldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.GRIME_BARREL_MENU, GrimeBarrelScreen::new);
        MenuScreens.register(ModMenuTypes.SLAG_FURNACE_MENU, SlagFurnaceScreen::new);
        ModelLayers.registerModelLayers();

        this.registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SIFT_FLY, SiftFlyRenderer::new);
        EntityRenderers.register(ModEntityTypes.SIFT_GRUB, SiftGrubRenderer::new);
    }
}
