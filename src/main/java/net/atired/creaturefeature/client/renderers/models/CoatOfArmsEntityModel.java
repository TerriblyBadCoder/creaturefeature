package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.CoatOfArmsEntity;
import net.atired.creaturefeature.entity.DetritusEntity;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.util.Mth;

public class CoatOfArmsEntityModel<T extends CoatOfArmsEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("coatofarmsentitymodel"), "main");
	private final ModelPart core;
	private final ModelPart root;
	private final ModelPart coremore;
	private final ModelPart left_arm;
	private final ModelPart right_arm;

	public CoatOfArmsEntityModel(ModelPart root) {
		this.root=root;
		this.core = root.getChild("core");
		this.coremore = this.core.getChild("coremore");
		this.left_arm = this.core.getChild("left_arm");
		this.right_arm = this.core.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(-8.0F, -4.0F, 0.0F));

		PartDefinition coremore = core.addOrReplaceChild("coremore", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-9.0F, -6.0F, -7.0F, 18.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 1.8F, 0.0F));

		PartDefinition left_arm = core.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(16.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = left_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 20).mirror().addBox(-4.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition right_arm = core.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 20).addBox(-2.0F, -1.0F, -3.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(CoatOfArmsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.right_arm.x=-1;
		this.left_arm.x=17;
		this.left_arm.xRot=-(Mth.sin(ageInTicks/12f)/7.0f)-entity.getOpening()*1.5f;
		this.right_arm.xRot=-Mth.sin(ageInTicks/12f)/7.0f-entity.getOpening()*1.5f;
		this.left_arm.zRot=-(-0.05f+Mth.sin(ageInTicks/16f)/16.0f);
		this.right_arm.zRot=-0.05f+Mth.sin(ageInTicks/16f)/16.0f;
		this.core.xRot=entity.getOpening()/2f;
		this.core.z=entity.getOpening()*2f;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}