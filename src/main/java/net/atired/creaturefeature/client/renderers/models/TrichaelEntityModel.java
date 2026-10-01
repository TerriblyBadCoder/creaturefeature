package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.DetritusEntity;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.atired.creaturefeature.entity.TrichaelEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.util.Mth;


public class TrichaelEntityModel<T extends TrichaelEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("trichaelentitymodel"), "main");
	public static final ModelLayerLocation INNER_LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("trichaelentitymodel"), "inner");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart root;

	public TrichaelEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		return createBodyLayer(0);
	}
	public static LayerDefinition createBodyLayer(float inf) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)), PartPose.offset(-1.5F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)).mirror(false), PartPose.offset(1.5F, 12.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 29).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 12.0F, 4.0F, new CubeDeformation(inf)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)).mirror(false), PartPose.offset(3.0F, -11.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(8, 43).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)), PartPose.offset(-3.0F, -11.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(inf)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(inf))
				.texOffs(0, 16).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(inf)), PartPose.offset(0.0F, -5.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(TrichaelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*1.8f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*1.8f;
		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.yRot=netHeadYaw/180.0f*3.14f;
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*0.4f;
		this.left_arm.xRot=Mth.cos(limbSwing)*limbSwingAmount*0.4f-sinused;
		this.right_arm.xRot=-Mth.cos(limbSwing)*limbSwingAmount*0.4f-sinused;
		this.left_arm.zRot=Mth.sin(ageInTicks/6.0f)/16.0f;
		this.right_arm.zRot=-Mth.sin(ageInTicks/6.0f)/16.0f;
		this.body.zRot=Mth.sin(ageInTicks/12.0f)/20.0f;
		this.body.xRot=Mth.sin(ageInTicks/12.0f)/20.0f;
		this.head.zRot=Mth.sin(ageInTicks/12.0f)/20.0f;
		this.head.xRot+=Mth.sin(ageInTicks/12.0f)/20.0f;
		float danceSin = Math.min(1.0f,Mth.sin(entity.getDancing()*3.14f)*1.5f);
		this.body.xRot-=danceSin/1.2f;
		this.body.zRot-=danceSin/8.0f;
		this.head.xRot+=danceSin;

		float mul = entity.flipped%2==0?1.0f:-1.0f;
		this.left_arm.yRot=danceSin/2.0f*mul;
		this.left_arm.xRot+=danceSin*1.07f*mul;
		this.left_arm.zRot-=danceSin*1.57f;
		this.right_arm.yRot=danceSin/2.0f*mul;
		this.right_arm.xRot-=danceSin*1.07f*mul;
		this.right_arm.zRot+=danceSin*1.57f;
	}


	@Override
	public ModelPart root() {
		return root;
	}
}