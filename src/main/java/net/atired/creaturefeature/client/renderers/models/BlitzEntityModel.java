package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BlitzEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BlitzEntityModel<T extends BlitzEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("blitzentitymodel"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart low_cube;
	private final ModelPart top_cube;
	private final ModelPart mid_cube;
	private final ModelPart root;
	public BlitzEntityModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.low_cube = this.body.getChild("low_cube");
		this.top_cube = this.body.getChild("top_cube");
		this.mid_cube = this.body.getChild("mid_cube");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 36).addBox(-4.0F, -2.0F, -8.0F, 8.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 2.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 36).addBox(-3.0F, -5.0F, -6.0F, 6.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition low_cube = body.addOrReplaceChild("low_cube", CubeListBuilder.create().texOffs(48, 20).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition top_cube = body.addOrReplaceChild("top_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -7.0F, 14.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition mid_cube = body.addOrReplaceChild("mid_cube", CubeListBuilder.create().texOffs(0, 20).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}



	@Override
	public void setupAnim(BlitzEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.low_cube.yRot=ageInTicks*1.5f/4.0f;
		this.mid_cube.yRot=-ageInTicks*1.25f/4.0f;
		this.top_cube.yRot=ageInTicks/4.0f;
		this.low_cube.y=-1+Mth.sin(1.5f*ageInTicks/10.0f)/6.0f;
		this.mid_cube.y=-10-Mth.sin(1.25f*ageInTicks/120.0f)/6.0f;
		this.top_cube.y=-17+Mth.sin(ageInTicks/10.0f)/6.0f;
		this.body.xRot=limbSwingAmount/4.0f;
		this.body.z=limbSwingAmount*1.0f;
		this.low_cube.z=limbSwingAmount*5.0f;
		this.mid_cube.z=limbSwingAmount*4.0f;
		this.top_cube.z=limbSwingAmount*2.0f;
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