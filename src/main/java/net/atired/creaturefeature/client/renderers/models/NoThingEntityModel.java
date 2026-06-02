package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.NoThingEntity;
import net.atired.creaturefeature.entity.SinisterEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
public class NoThingEntityModel<T extends NoThingEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("nothingentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_arm;
	private final ModelPart left_leg;
	private final ModelPart right_arm;
	private final ModelPart body;
	private final ModelPart head;
	private final  ModelPart root;

	public NoThingEntityModel(ModelPart root) {
		super(CFRenderTypes::entityAmbushCutout);
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(28, 14).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.4F, 7.5F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(28, 14).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.4F, 7.5F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -10.5F, -2.0F, 6.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -10.1F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(20, 14).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -8.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(20, 14).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, -8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(NoThingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount;
		this.left_arm.zRot=-Mth.sin(ageInTicks/8.0f)/8.0f-0.1f;
		this.right_arm.zRot=Mth.sin(ageInTicks/8.0f)/8.0f+0.1f;
		float lunged = Mth.sin(entity.getLunge()*3.14f*2.0f-3.14f*2.0f);
		this.head.zRot=Mth.sin(ageInTicks/6.0f)/10.0f;
		this.head.yRot=Mth.cos(ageInTicks/9.0f)/5.0f;

		this.body.zRot=Mth.sin(ageInTicks/12.0f)/10.0f+Mth.sin(limbSwing*2.0f)*limbSwingAmount/12.0f;
		this.body.yRot=Mth.cos(ageInTicks/15.0f)/5.0f;
		this.body.xRot=-limbSwingAmount/2.0f+Mth.cos(limbSwing*2.0f)*limbSwingAmount/12.0f+lunged;
		this.left_arm.xRot=Mth.cos(limbSwing)*limbSwingAmount*0.4f-lunged*3.14f;
		this.right_arm.xRot=-Mth.cos(limbSwing)*limbSwingAmount*0.4f-lunged*3.14f;
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.6f;
		this.left_arm.xRot+=sinused;
		this.right_arm.xRot+=sinused;
		this.right_leg.xRot-=sinused;
		this.head.xRot=headPitch/180.0f*3.14f;
	}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
        super.renderToBuffer(poseStack, vertexConsumer, i, i1, i2);
    }

	@Override
	public ModelPart root() {
		return this.root;
	}
}