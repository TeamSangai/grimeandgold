package geoves.grimeandgold.client;

import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.client.models.SiftFlyModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModelLayers {
    public static final ModelLayerLocation SIFT_FLY = new ModelLayerLocation(Identifier.fromNamespaceAndPath(GrimeAndGold.MOD_ID, "sift_fly"), "main");

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(SIFT_FLY, SiftFlyModel::createBodyLayer);
    }
}
