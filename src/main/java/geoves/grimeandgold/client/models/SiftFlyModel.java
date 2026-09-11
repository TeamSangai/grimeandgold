package geoves.grimeandgold.client.models;

import geoves.grimeandgold.client.renderstates.SiftFlyRenderState;
import geoves.grimeandgold.entities.custom.animations.SiftFlyAnimation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class SiftFlyModel extends EntityModel<SiftFlyRenderState>  {
    private final ModelPart Main;
    private final ModelPart legs_right;
    private final ModelPart legs_left;
    private final ModelPart wingl;
    private final ModelPart wingr;
    private final KeyframeAnimation flyingAnimation;

    public SiftFlyModel(ModelPart root) {
        super(root);
        this.Main = root.getChild("Main");
        this.legs_right = this.Main.getChild("legs_right");
        this.legs_left = this.Main.getChild("legs_left");
        this.wingl = this.Main.getChild("wingl");
        this.wingr = this.Main.getChild("wingr");
        this.flyingAnimation = SiftFlyAnimation.FLYING.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -6.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(22, 15).addBox(-3.0F, -5.0F, 2.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(2, 28).addBox(-0.5F, -1.0F, -7.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(1.0F, -6.25F, -7.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 25).addBox(-3.0F, -6.25F, -7.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 23).addBox(-1.5F, -4.0F, -7.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(23, 25).addBox(-6.0F, -10.0F, -6.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(23, 29).mirror().addBox(2.0F, -10.0F, -6.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition legs_right = Main.addOrReplaceChild("legs_right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = legs_right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 27).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 27).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(0.0F, 0.0F, -5.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition legs_left = Main.addOrReplaceChild("legs_left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = legs_left.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 27).addBox(0.0F, 0.0F, -1.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 27).addBox(0.0F, 0.0F, -3.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 25).addBox(0.0F, 0.0F, -5.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition wingl = Main.addOrReplaceChild("wingl", CubeListBuilder.create().texOffs(22, 22).addBox(-9.0F, -7.0F, 1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-11.0F, -7.0F, -5.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition wingr = Main.addOrReplaceChild("wingr", CubeListBuilder.create().texOffs(0, 15).addBox(4.0F, -7.0F, -5.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 23).addBox(4.0F, -7.0F, 1.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SiftFlyRenderState state) {
        super.setupAnim(state);
        this.flyingAnimation.apply((long) state.ageInTicks, 1F);
    }
}
