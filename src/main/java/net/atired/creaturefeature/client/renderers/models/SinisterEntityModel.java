package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.SinisterEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SinisterEntityModel<T extends SinisterEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("sinisterentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart body;
	private final ModelPart upperbody;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart left_leg;
	private final ModelPart mirror;
	private final ModelPart root;

	public SinisterEntityModel(ModelPart root) {
		this.root = root;
		this.right_leg = root.getChild("right_leg");
		this.body = root.getChild("body");
		this.upperbody = this.body.getChild("upperbody");
		this.head = this.upperbody.getChild("head");
		this.right_arm = this.upperbody.getChild("right_arm");
		this.left_arm = this.upperbody.getChild("left_arm");
		this.left_leg = root.getChild("left_leg");
		this.mirror = root.getChild("mirror");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 42).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 13.0F, 4.1F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.1F, 13.8F, 3.8F, -0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-4.0F, -3.7517F, -11.0217F, 9.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4F, 0.0F, -0.2F, 0.1745F, 0.0F, 0.0F));

		PartDefinition upperbody = body.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.offsetAndRotation(0.1F, 1.6161F, -9.5071F, 1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r2 = upperbody.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 0).addBox(-5.0F, -17.0F, -3.0F, 10.0F, 17.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3839F, 0.5071F, -0.0436F, 0.0F, 0.0F));

		PartDefinition head = upperbody.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.6161F, 3.1071F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(42, 21).addBox(-3.0F, -15.0F, -3.0F, 5.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition right_arm = upperbody.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(4.9F, -4.6161F, 0.5071F));

		PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 58).addBox(-1.0F, -12.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition left_arm = upperbody.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.9F, -4.6161F, 0.5071F, 0.0F, 0.0F, -2.7489F));

		PartDefinition cube_r5 = left_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 47).addBox(-2.0F, -11.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 47).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 13.0F, 4.1F));

		PartDefinition mirror = partdefinition.addOrReplaceChild("mirror", CubeListBuilder.create().texOffs(79, 69).addBox(-8.0F, -15.0F, -1.5333F, 2.0F, 30.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -14.0F, -0.9333F, 14.0F, 28.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 69).addBox(6.0F, -15.0F, -1.5333F, 2.0F, 30.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -6.0667F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(SinisterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float i = 0.0f;
		for(ModelPart part : this.root.getAllParts().toList()){
			if(part.getInitialPose()==PartPose.ZERO)
				part.setInitialPose(part.storePose());
			PartPose pose= part.getInitialPose();
			if(part==this.mirror||part==this.root){
				continue;
			}
			part.setPos(pose.x+Mth.sin(i+ageInTicks/3.0f)/20.0f,pose.y+Mth.cos(i+ageInTicks/3.0f/1.3f)/20.0f,pose.z-Mth.sin(i+ageInTicks/3.0f/1.4f)/10.0f);
			part.xScale=1.0f+Mth.sin(-i+ageInTicks/2.0f)/30.0f;
			part.yScale=1.0f-Mth.sin(-i+ageInTicks/2.5f)/30.0f;
			part.zScale=1.0f+Mth.cos(-i+ageInTicks/2.0f)/30.0f;
			part.zRot=Mth.cos(i+ageInTicks/5.0f)/40.0f+pose.zRot;
			i+=0.5f;


		}
		this.mirror.z=this.mirror.getInitialPose().z-(1.0f-Math.abs(Math.max(0.0f,entity.getMirrorBounce()-(ageInTicks%1)/5.0f)-0.5f)*2.0f)*4.0f;
		this.mirror.zRot=Mth.sin(ageInTicks*1.5f)/32.0f*entity.getMirrorBounce();
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount/1.0f;
		this.right_leg.xRot= -Mth.sin(limbSwing)*limbSwingAmount/1.0f;
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