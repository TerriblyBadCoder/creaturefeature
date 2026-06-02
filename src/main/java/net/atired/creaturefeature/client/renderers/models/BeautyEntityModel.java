package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BeautyEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BeautyEntityModel<T extends BeautyEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("beautyentitymodel"), "main");
	private final ModelPart root;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart bone;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart head;
	private final ModelPart sprout1;
	private final ModelPart sprout2;

	public BeautyEntityModel(ModelPart root) {
		this.root = root;
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.bone = root.getChild("bone");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.head = root.getChild("head");
		this.sprout1 = this.head.getChild("sprout1");
		this.sprout2 = this.head.getChild("sprout2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 24).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 9.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 24).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 9.0F, 0.0F));

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 24).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 13).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(32, 37).addBox(-1.0F, 9.0F, 1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -5.0F, 0.1F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 13).mirror().addBox(-2.0F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(32, 37).mirror().addBox(-2.0F, 9.0F, 1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, -5.0F, 0.1F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 0.0F));

		PartDefinition sprout1 = head.addOrReplaceChild("sprout1", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.3381F, -0.0886F, -0.2467F));

		PartDefinition sprout2 = head.addOrReplaceChild("sprout2", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.3054F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void setupAnim(BeautyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * 0.017453292F * 0.3f;
		this.head.xRot = headPitch * 0.017453292F;
		float spun = Math.max(0.0f,entity.getSpin());
		float spunRot =spun*3.14f*4.0f;
		this.root.yRot= spunRot;
		limbSwingAmount*=1.0f-spun;
		this.left_leg.xRot= Mth.sin(limbSwing)*limbSwingAmount/1.0f;
		this.right_leg.xRot= -Mth.sin(limbSwing)*limbSwingAmount/1.0f;
		this.left_arm.xRot= Mth.cos(limbSwing*1.0f)*limbSwingAmount*1.0f+spun/4.0f;
		this.right_arm.xRot= -Mth.cos(limbSwing*1.0f)*limbSwingAmount*1.0f-spun/4.0f;
		this.sprout1.yRot=ageInTicks/8.0f;
		this.sprout2.yRot=-ageInTicks/12.0f;
		this.sprout2.xRot=Mth.cos(ageInTicks/4.0f)/12.0f;

		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.6f;
		this.head.zRot=Mth.sin(ageInTicks/12.0f)/10.0f;
		this.left_arm.zRot=-Mth.cos(ageInTicks/6.0f)/12.0f-0.2f;
		this.right_arm.zRot=Mth.cos(ageInTicks/6.0f)/12.0f+0.2f;
		this.left_arm.xRot-=sinused+Mth.sin(ageInTicks/6.0f)/20.0f+Mth.sin(ageInTicks/6.0f)/12.0f;
		this.left_arm.yRot=-sinused/3.0f+Mth.sin(ageInTicks/6.0f)/20.0f+Mth.sin(ageInTicks/6.0f)/12.0f;
		this.right_arm.xRot-=sinused-Mth.sin(ageInTicks/6.0f)/90.0f;
		this.right_arm.yRot=sinused/3.0f-Mth.sin(ageInTicks/6.0f)/90.0f;


		this.bone.yRot=Mth.sin(limbSwing)*limbSwingAmount/30.0f;
		this.bone.xRot=limbSwingAmount/2.0f;
		this.bone.z=-limbSwingAmount*7.0f;
		this.head.z=-limbSwingAmount*7.0f;
		this.bone.y=-6.0f+limbSwingAmount*2.0f;
		this.head.y=-5.5f+limbSwingAmount*2.0f;
		this.left_arm.z=-limbSwingAmount*7.0f;
		this.right_arm.z=-limbSwingAmount*7.0f;

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}