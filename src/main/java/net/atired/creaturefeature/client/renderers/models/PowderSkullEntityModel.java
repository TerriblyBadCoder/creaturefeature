package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.FishEyeEntity;
import net.atired.creaturefeature.entity.PowderSkullEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;


public class PowderSkullEntityModel<T extends PowderSkullEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("powderskullentitymodel"), "main");
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart skull;
	private final ModelPart root;

	public PowderSkullEntityModel(ModelPart root) {
		this.root=root;
		this.head = root.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.skull = this.head.getChild("skull");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, 18.4F, 0.4F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(44, 24).addBox(-5.0F, 0.0F, -10.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-5.0F, 1.95F, -10.05F, 10.0F, 4.0F, 10.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.5F, -2.4F, 4.6F, 0.5672F, 0.0F, 0.0F));

		PartDefinition skull = head.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -10.95F, -10.95F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-5.5F, -10.55F, -10.55F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.45F, 4.55F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(PowderSkullEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot=headPitch/180.0f*3.14f;
		this.skull.yRot=netHeadYaw/180.0f*3.14f+Mth.sin(ageInTicks/8.0f)/12.0f;
		this.jaw.yRot=-Mth.sin(ageInTicks/8.0f)/12.0f;
		this.jaw.zRot=-Mth.cos(ageInTicks/8.0f)/12.0f;
		this.skull.zRot=Mth.cos(ageInTicks/8.0f)/12.0f;
	}

	@Override
	public ModelPart root() {
		return root;
	}
}
