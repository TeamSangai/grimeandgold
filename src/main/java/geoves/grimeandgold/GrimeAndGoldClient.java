package geoves.grimeandgold;

import geoves.grimeandgold.client.ModelLayers;
import geoves.grimeandgold.client.models.SiftGrubModel;
import geoves.grimeandgold.client.renderers.layers.SiftGrubHeldItemLayer;
import geoves.grimeandgold.client.renderers.mobs.SiftFlyRenderer;
import geoves.grimeandgold.client.renderers.mobs.SiftGrubRenderer;
import geoves.grimeandgold.client.renderstates.SiftGrubRenderState;
import geoves.grimeandgold.entities.ModEntityTypes;
import geoves.grimeandgold.menu.GrimeBarrelScreen;
import geoves.grimeandgold.menu.ModMenuTypes;
import geoves.grimeandgold.menu.SlagFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.RenderLayerParent;

public class GrimeAndGoldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.GRIME_BARREL_MENU, GrimeBarrelScreen::new);
        MenuScreens.register(ModMenuTypes.SLAG_FURNACE_MENU, SlagFurnaceScreen::new);
        ModelLayers.registerModelLayers();

        this.registerEntityRenderers();

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType.equals(ModEntityTypes.SIFT_GRUB)) {
                registrationHelper.register(new SiftGrubHeldItemLayer((RenderLayerParent<SiftGrubRenderState, SiftGrubModel>) entityRenderer));
            }
        });
    }

    private void registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SIFT_FLY, SiftFlyRenderer::new);
        EntityRenderers.register(ModEntityTypes.SIFT_GRUB, SiftGrubRenderer::new);
    }
}
