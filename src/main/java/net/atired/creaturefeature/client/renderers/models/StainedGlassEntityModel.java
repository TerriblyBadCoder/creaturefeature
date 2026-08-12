package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.DetritusEntity;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.atired.creaturefeature.entity.StainedGlassEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.util.Mth;

public class StainedGlassEntityModel<T extends StainedGlassEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("stainedglassentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart core;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart r_wing;
	private final ModelPart l_wing;
	private final ModelPart root;

	public StainedGlassEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.core = this.body.getChild("core");
		this.tail = this.core.getChild("tail");
		this.head = this.core.getChild("head");
		this.r_wing = this.body.getChild("r_wing");
		this.l_wing = this.body.getChild("l_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-0.0002F, 20.2941F, 0.0556F));

		PartDefinition core = body.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(0.0007F, -1.6667F, -5.1667F, 0.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0004F, -1.6274F, 0.1111F));

		PartDefinition tail = core.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 14).addBox(-0.001F, -4.0F, 0.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0007F, 1.3333F, 3.8333F));

		PartDefinition head = core.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 0).addBox(0.0F, -5.0F, -7.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0003F, 0.3333F, -4.1667F));

		PartDefinition r_wing = body.addOrReplaceChild("r_wing", CubeListBuilder.create(), PartPose.offset(0.0002F, -0.2941F, -0.0556F));

		PartDefinition cube_r1 = r_wing.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, -4.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6581F));

		PartDefinition l_wing = body.addOrReplaceChild("l_wing", CubeListBuilder.create(), PartPose.offset(0.0002F, -0.2941F, -0.0556F));

		PartDefinition cube_r2 = l_wing.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(0.0F, 0.0F, -4.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.6581F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(StainedGlassEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			float returned = 1;
			float i= 0.0f;
			if(entity.getReturning()<0.5){
				returned=entity.getReturning()*2.0f;
			}
			float evilAge = ageInTicks-(ageInTicks%4);
			for(ModelPart part : this.root.getAllParts().toList()){
				i+=0.4f;
				if(part==this.root)continue;
				part.xRot=(returned*(i+evilAge/3.5f))%((float)Math.PI*2.0f);
				part.zRot=(returned*(i+evilAge/3.5f))%((float)Math.PI*2.0f);
				part.yRot=(returned*(i+evilAge/3.5f))%((float)Math.PI*2.0f);
			}
			for(ModelPart part : this.root.getAllParts().toList()){
				if(part.getInitialPose()==PartPose.ZERO)
					part.setInitialPose(part.storePose());
				PartPose pose= part.getInitialPose();
				if(part==this.body||part==this.root){
					continue;
				}
				part.setPos(pose.x+Mth.sin(i+evilAge/3.0f)*5.0f*returned,pose.y+Mth.cos(i+evilAge/3.0f/1.3f)*returned*5.0f,pose.z-Mth.sin(i+ageInTicks/3.0f/1.4f)*returned*5.0f);

				i+=0.5f;


			}
			ageInTicks = ageInTicks-(ageInTicks%2);
			this.r_wing.zRot=Mth.sin(ageInTicks/4.0f)/2.0f+3.14f/2.0f;
			this.l_wing.zRot=-Mth.sin(ageInTicks/4.0f)/2.0f-3.14f/2.0f;
			this.l_wing.xRot=-Mth.sin(ageInTicks/2)/4.0f;
			this.r_wing.xRot=Mth.sin(ageInTicks/2)/4.0f;
			this.tail.yRot=Mth.sin(ageInTicks/6.0f)/2.0f;
			this.body.zRot=Mth.sin(ageInTicks/12.0f)/2.0f;
			this.body.xRot=headPitch/180.0f*3.14f;
			this.head.yRot=Mth.cos(ageInTicks/6.0f)/2.0f;

	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}