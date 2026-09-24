package geoves.grimeandgold.client.renderers.mobs;

import geoves.grimeandgold.client.ModelLayers;
import geoves.grimeandgold.client.Textures;
import geoves.grimeandgold.client.models.SiftFlyModel;
import geoves.grimeandgold.client.renderstates.SiftFlyRenderState;
import geoves.grimeandgold.entities.mobs.siftfly.SiftFlyEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SiftFlyRenderer extends MobRenderer<SiftFlyEntity, SiftFlyRenderState, SiftFlyModel> {
    public SiftFlyRenderer(EntityRendererProvider.Context context) {
        super(context, new SiftFlyModel(context.bakeLayer(ModelLayers.SIFT_FLY)), 0.4F);
    }

    @Override
    public Identifier getTextureLocation(SiftFlyRenderState state) {
        return Textures.SIFT_FLY;
    }

    @Override
    public SiftFlyRenderState createRenderState() {
        return new SiftFlyRenderState();
    }

    @Override
    public void extractRenderState(SiftFlyEntity entity, SiftFlyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        // something
    }
}
