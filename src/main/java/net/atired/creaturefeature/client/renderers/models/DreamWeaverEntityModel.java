package net.atired.creaturefeature.client.renderers.models;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.DreamWeaverEntity;
import net.atired.creaturefeature.entity.FendEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;

public class DreamWeaverEntityModel<T extends DreamWeaverEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("dreamweaverentitymodel"), "main");
	public static final ModelLayerLocation INNER_LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("dreamweaverentitymodel"), "inner");
    public static final ModelLayerLocation INNERER_LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("dreamweaverentitymodel"), "inner_other");

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart corebody;
	private final ModelPart sac;
	private final ModelPart l_leg_2;
	private final ModelPart l_leg_1;
	private final ModelPart l_leg_3;
	private final ModelPart l_leg_4;
	private final ModelPart r_leg_2;
	private final ModelPart r_leg_1;
	private final ModelPart r_leg_3;
	private final ModelPart r_leg_4;
	private final ModelPart root;

	public DreamWeaverEntityModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root=root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.corebody = this.body.getChild("corebody");
		this.sac = this.corebody.getChild("sac");
		this.l_leg_2 = this.body.getChild("l_leg_2");
		this.l_leg_1 = this.body.getChild("l_leg_1");
		this.l_leg_3 = this.body.getChild("l_leg_3");
		this.l_leg_4 = this.body.getChild("l_leg_4");
		this.r_leg_2 = this.body.getChild("r_leg_2");
		this.r_leg_1 = this.body.getChild("r_leg_1");
		this.r_leg_3 = this.body.getChild("r_leg_3");
		this.r_leg_4 = this.body.getChild("r_leg_4");
	}

	public static LayerDefinition createBodyLayer() {
		return createBodyLayer(0.0f);
	}
	public static LayerDefinition createBodyLayer(float grow) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(34, 27).addBox(-4.0F, -4.0F, -7.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(grow)), PartPose.offset(0.0F, 18.0F, -5.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 17.839F, -1.021F));

		PartDefinition corebody = body.addOrReplaceChild("corebody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.161F, -2.979F));

		PartDefinition cube_r1 = corebody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 11.0F, new CubeDeformation(grow)), PartPose.offsetAndRotation(0.0F, -1.9F, 4.0F, 0.5672F, 0.0F, 0.0F));

		PartDefinition sac = corebody.addOrReplaceChild("sac", CubeListBuilder.create(), PartPose.offset(0.0F, -3.3F, 6.3F));
        if(grow<-0.06){
            PartDefinition cube_r2 = sac.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -2.0F-grow*30.0f, 12.0F, 10.0F, 17.0F, new CubeDeformation(grow*99.0f*6.0f*0.2f,grow*99.0f,grow*99.0f*8.5f*0.2f)), PartPose.offsetAndRotation(0.0F, -1.474F, 1.6235F, 1.0908F, 0.0F, 0.0F));
        }else{
            PartDefinition cube_r2 = sac.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -2.0F-grow*60.0f, 12.0F, 10.0F, 17.0F, new CubeDeformation(grow*2.0f)), PartPose.offsetAndRotation(0.0F, -1.474F, 1.6235F, 1.0908F, 0.0F, 0.0F));
        }

		PartDefinition l_leg_2 = body.addOrReplaceChild("l_leg_2", CubeListBuilder.create(), PartPose.offset(2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r3 = l_leg_2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(34, 43).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.3054F, 0.4363F));

		PartDefinition l_leg_1 = body.addOrReplaceChild("l_leg_1", CubeListBuilder.create(), PartPose.offset(2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r4 = l_leg_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(34, 43).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1233F, 0.7383F, 0.5544F));

		PartDefinition l_leg_3 = body.addOrReplaceChild("l_leg_3", CubeListBuilder.create(), PartPose.offset(2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r5 = l_leg_3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(34, 43).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, -0.3054F, 0.4363F));

		PartDefinition l_leg_4 = body.addOrReplaceChild("l_leg_4", CubeListBuilder.create(), PartPose.offset(2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r6 = l_leg_4.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(34, 43).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1233F, -0.7383F, 0.5544F));

		PartDefinition r_leg_2 = body.addOrReplaceChild("r_leg_2", CubeListBuilder.create(), PartPose.offset(-2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r7 = r_leg_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(34, 43).mirror().addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, -0.3054F, -0.4363F));

		PartDefinition r_leg_1 = body.addOrReplaceChild("r_leg_1", CubeListBuilder.create(), PartPose.offset(-2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r8 = r_leg_1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(34, 43).mirror().addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1233F, -0.7383F, -0.5544F));

		PartDefinition r_leg_3 = body.addOrReplaceChild("r_leg_3", CubeListBuilder.create(), PartPose.offset(-2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r9 = r_leg_3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(34, 43).mirror().addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.3054F, -0.4363F));

		PartDefinition r_leg_4 = body.addOrReplaceChild("r_leg_4", CubeListBuilder.create(), PartPose.offset(-2.2713F, 0.3549F, 0.6224F));

		PartDefinition cube_r10 = r_leg_4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(34, 43).mirror().addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(grow)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1233F, 0.7383F, -0.5544F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
	public void setupSilk(DreamWeaverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
		@Override
	public void setupAnim(DreamWeaverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		ModelPart[] legs = new ModelPart[]{l_leg_1,l_leg_2,l_leg_3,l_leg_4,r_leg_1,r_leg_2,r_leg_3,r_leg_4};
		float count = 0;
		for(ModelPart i : legs){
			i.xRot=Mth.sin(limbSwing+count)*limbSwingAmount*0.6f;
			i.yRot=Mth.sin(limbSwing+count)*limbSwingAmount*0.2f;
			count+=1.0f;
		}
		float scaled = 1.0f;
		if(entity.getRupturing()>-0.6f){
			scaled*=entity.getRupturing()*(1.0f+Mth.sin(entity.getRupturing()*3.14f/1.4f)*1.5f);
		}
		this.sac.xScale=(1.17f+Mth.sin(ageInTicks/2.0f)/8.0f)*scaled;
		this.sac.zScale=(1.17f+Mth.sin(ageInTicks/2.0f)/8.0f)*scaled;
		this.sac.yScale=(1.17f+Mth.cos(ageInTicks/2.0f)/8.0f)*scaled;
		this.sac.yRot=Mth.sin((1.0f-scaled)*3.0f);
		this.corebody.zRot=Mth.sin(ageInTicks/6.0f)/10.0f;
		this.head.yRot=netHeadYaw/180.0f*3.14f+Mth.sin(ageInTicks/10.0f)/10.0f;
		this.head.zRot=Mth.cos(ageInTicks/10.0f)/10.0f;
		this.head.xRot=headPitch/180.0f*3.14f;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}