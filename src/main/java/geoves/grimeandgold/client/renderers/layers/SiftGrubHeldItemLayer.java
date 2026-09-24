package geoves.grimeandgold.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import geoves.grimeandgold.client.models.SiftGrubModel;
import geoves.grimeandgold.client.renderstates.SiftGrubRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SiftGrubHeldItemLayer extends RenderLayer<SiftGrubRenderState, SiftGrubModel> {
    public SiftGrubHeldItemLayer(RenderLayerParent<SiftGrubRenderState, SiftGrubModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       int lightCoords,
                       SiftGrubRenderState state,
                       float yRot,
                       float xRot) {
        ItemStackRenderState item = state.heldItem;
        if (!item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(this.getParentModel().head.x / 16.0F, this.getParentModel().head.y / 16.0F, this.getParentModel().head.z / 16.0F);
            poseStack.translate(0, 1.5F, 0.05F);
            // Todo: clamp this
            poseStack.translate(-Mth.sin(state.yRot * Mth.DEG_TO_RAD) * 0.3F, 0, -Mth.cos(state.yRot * Mth.DEG_TO_RAD) * 0.5F);
//            poseStack.translate(-Mth.sin(state.xRot) * 0.1F, 0F, -Mth.cos(state.xRot) * 0.1F);
            poseStack.rotateDegrees(Axis.YN, yRot);
            poseStack.rotateDegrees(Axis.XP, xRot);
            poseStack.rotateDegrees(Axis.XP, 180.0F);  // + state.yRot (also this should be y axis)
            poseStack.scale(0.5F, 0.5F, 0.5F);
            item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
