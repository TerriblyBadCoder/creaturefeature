package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CanaryEntity;
import net.atired.creaturefeature.entity.CannonballCrabEntity;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class ToadstoolEntityModel<T extends ToadstoolEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("toadstoolentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart left_arm;

	public ToadstoolEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 31).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 18.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 31).mirror().addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 18.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -4.0F, -7.0F, 14.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 42).addBox(-5.5F, -4.5F, -6.5F, 11.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -5.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, -2.3F, 0.0F));

		PartDefinition cube_r1 = right_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 31).addBox(-3.0F, -6.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, -2.3F, 0.0F));

		PartDefinition cube_r2 = left_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-1.0F, -6.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ToadstoolEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.yRot=netHeadYaw/180.0f/5.0f*3.14f;
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*2.2f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*2.2f;

		this.left_arm.xRot=-Mth.sin(limbSwing)*limbSwingAmount*1.6f;
		this.left_arm.xRot+=Mth.sin(ageInTicks/8.0f)/2.0f;
		this.left_arm.zRot=Mth.cos(ageInTicks/8.0f)/2.0f;

		this.right_arm.xRot=Mth.sin(limbSwing)*limbSwingAmount*1.6f;
		this.right_arm.xRot-=Mth.sin(ageInTicks/8.0f)/2.0f;
		this.right_arm.zRot=-Mth.cos(ageInTicks/8.0f)/2.0f;
		this.head.xRot-=Mth.sin(ageInTicks/9.0f)/7.0f;
		this.head.zRot=-Mth.cos(ageInTicks/9.0f)/7.0f;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}