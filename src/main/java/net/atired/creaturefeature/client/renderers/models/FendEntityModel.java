package net.atired.creaturefeature.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.FendEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.armortrim.ArmorTrim;

public class FendEntityModel extends HumanoidModel<FendEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fendsentitymodel"), "main");
    public static final ModelLayerLocation LAYER_ARMOUR_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fendsentitymodel"), "outer_armor");
    public static final ModelLayerLocation LAYER_INNER_ARMOUR_LOCATION = new ModelLayerLocation(CreatureFeature.getId("fendsentitymodel"), "inner_armor");

    public FendEntityModel(ModelPart root) {
        super(root, CFRenderTypes::entityFendCutout);
    }

    @Override
    public void setupAnim(FendEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ageInTicks+=entity.getId()*10;
        float i = entity.attackAnim;
        float sinused = Mth.sin(i*3.14f)*1.6f;
        leftLeg.xRot*=0.1f;
        leftLeg.xRot+= Mth.cos(ageInTicks/3.0f)/3.0f+limbSwingAmount;
        rightLeg.xRot*=0.1f;
        rightLeg.xRot-= Mth.cos(ageInTicks/3.0f)/3.0f-limbSwingAmount;
        leftLeg.zRot+= Mth.cos(ageInTicks/3.0f)/4.0f;
        rightLeg.zRot-= Mth.cos(ageInTicks/3.0f)/4.0f;

        boolean shouldMeleeSword = entity.getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof SwordItem;
        boolean shouldRanged = entity.getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof BowItem;
        head.z=0.0f;
        if(!shouldRanged)leftArm.xRot*=0.2f;
        if(!shouldRanged)rightArm.xRot*=0.2f;
        if(shouldMeleeSword){
            sinused+=1.7f;leftArm.xRot*=0.2f;rightArm.xRot*=0.2f;
            if(true){
                leftArm.yRot+=0.7f+entity.getLunge();
                rightArm.yRot-=0.7f+entity.getLunge();
            }
        }
        else{
            leftArm.xRot+= Mth.cos(ageInTicks/3.0f)/9.0f;
            rightArm.xRot-= Mth.cos(ageInTicks/3.0f)/9.0f;
        }
        leftArm.xRot+=-limbSwingAmount-sinused;
        rightArm.xRot-=-limbSwingAmount+sinused;
        if(!shouldRanged&&!shouldMeleeSword){
            head.z=-4.0f;
            head.y+=4.0f;
        }
        this.body.z=0f;
        if(shouldRanged){
            leftArm.yRot=3.14f/4.0f;
            this.body.xRot-=0.3f;
            this.body.z=4f;
            this.body.y+=1f;
            this.leftArm.z+=3f;
            this.rightArm.z+=3f;
            this.head.z=3.0f;
            this.head.y+=1.0f;
        }
        leftArm.zRot+= Mth.sin(ageInTicks/3.0f)/10.0f;
        rightArm.zRot-= Mth.sin(ageInTicks/3.0f)/10.0f;

        for (ModelPart part : bodyParts()) {
            part.y+=Mth.sin(ageInTicks/7.0f)*4.0f;
        }
        head.y+=Mth.sin(ageInTicks/7.0f)*4.0f;
        head.yRot+=entity.getLunge()*3.14f*4.0f;
    }
    public void prepareMobModel(FendEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack itemstack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemstack.is(Items.BOW) && entity.isAggressive()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }

        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation cubeDeformation = new CubeDeformation(0);
        float yOffset=0.0f;
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation.extend(0.5F)), PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(-5.0F, 2.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(5.0F, 2.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(-1.9F, 12.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(1.9F, 12.0F + yOffset, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
