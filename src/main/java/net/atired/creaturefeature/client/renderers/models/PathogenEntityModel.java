package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BeautyEntity;
import net.atired.creaturefeature.entity.PathogenesisEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class PathogenEntityModel<T extends PathogenesisEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("pathogenentitymodel"), "main");
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart subbody;
	private final ModelPart leg3;
	private final ModelPart root;

	public PathogenEntityModel(ModelPart root) {
		this.root=root;
		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.subbody = this.body.getChild("subbody");
		this.leg3 = root.getChild("leg3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition cube_r1 = leg1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		PartDefinition cube_r2 = leg2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0833F, -0.0667F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5833F, -0.0333F));

		PartDefinition subbody = body.addOrReplaceChild("subbody", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, 0.3333F, -1.5667F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 22).mirror().addBox(-5.0F, -3.6667F, 0.0333F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(14, 22).addBox(2.0F, -3.6667F, 0.0333F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.4167F, 0.0333F));

		PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 17.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		PartDefinition cube_r3 = leg3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(PathogenesisEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float sinused = Mth.sin(ageInTicks/14.0f)/4.0f;
		this.leg1.yRot=ageInTicks/3.0f+0.0f;
		this.leg2.yRot=ageInTicks/3.0f+3.14f*2.0f/3.0f;
		this.leg3.yRot=ageInTicks/3.0f+3.14f*4.0f/3.0f;
		this.body.xRot=limbSwingAmount*0.2f;
		this.head.xRot=-limbSwingAmount*0.66f*0.2f;
		this.leg1.xRot=sinused;
		this.leg2.xRot=sinused;
		this.leg3.xRot=sinused;
		this.leg1.zRot=sinused;
		this.leg2.zRot=sinused;
		this.leg3.zRot=sinused;
		this.leg1.xScale=sinused+1;
		this.leg2.xScale=sinused+1;
		this.leg3.xScale=sinused+1;
		this.head.yRot=-ageInTicks/5.0f;
		this.head.zRot=Mth.sin(ageInTicks/8.0f)/8.0f;
		float scaled = 1.0f-entity.getFading();
		if(scaled<1.0f){
			float partial = ageInTicks%1;
			scaled-= partial*0.2f;
		}
		this.root.y=-23.0f+scaled*23.0f;
		this.root.xScale=scaled;
		this.root.yScale=2.0f-scaled*1.0f;
		this.root.zScale=scaled;
	}
}