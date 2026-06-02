package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.MindsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.FastColor;

public class MindsEntityModel<T extends MindsEntity> extends AbstractZombieModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("mindsentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart mind;
	private final ModelPart hat;
	private final ModelPart numberstorage;

	public MindsEntityModel(ModelPart root) {
		super(root);
		this.right_leg = root.getChild("right_leg");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.mind = this.head.getChild("mind");
		this.hat = root.getChild("hat");
		this.numberstorage = root.getChild("numberstorage");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(41, 16).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(41, 16).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 36).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, 0.0436F, 0.0F, -0.0436F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition mind = head.addOrReplaceChild("mind", CubeListBuilder.create().texOffs(1, 50).addBox(-3.5F, -1.3F, -2.2F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(31, 52).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.7F, -2.3F));

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition numberstorage = partdefinition.addOrReplaceChild("numberstorage", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		headPitch/=4.0f;
		super.setupAnim(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
		float mult = (float)Math.pow(entity.getSpeedUp()*0.9f,0.5f)+0.2f;
		float mult2=(float)Math.pow(entity.getSpeedUp(),0.5f);
		this.head.yRot=(float)(Math.sin(ageInTicks/1.0f)/20.0f*mult2);
		this.head.xRot+=-mult2/2.5f;
		float minmult = Math.min(mult2*7.0f,1.0f)*0.6f;
		this.left_arm.yRot+=minmult;
		this.right_arm.yRot-=minmult;
		this.right_arm.xRot-=minmult/2.0f;
		mult-=0.2f;
		mult*=1.5f;
		mult+=0.2f;
		this.mind.xScale= (float) (Math.sin(ageInTicks/1.5f)/23.0f*mult+0.95f);
		this.mind.yScale= (float) (Math.cos(ageInTicks/1.0f)/8.0f*mult+1.0f);
		this.mind.zScale= (float) (Math.cos(ageInTicks/2.0f)/23.0f*mult+0.95f);
		if(entity.hasNoMind()){
			this.mind.xScale*=0.0f;
			this.mind.yScale*=0.0f;
			this.mind.zScale*=0.0f;
		}
		this.mind.yRot= (float) (Math.sin(ageInTicks/1.0f+0.7f)/50.0f)*mult;
		this.numberstorage.xRot=Math.min((float)Math.pow(entity.getSpeedUp(),0.33f)*3.0f,1.0f)*0.3f;
	}

	@Override
	public boolean isAggressive(T entity) {
		return entity.isAggressive()||entity.getSpeedUp()>0.0001f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		green=(int)(green*(1.0f-this.numberstorage.xRot));
		color = FastColor.ARGB32.color(255,red,green,blue);
		super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
	}
}