package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.HenchEntity;
import net.atired.creaturefeature.entity.TrichaelEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.util.stream.Stream;


public class HenchEntityModel<T extends HenchEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("henchentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart root;

	public HenchEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		return createBodyLayer(0);
	}
	public static LayerDefinition createBodyLayer(float inf) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)), PartPose.offset(-1.5F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)).mirror(false), PartPose.offset(1.5F, 12.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 29).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 12.0F, 4.0F, new CubeDeformation(inf)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)).mirror(false), PartPose.offset(3.0F, -11.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(8, 43).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(inf)), PartPose.offset(-3.0F, -11.0F, 0.0F));


		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(HenchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		limbSwing-=limbSwing%(3.14f/4.0f);
		limbSwingAmount*=0.75f;
		float mulAgeInTicks=Math.max(0,ageInTicks-2);
		mulAgeInTicks-=mulAgeInTicks%1.5f;
		float scaled = Mth.sin(Math.min(1,mulAgeInTicks/10.0f)*3.14f)*1.5f*(1.0f-Math.min(1,mulAgeInTicks/10.0f))+Math.min(1,mulAgeInTicks/10.0f);
		this.root.yScale=scaled;
		this.body.yScale=scaled;
		this.root.y=(1.0f-scaled)*24.0f;
		ageInTicks+=entity.getId()*4;
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*1.8f-Mth.cos(ageInTicks/7.0f)/7.0f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*1.8f+Mth.cos(ageInTicks/7.0f)/7.0f;
		this.left_leg.yRot=-Mth.sin(ageInTicks/8.0f)/3.0f;
		this.right_leg.yRot=Mth.sin(ageInTicks/8.0f)/3.0f;
		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*0.4f;
		this.left_arm.xRot=Mth.cos(limbSwing)*limbSwingAmount*0.6f-sinused-Mth.cos(ageInTicks/7.0f)/2.0f;
		this.right_arm.xRot=-Mth.cos(limbSwing)*limbSwingAmount*0.6f-sinused+Mth.cos(ageInTicks/7.0f)/2.0f;
		this.left_arm.zRot=Mth.sin(ageInTicks/12.0f)/4.0f-0.1f;
		this.right_arm.zRot=-Mth.sin(ageInTicks/12.0f)/4.0f+0.1f;
		this.body.zRot=Mth.sin(ageInTicks/6.0f)/4.0f;
		this.body.xRot=Mth.cos(ageInTicks/6.0f)/4.0f;
		this.body.yRot=Mth.cos(ageInTicks/8.0f)/2.0f;
		ModelPart[] parts =  this.root.getAllParts().toArray(ModelPart[]::new);
		for(ModelPart part : parts){
			part.xRot-=((part.xRot*16.0f)%1)/16.0f;
			part.yRot-=((part.yRot*16.0f)%1)/16.0f;
			part.zRot-=((part.zRot*16.0f)%1)/16.0f;
		}
	}


	@Override
	public ModelPart root() {
		return root;
	}
}