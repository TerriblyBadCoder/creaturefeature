package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.FishEyeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class FishEyeEntityModel<T extends FishEyeEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fisheyeentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart dorsal;
	private final ModelPart front;
	private final ModelPart eye;
	private final ModelPart root;

	public FishEyeEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.tail = this.body.getChild("tail");
		this.dorsal = this.tail.getChild("dorsal");
		this.front = this.body.getChild("front");
		this.eye = this.front.getChild("eye");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, -1.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(16, 24).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 3.0F));

		PartDefinition dorsal = tail.addOrReplaceChild("dorsal", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -4.0F, -1.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 2.0F));

		PartDefinition front = body.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -5.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.4F, 0.0F, 1.0F));

		PartDefinition eye = front.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(16, 14).addBox(-3.0F, -3.0F, -4.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.9F, 0.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}



	@Override
	public void setupAnim(FishEyeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		headPitch*=1.5f;
		this.body.xRot=headPitch/180.0f*3.14f/1.2f;
		this.eye.xRot=headPitch/180.0f*3.14f/1.2f;
		this.tail.xRot=-headPitch/180.0f*3.14f/3.2f;
		this.dorsal.xRot=-headPitch/180.0f*3.14f/3.2f;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}