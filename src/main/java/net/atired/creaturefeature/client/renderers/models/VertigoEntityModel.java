package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.SinisterEntity;
import net.atired.creaturefeature.entity.VertigoEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class VertigoEntityModel<T extends VertigoEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("vertigoentitymodel"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart horns;
	private final ModelPart rhorn;
	private final ModelPart lhorn;
	private final ModelPart body;
	private final ModelPart left_back_leg;
	private final ModelPart left_front_leg;
	private final ModelPart right_back_leg;
	private final ModelPart right_front_leg;

	public VertigoEntityModel(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.horns = this.head.getChild("horns");
		this.rhorn = this.horns.getChild("rhorn");
		this.lhorn = this.horns.getChild("lhorn");
		this.body = this.root.getChild("body");
		this.left_back_leg = this.body.getChild("left_back_leg");
		this.left_front_leg = this.body.getChild("left_front_leg");
		this.right_back_leg = this.body.getChild("right_back_leg");
		this.right_front_leg = this.body.getChild("right_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -9.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -11.0F, -4.0F, 6.0F, 11.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

		PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -6.6F, -4.7F, -1.0036F, 0.0F, 0.0F));

		PartDefinition rhorn = horns.addOrReplaceChild("rhorn", CubeListBuilder.create().texOffs(44, 28).addBox(-2.5F, -7.5F, -2.0F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition lhorn = horns.addOrReplaceChild("lhorn", CubeListBuilder.create().texOffs(44, 28).mirror().addBox(-0.5F, -7.5F, -2.0F, 3.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 9.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.5F, -9.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition left_back_leg = body.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(28, 43).mirror().addBox(-2.0F, -0.5F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.8F, 5.0F, 5.6F));

		PartDefinition left_front_leg = body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(28, 28).mirror().addBox(-2.0F, -1.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.8F, 4.0F, -7.4F));

		PartDefinition right_back_leg = body.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(28, 43).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8F, 5.0F, 5.6F));

		PartDefinition right_front_leg = body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(28, 28).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8F, 4.0F, -7.4F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(VertigoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float rabiesRotation = 3.14f*2.0f*entity.getRabies();
		this.head.zRot=Mth.sin(ageInTicks/4.2f)/30.0f+Mth.sin(rabiesRotation)*entity.getRabies();
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.1f;
		this.head.xRot=-Mth.cos(ageInTicks/4.2f)/30.0f+Mth.cos(rabiesRotation)*(1.0f-Math.abs(entity.getRabies()-0.5f)*2.0f)+
				headPitch/180.0f*3.14f+sinused;

		this.horns.zRot=Mth.sin(ageInTicks/4.2f)/20.0f+Mth.sin(rabiesRotation)*entity.getRabies();
		this.horns.xRot=-Mth.cos(ageInTicks/4.2f)/20.0f;

		this.head.yRot=netHeadYaw/180.0f*3.14f;
		rabiesRotation = Mth.sin(3.14f*1.0f*entity.getRabies())*entity.getRabies()*3.0f;
		this.body.zRot=Mth.sin(ageInTicks/6.0f)/40.0f+Mth.sin(limbSwing)/10.0f*limbSwingAmount;
		this.body.xRot=-Mth.sin(rabiesRotation)*0.7f;
		this.head.y=1.0f-Mth.sin(rabiesRotation)*4.5f;

		this.left_front_leg.zRot=-this.body.zRot+Mth.cos(ageInTicks/4.2f)/30.0f;
		this.right_front_leg.zRot=-this.body.zRot-Mth.cos(ageInTicks/4.2f)/30.0f;
		this.right_back_leg.zRot=-this.body.zRot;
		this.left_back_leg.zRot=-this.body.zRot;

		this.left_front_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*0.66f-this.body.xRot;
		this.right_front_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*0.66f-this.body.xRot;
		this.right_back_leg.xRot=Mth.cos(limbSwing)*limbSwingAmount*0.66f-this.body.xRot;
		this.left_back_leg.xRot=-Mth.cos(limbSwing)*limbSwingAmount*0.66f-this.body.xRot;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}