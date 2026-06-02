package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CanaryEntity;
import net.atired.creaturefeature.entity.CanaryPart;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class CanaryEntityPartModel<T extends CanaryPart> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("canarypartentitymodel"), "main");
	private final ModelPart studs;
	private final ModelPart studded;
	private final ModelPart root;

	public CanaryEntityPartModel(ModelPart root) {
		this.root=root;
		this.studs = root.getChild("studs");
		this.studded = this.studs.getChild("studded");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition studs = partdefinition.addOrReplaceChild("studs", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 12.0F));

		PartDefinition studded = studs.addOrReplaceChild("studded", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, -13.0F));

		PartDefinition cube_r1 = studded.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 25).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r2 = studded.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 25).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r3 = studded.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 18).addBox(-3.0F, -1.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.7F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(CanaryPart entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.studded.zRot=ageInTicks/6.0f+entity.getPart();
		this.studs.xRot=headPitch/180.0f*3.14f;
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