package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.FiendEntity;
import net.atired.creaturefeature.entity.SinisterEntity;
import net.atired.creaturefeature.entity.VertigoEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.util.Objects;


public class FiendEntityModel<T extends FiendEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fiendentitymodel"), "main");
	public static final ModelLayerLocation INNER_LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fiendentitymodel"), "inner");
	private final ModelPart root;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart head;
	private final ModelPart horns;

	public FiendEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		this.head = this.body.getChild("head");
		this.horns = this.head.getChild("horns");
	}

	public static LayerDefinition createBodyLayer(float grow) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(26, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(4.0F*grow, 14.0F*grow, 4.0F*grow)), PartPose.offset(-2.0F, 11.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(26, 16).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(4.0F*grow, 14.0F*grow, 4.0F*grow)).mirror(false), PartPose.offset(2.0F, 11.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.5F, -14.0F, -2.0F, 9.0F, 14.0F, 4.0F, new CubeDeformation(9.0F*grow, 13.0F*grow, 4.0F*grow)), PartPose.offset(-0.1F, 10.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 34).addBox(-2.9F, -1.0F, -2.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(3.0F*grow, 13.0F*grow, 4.0F*grow)), PartPose.offset(-4.6F, -12.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 34).mirror().addBox(-0.1F, -1.0F, -2.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(3.0F*grow, 13.0F*grow, 4.0F*grow)).mirror(false), PartPose.offset(4.6F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(12.0F*grow, 4.0F*grow, 12.0F*grow)), PartPose.offset(0.1F, -15.0F, 0.0F));

		PartDefinition horns = head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(0, 34).addBox(-10.0F, -11.5F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(4.0F*grow, 13.0F*grow, 4.0F*grow))
				.texOffs(0, 34).mirror().addBox(6.0F, -11.5F, -2.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(4.0F*grow, 13.0F*grow, 4.0F*grow)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(FiendEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		float i = entity.attackAnim;
		float sinused = Mth.sin(i*3.14f)*1.5f;
		float cosused = Mth.cos(i*3.14f*2.0f)*Mth.cos(entity.getKick()*3.14f);

		limbSwingAmount*=cosused;
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount*1.2f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount*1.2f-Mth.sin(entity.getKick()*3.14f)*2.0f;
		this.left_arm.xRot=-Mth.sin(limbSwing)*limbSwingAmount/1.4f-sinused+0.02f;
		this.right_arm.xRot=Mth.sin(limbSwing)*limbSwingAmount/1.4f-sinused+0.02f;
		this.head.yRot=netHeadYaw/180.0f*3.14f+Mth.sin(ageInTicks/5.0f)/24.0f;
		this.left_arm.zRot=Mth.cos(ageInTicks/3.0f)/12.0f+sinused/7.0f;
		this.right_arm.zRot=-Mth.cos(ageInTicks/3.0f)/12.0f-sinused/7.0f;
		this.body.zRot=Mth.cos(ageInTicks/6.0f)/32.0f;
		if(entity.getFirstPassenger()!=null){
			limbSwingAmount*=0.4f;
		}
		this.head.xRot=headPitch/180.0f*3.14f+Mth.cos(ageInTicks/5.0f)/24.0f-limbSwingAmount/2.0f;
		this.body.xRot=limbSwingAmount/2.0f+Mth.sin(ageInTicks/6.0f)/32.0f;
		this.left_arm.yRot=0;
		this.right_arm.yRot=0;
		if(entity.getCrit()>0){
			float critted = Math.min(1f,(entity.getCrit()-0.1f)*10f);
			this.body.xRot-=critted*0.5f;
			this.head.xRot+=critted*0.5f;
			this.left_arm.xRot*=0.1f;
			this.right_arm.xRot*=0.1f;
			this.left_arm.xRot-=critted*1f;
			this.right_arm.xRot-=critted*1f;
			this.left_arm.yRot=critted*0.6f;
			this.right_arm.yRot=-critted*0.6f;
		}
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}