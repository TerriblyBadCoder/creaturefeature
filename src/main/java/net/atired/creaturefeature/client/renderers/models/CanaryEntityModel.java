package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CanaryEntity;
import net.atired.creaturefeature.entity.MinedFlayerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class CanaryEntityModel<T extends CanaryEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("canaryentitymodel"), "main");
	private final ModelPart head;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart bone6;
	private final ModelPart bone7;
	private final ModelPart bone8;
	private final ModelPart root;
	private ModelPart[] demBones;
	private ModelPart[] demBones2;

	public CanaryEntityModel(ModelPart root) {
		this.root=root;
		this.head = root.getChild("head");
		this.bone = this.head.getChild("bone");
		this.bone2 = this.head.getChild("bone2");
		this.bone3 = this.head.getChild("bone3");
		this.bone4 = this.head.getChild("bone4");
		this.bone5 = this.head.getChild("bone5");
		this.bone6 = this.head.getChild("bone6");
		this.bone7 = this.head.getChild("bone7");
		this.bone8 = this.head.getChild("bone8");
		this.demBones= new ModelPart[]{ bone2,bone7, bone4 , bone8};
		this.demBones2= new ModelPart[]{ bone6, bone3, bone5, bone};
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.1333F, -2.2269F, 16.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.1333F, -0.7731F));

		PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(7.0F, -4.1333F, -1.4269F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -3.0F, -0.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

		PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(-4.0F, -7.1333F, -1.4269F));

		PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -10.0F, -0.5F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition bone3 = head.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(-7.0F, -4.1333F, -1.4269F));

		PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-10.0F, -3.0F, -0.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition bone4 = head.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(-4.0F, 7.4667F, -1.4269F));

		PartDefinition cube_r4 = bone4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, 0.0F, -0.5F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.8727F, 0.0F, 0.0F));

		PartDefinition bone5 = head.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(7.0F, 3.8667F, -1.4269F));

		PartDefinition cube_r5 = bone5.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -3.0F, -0.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

		PartDefinition bone6 = head.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(-7.0F, 3.8667F, -1.4269F));

		PartDefinition cube_r6 = bone6.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-10.0F, -3.0F, -0.5F, 10.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition bone7 = head.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(4.0F, -7.1333F, -1.4269F));

		PartDefinition cube_r7 = bone7.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -10.0F, -0.5F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition bone8 = head.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(4.0F, 7.4667F, -1.4269F));

		PartDefinition cube_r8 = bone8.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, 0.0F, -0.5F, 6.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.8727F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(CanaryEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.zRot=Mth.cos(ageInTicks/5.0f)/10.0f;
		this.root.y=7.0f;
		int count =0;
		for(ModelPart part : this.demBones){
			part.xRot=Mth.sin(ageInTicks/4.0f+count*3.14f)/4.0f;
			if(count>=2){
				part.xRot+=0.4f;
			}else{
				part.xRot-=0.4f;
			}
			count+=1;
		}
		count=0;
		for(ModelPart part : this.demBones2){
			part.yRot=Mth.sin(ageInTicks/4.0f+count*3.14f)/4.0f;
			if(count<2){
				part.yRot+=0.4f;
			}else{
				part.yRot-=0.4f;
			}
			count+=1;
		}
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