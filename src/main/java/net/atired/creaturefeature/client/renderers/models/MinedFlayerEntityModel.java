package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;


public class MinedFlayerEntityModel<T extends MinedFlayerEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("minedflayerentitymodel"), "main");
	private final ModelPart head;
	private final ModelPart jelly;
	private final ModelPart wobble;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart root;

	public MinedFlayerEntityModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root=root;
		this.head = root.getChild("head");
		this.jelly = this.head.getChild("jelly");
		this.wobble = this.head.getChild("wobble");
		this.left_arm = this.wobble.getChild("left_arm");
		this.right_arm = this.wobble.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 9.8F, -0.0333F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 0.0333F, -0.0873F, 0.0F, 0.0F));

		PartDefinition jelly = head.addOrReplaceChild("jelly", CubeListBuilder.create(), PartPose.offset(0.0F, -5.6F, 1.0333F));

		PartDefinition cube_r2 = jelly.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -6.5F, -9.0F, 18.0F, 13.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.3F, -1.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition wobble = head.addOrReplaceChild("wobble", CubeListBuilder.create().texOffs(32, 33).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.1F, -0.0667F));

		PartDefinition left_arm = wobble.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, 8.8F, -2.9F));

		PartDefinition cube_r3 = left_arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, -4.5F, -1.0F, 4.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -0.5672F, 0.0F));

		PartDefinition right_arm = wobble.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, 8.8F, -2.9F));

		PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-2.0F, -4.5F, -1.0F, 4.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.5672F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(MinedFlayerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot=headPitch/180.0f*3.14f;
		this.right_arm.xRot=Mth.sin(ageInTicks/4.0f)/8.0f;
		this.left_arm.xRot=-Mth.sin(ageInTicks/4.0f)/8.0f;
		float scaled = Mth.sin(ageInTicks/12.0f)/12.0f+Mth.sin(ageInTicks/2.0f)/8.0f*entity.selfRot;
		this.head.xRot+=Mth.sin(ageInTicks/2.0f)/5.0f*entity.selfRot;
		this.jelly.yScale=1.0f+scaled;
		this.jelly.xScale=1.0f-scaled;
		this.jelly.zScale=1.0f-scaled;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
		super.renderToBuffer(poseStack, vertexConsumer, i, i1, i2);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}