package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.DetritusEntity;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.util.Mth;


public class DetritusEntityModel<T extends DetritusEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("detritusentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart head;
	private final ModelPart root;

	public DetritusEntityModel(ModelPart root) {
		super(CFRenderTypes::entityMonochromeCull);
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
		this.head = this.body.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 11.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 11.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.5F, 11.0F, 0.6F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -15.0F, -2.0F, 7.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, -0.6F, 0.1745F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 18).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -14.0F, -3.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -14.0F, -3.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.3F))
		.texOffs(0, 0).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -14.0F, -3.6F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(DetritusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_arm.zRot=-Mth.sin(ageInTicks/8.0f)/24.0f;
		this.right_arm.zRot=Mth.sin(ageInTicks/8.0f)/24.0f;
		float copied = entity.getCopy()*(1.0f-Math.min(1.0f,entity.getUnburying()*4.0f));
		this.root.xScale=1.0f+Mth.sin(entity.getId()+2)*copied/3.0f;
		float yScaled = Mth.cos(entity.getId()+3)*copied/3.0f;
		this.root.yScale=1.0f+yScaled;
		this.root.zScale=1.0f+Mth.sin(entity.getId()+4.2f)*copied/3.0f;

		float unburied=entity.getUnburying();
		this.root.y=0.0f-yScaled*20.0f+(float)Math.pow(unburied,0.5f)*32.0f;
		this.left_arm.zRot-=unburied*3.1f;
		this.left_arm.y=-14.0f+unburied*14.0f;
		this.right_arm.y=this.left_arm.y;
		this.right_arm.zRot+=unburied*3.1f;


		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*0.8f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*0.8f;

		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.yRot=netHeadYaw/180.0f*3.14f;
		float copyed = copied/4.0f;
		if(entity.getId()%2==0)copyed*=-1.0f;
		this.body.zRot=copyed;
		this.left_arm.zRot+=-copyed/1.5f;
		this.right_arm.zRot+=-copyed/1.5f;
		this.head.zRot=-copyed;
		this.body.xRot=Mth.sin(ageInTicks/16.0f)/6.0f;
		this.head.xRot+=Mth.sin(ageInTicks/16.0f)/6.0f-copyed;

		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.1f;
		this.left_arm.xRot=Mth.cos(limbSwing)*limbSwingAmount*0.4f-sinused;
		this.right_arm.xRot=-Mth.cos(limbSwing)*limbSwingAmount*0.4f-sinused;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}