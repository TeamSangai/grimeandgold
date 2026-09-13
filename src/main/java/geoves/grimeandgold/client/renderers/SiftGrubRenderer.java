package geoves.grimeandgold.client.renderers;

import geoves.grimeandgold.client.ModelLayers;
import geoves.grimeandgold.client.Textures;
import geoves.grimeandgold.client.models.SiftGrubModel;
import geoves.grimeandgold.client.renderstates.SiftGrubRenderState;
import geoves.grimeandgold.entities.custom.SiftGrubEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SiftGrubRenderer extends MobRenderer<SiftGrubEntity, SiftGrubRenderState, SiftGrubModel> {
    public SiftGrubRenderer(EntityRendererProvider.Context context) {
        super(context, new SiftGrubModel(context.bakeLayer(ModelLayers.SIFT_GRUB)), 0.4F);
    }

    @Override
    public Identifier getTextureLocation(SiftGrubRenderState state) {
        return Textures.SIFT_GRUB;
    }

    @Override
    public SiftGrubRenderState createRenderState() {
        return new SiftGrubRenderState();
    }

    @Override
    public void extractRenderState(SiftGrubEntity entity, SiftGrubRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        // stuff
    }
}
