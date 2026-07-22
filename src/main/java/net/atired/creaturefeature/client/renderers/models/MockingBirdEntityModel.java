package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.MockingBirdEntityRenderer;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.atired.creaturefeature.entity.MockingBirdEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MockingBirdEntityModel<T extends MockingBirdEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("mockingbirdentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart puff;
	private final ModelPart head;
	private final ModelPart root;

	public MockingBirdEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.puff = this.neck.getChild("puff");
		this.head = this.neck.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-1.5F, -3.0F, -2.0F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.1F, 7.0F, 1.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-1.5F, -3.0F, -2.0F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 7.0F, 1.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 1.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 0).addBox(-5.0F, -1.0F, -3.0F, 9.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -13.0F, -1.0F, 0.0436F, 0.0F, 0.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition puff = neck.addOrReplaceChild("puff", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, -1.0F));

		PartDefinition cube_r2 = puff.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 26).addBox(-6.0F, -4.0F, -5.0F, 12.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -12.0F, -13.0F, 4.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(MockingBirdEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.neck.yRot=netHeadYaw/180.0f*3.14f/8.0f+ Mth.sin(ageInTicks/15.0f)/4.0f;
		float wing = entity.getWingspan();
		float legSwung = 1.0f-wing*0.76f;
		this.neck.zRot=Mth.sin(ageInTicks/10.0f)/8.0f;
		this.neck.xRot=Mth.sin(ageInTicks/12.0f)/8.0f;
		this.head.yRot=netHeadYaw/180.0f*3.14f/8.0f*7.0f+3.14f/2.0f*entity.getFlapping();
		this.head.z=-1.0f*entity.getFlapping();
		this.head.x=4.0f*entity.getFlapping();
		this.head.xRot=headPitch/180.0f*3.14f;
		this.left_leg.xRot= Mth.sin(limbSwing)*limbSwingAmount/1.5f*legSwung+wing;
		this.right_leg.xRot= -Mth.sin(limbSwing)*limbSwingAmount/1.5f*legSwung+wing;
		this.body.xRot= Mth.cos(limbSwing)*limbSwingAmount/10.0f-wing/3.0f;
		this.head.xRot=Mth.sin(ageInTicks/4.0f)/24.0f;
		this.body.yRot=Mth.cos(ageInTicks/20.0f)/12.0f;
		this.puff.yRot=Mth.sin(ageInTicks/18.0f)/6.0f;
	}
}