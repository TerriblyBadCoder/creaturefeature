package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CanaryEntity;
import net.atired.creaturefeature.entity.CannonballCrabEntity;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;


public class CannonballCrabEntityModel<T extends CannonballCrabEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("cannonball_crabentitymodel"), "main");
	private final ModelPart core;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart headRot;

	public CannonballCrabEntityModel(ModelPart root) {
		this.core = root.getChild("core");
		this.body = this.core.getChild("body");
		this.head = this.core.getChild("head");
		this.headRot = this.head.getChild("headRot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = core.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, 7.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, 0.0F));

		PartDefinition head = core.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition headRot = head.addOrReplaceChild("headRot", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 18.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(CannonballCrabEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot=netHeadYaw/180.0f*3.14f+Mth.sin(ageInTicks/20.0f)/10.0f;
		this.head.xRot=headPitch/180.0f*3.14f+Mth.cos(ageInTicks/20.0f)/10.0f;
		this.core.xRot=limbSwingAmount*0.75f+Mth.cos(ageInTicks/8.0f)/10.0f;
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.5f;
		this.headRot.zRot=sinused;
		this.body.xRot=sinused/10.0f;
		this.head.zRot=sinused/20.0f;
	}


	@Override
	public ModelPart root() {
		return this.core;
	}
}