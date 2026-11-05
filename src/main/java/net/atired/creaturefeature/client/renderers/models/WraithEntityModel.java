package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.BeautyEntity;
import net.atired.creaturefeature.entity.WraithEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
public class WraithEntityModel<T extends WraithEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("wraithentitymodel"), "main");
	public static final ModelLayerLocation INNER_LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("wraithentitymodel"), "inner");

	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart head;
	private final ModelPart right_fin;
	private final ModelPart left_fin;
	private final ModelPart right_arm;
	private final ModelPart right_hand;
	private final ModelPart left_arm;
	private final ModelPart left_hand;
	private final ModelPart root;

	public WraithEntityModel(ModelPart root) {
		super(CFRenderTypes::entityWraithCutout);
		this.root=root;
		this.body = root.getChild("body");
		this.torso = this.body.getChild("torso");
		this.head = this.body.getChild("head");
		this.right_fin = this.head.getChild("right_fin");
		this.left_fin = this.head.getChild("left_fin");
		this.right_arm = this.body.getChild("right_arm");
		this.right_hand = this.right_arm.getChild("right_hand");
		this.left_arm = this.body.getChild("left_arm");
		this.left_hand = this.left_arm.getChild("left_hand");
	}

	public static LayerDefinition createBodyLayer(float scale) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.4F, 21.0F, 4.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, -4.0F));

		PartDefinition cube_r1 = torso.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 30).addBox(-4.0F, 0.0F, -2.0F, 7.0F, 16.0F, 4.0F, new CubeDeformation(scale)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -11.0F, -3.0F, 8.0F, 11.0F, 6.0F, new CubeDeformation(scale)), PartPose.offset(-0.4F, -13.0F, -4.0F));

		PartDefinition right_fin = head.addOrReplaceChild("right_fin", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.0F, 0.0F));

		PartDefinition cube_r2 = right_fin.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(5, 54).addBox(-6.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(scale)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition left_fin = head.addOrReplaceChild("left_fin", CubeListBuilder.create(), PartPose.offset(4.0F, -5.0F, 0.0F));

		PartDefinition cube_r3 = left_fin.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(5, 54).mirror().addBox(0.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(scale)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.48F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 16).addBox(-3.85F, -1.9F, -9.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(scale)), PartPose.offset(-3.95F, -12.1F, -3.0F));

		PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -9.0F));

		PartDefinition cube_r4 = right_hand.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, 36).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(scale)), PartPose.offsetAndRotation(-2.0669F, 3.9578F, 0.216F, 1.6144F, -0.0019F, 0.0436F));

		PartDefinition cube_r5 = right_hand.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-3.75F, -2.0F, -11.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(scale)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.6144F, -0.0019F, 0.0436F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 16).mirror().addBox(-0.15F, -1.9F, -9.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(scale)).mirror(false), PartPose.offset(3.15F, -12.1F, -3.0F));

		PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -9.0F));

		PartDefinition cube_r6 = left_hand.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(1, 36).mirror().addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(scale)).mirror(false), PartPose.offsetAndRotation(2.0669F, 3.9578F, 0.216F, 1.6144F, 0.0019F, -0.0436F));

		PartDefinition cube_r7 = left_hand.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.25F, -2.0F, -11.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(scale)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.6144F, 0.0019F, -0.0436F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(WraithEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.1f;
		this.body.y=14+Mth.sin(ageInTicks/4.0f);
		this.body.yRot=Mth.sin(ageInTicks/12.0f)/9.0f;
		this.left_arm.yRot=Mth.sin(ageInTicks/12.0f)/4.0f-0.4f-Mth.sin(entity.getSwing()*3.14f)*2.0f;
		this.right_arm.yRot=-Mth.sin(ageInTicks/12.0f)/4.0f+0.4f+Mth.sin(entity.getSwing()*3.14f)*2.0f;
		this.left_arm.xRot=Mth.sin(ageInTicks/7.0f)/7.0f+0.4f;
		this.right_arm.xRot=Mth.sin(ageInTicks/7.0f)/7.0f+0.4f;
		this.left_hand.xRot=-Mth.sin(ageInTicks/7.0f)/15.0f-0.4f+Mth.sin(entity.getSwing()*3.14f)/2.0f;
		this.right_hand.xRot=-Mth.sin(ageInTicks/7.0f)/15.0f-0.4f+Mth.sin(entity.getSwing()*3.14f)/2.0f;
		this.left_hand.zRot=-Mth.sin(ageInTicks/7.0f)/5.0f;
		this.right_hand.zRot=Mth.sin(ageInTicks/7.0f)/5.0f;
		this.body.zRot=Mth.cos(ageInTicks/12.0f)/12.0f;
		this.head.xRot=headPitch/180.0f*3.14f/1.6f-sinused-limbSwingAmount/2.0f-Mth.sin(entity.getSwing()*3.14f)*2.0f;
		this.body.xRot=-sinused/3.0f+limbSwingAmount/2.0f+Mth.sin(entity.getSwing()*3.14f);
		this.torso.xRot=-this.body.xRot;
		this.head.yRot=netHeadYaw/180.0f*3.14f/1.5f+Mth.sin(ageInTicks/12.0f)/12.0f+Mth.sin(entity.getSwing()*3.14f*2.0f)*1.0f;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}