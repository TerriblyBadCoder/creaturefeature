// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BeautyEntity;
import net.atired.creaturefeature.entity.PathogenesisEntity;
import net.atired.creaturefeature.entity.SaintSolisEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SaintSolisEntityModel<T extends SaintSolisEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("pathogenentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart l_front_leg;
	private final ModelPart l_back_leg;
	private final ModelPart body_back;
	private final ModelPart r_back_leg;
	private final ModelPart r_front_leg;
	private final ModelPart root;
	public SaintSolisEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.l_front_leg = this.body.getChild("l_front_leg");
		this.l_back_leg = this.body.getChild("l_back_leg");
		this.body_back = root.getChild("body_back");
		this.r_back_leg = this.body_back.getChild("r_back_leg");
		this.r_front_leg = this.body_back.getChild("r_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -6.1F, 0.0F, 14.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.1F, -9.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 41).addBox(-8.0F, -7.0F, -3.0F, 16.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 9.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(42, 41).addBox(-5.0F, -5.0F, -4.6903F, 10.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(48, 0).addBox(-2.5F, -2.0F, -7.6903F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.9F, 0.6903F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(46, 36).mirror().addBox(-1.0F, -2.5F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.5F, -4.5F, -1.6903F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(46, 36).addBox(-3.0F, -2.5F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -4.5F, -1.6903F, 0.0F, 0.1309F, 0.0F));

		PartDefinition l_front_leg = body.addOrReplaceChild("l_front_leg", CubeListBuilder.create().texOffs(46, 21).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.9F, 4.5F));

		PartDefinition l_back_leg = body.addOrReplaceChild("l_back_leg", CubeListBuilder.create().texOffs(46, 21).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 4.9F, 27.5F));

		PartDefinition body_back = partdefinition.addOrReplaceChild("body_back", CubeListBuilder.create(), PartPose.offset(0.0F, 12.1F, 19.0F));

		PartDefinition cube_r4 = body_back.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 21).addBox(-8.0F, -7.0F, -3.0F, 16.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition r_back_leg = body_back.addOrReplaceChild("r_back_leg", CubeListBuilder.create().texOffs(46, 21).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 5.9F, -0.5F));

		PartDefinition r_front_leg = body_back.addOrReplaceChild("r_front_leg", CubeListBuilder.create().texOffs(46, 21).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 5.9F, -23.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}