package geoves.grimeandgold.client.models;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import geoves.grimeandgold.GrimeAndGold;
import geoves.grimeandgold.client.animations.SiftGrubAnimations;
import geoves.grimeandgold.client.renderstates.SiftGrubRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class SiftGrubModel extends EntityModel<SiftGrubRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart Tail;
	private final ModelPart bone;
	private final ModelPart TailEnd;
	private final ModelPart bone2;
	private final ModelPart bone4;
	private final ModelPart Eyes;
	private final ModelPart bone3;
	private final ModelPart legsL;
	private final ModelPart legA;
	private final ModelPart legB;
	private final ModelPart legC;
	private final ModelPart LegD;
	private final ModelPart legsR;
	private final ModelPart legA2;
	private final ModelPart legB2;
	private final ModelPart legC2;
	private final ModelPart LegD2;
	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation idleAnimation;
	private final KeyframeAnimation siftAnimation;

    public SiftGrubModel(ModelPart root) {
        super(root);
		this.Tail = root.getChild("Tail");
		this.bone = this.Tail.getChild("bone");
		this.TailEnd = this.bone.getChild("TailEnd");
		this.bone2 = root.getChild("bone2");
		this.bone4 = this.bone2.getChild("bone4");
		this.Eyes = this.bone4.getChild("Eyes");
		this.bone3 = this.bone4.getChild("bone3");
		this.legsL = root.getChild("legsL");
		this.legA = this.legsL.getChild("legA");
		this.legB = this.legsL.getChild("legB");
		this.legC = this.legsL.getChild("legC");
		this.LegD = this.legsL.getChild("LegD");
		this.legsR = root.getChild("legsR");
		this.legA2 = this.legsR.getChild("legA2");
		this.legB2 = this.legsR.getChild("legB2");
		this.legC2 = this.legsR.getChild("legC2");
		this.LegD2 = this.legsR.getChild("LegD2");
		this.walkAnimation = SiftGrubAnimations.WALK.bake(root);
		this.idleAnimation = SiftGrubAnimations.IDLE.bake(root);
		this.siftAnimation = SiftGrubAnimations.SIFT.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Tail = partdefinition.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition bone = Tail.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition TailEnd = bone.addOrReplaceChild("TailEnd", CubeListBuilder.create().texOffs(20, 7).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 8.0F));

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition bone4 = bone2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 3).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 14).addBox(-1.5F, -3.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Eyes = bone4.addOrReplaceChild("Eyes", CubeListBuilder.create(), PartPose.offset(1.5F, -1.5F, -3.0F));

		PartDefinition cube_r1 = Eyes.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 13).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(20, 13).addBox(1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition bone3 = bone4.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(-2.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-2.0F, -0.25F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, -4.0F));

		PartDefinition legsL = partdefinition.addOrReplaceChild("legsL", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, -2.0F));

		PartDefinition legA = legsL.addOrReplaceChild("legA", CubeListBuilder.create().texOffs(7, 6).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.5F));

		PartDefinition legB = legsL.addOrReplaceChild("legB", CubeListBuilder.create().texOffs(6, 4).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -0.5F));

		PartDefinition legC = legsL.addOrReplaceChild("legC", CubeListBuilder.create().texOffs(6, 2).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.5F));

		PartDefinition LegD = legsL.addOrReplaceChild("LegD", CubeListBuilder.create().texOffs(7, 0).addBox(-2.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.5F));

		PartDefinition legsR = partdefinition.addOrReplaceChild("legsR", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, -2.0F));

		PartDefinition legA2 = legsR.addOrReplaceChild("legA2", CubeListBuilder.create().texOffs(6, 13).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -2.5F));

		PartDefinition legB2 = legsR.addOrReplaceChild("legB2", CubeListBuilder.create().texOffs(6, 11).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -0.5F));

		PartDefinition legC2 = legsR.addOrReplaceChild("legC2", CubeListBuilder.create().texOffs(6, 9).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 1.5F));

		PartDefinition LegD2 = legsR.addOrReplaceChild("LegD2", CubeListBuilder.create().texOffs(6, 7).addBox(0.0F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 3.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(SiftGrubRenderState state) {
		super.setupAnim(state);
		this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 15, 69);
		this.idleAnimation.apply(state.idleAnimationState, state.ageInTicks);  // todo: this doesn't work when not moving
//		this.idleAnimation.applyWalk(state.walkAnimationPos + 22, state.walkAnimationSpeed + 22, 15, 69);  // todo: this doesn't work when not moving
		this.siftAnimation.apply(state.siftAnimationState, state.ageInTicks);
	}
}