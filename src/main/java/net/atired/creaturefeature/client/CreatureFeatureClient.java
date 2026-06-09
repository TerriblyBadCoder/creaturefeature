package net.atired.creaturefeature.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.accessors.GameRendererResourceManagerAccessor;
import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.accessors.PoseStackAccessor;
import net.atired.creaturefeature.client.particles.*;
import net.atired.creaturefeature.entity.CanaryPart;
import net.atired.creaturefeature.init.*;
import net.atired.creaturefeature.client.renderers.*;
import net.atired.creaturefeature.client.renderers.models.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.joml.Quaternionf;

import java.io.IOException;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = CreatureFeature.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = CreatureFeature.MODID, value = Dist.CLIENT)
public class CreatureFeatureClient {
    public CreatureFeatureClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }


    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(()->{
            ItemProperties.register(CFItemInit.VERTIGO_HORN.get(), ResourceLocation.withDefaultNamespace("tooting"), (p_340947_, p_340948_, p_340949_, p_340950_) -> {
                if(p_340949_!=null&&p_340949_.isUsingItem()){
                    return 1;
                }
                return 0;
        });
        });
    }
    public static ResourceLocation EVIL_ASS_MINEDFLAYEREFFECT = CreatureFeature.getId("shaders/post/minedflayer.json");
    public static ResourceLocation SLEEPYEFFECT = CreatureFeature.getId("shaders/post/sleep.json");
    public static ResourceLocation RABIESEFFECT = CreatureFeature.getId("shaders/post/rabies.json");
    public static PostChain MINEDFLAYER = null;
    public static PostChain SLEEP = null;
    public static PostChain RABIES = null;
    public static RenderTarget RABIES_TARGET = null;
    public static CFClientProxy PROXY = new CFClientProxy();
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        if(Minecraft.getInstance().gameRenderer instanceof GameRendererResourceManagerAccessor accessor){
            SIZED= new int[]{Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight()};
            RABIES= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.creaturefeature$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), RABIESEFFECT);
            RABIES.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            RABIES.addTempTarget("rabiestargeteheheh",Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            RABIES_TARGET=RABIES.getTempTarget("rabiestargeteheheh");
            SLEEP= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.creaturefeature$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), SLEEPYEFFECT);
            SLEEP.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            MINEDFLAYER= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.creaturefeature$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), EVIL_ASS_MINEDFLAYEREFFECT);
            MINEDFLAYER.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
        }

    }
    private static final ResourceLocation GUNK_LOCATION = CreatureFeature.getId("textures/entity/minedflayer_gunk.png");
    private static final ResourceLocation BLITZ_TRAIL_LOCATION = CreatureFeature.getId("textures/entity/blitz_trail.png");

    @SubscribeEvent
    static void renderModelEvent(RenderLivingEvent.Pre event) {
        if(!event.getEntity().isDeadOrDying()&&event.getEntity() instanceof LivingEntityGoopAccessor accessor&&accessor.isFallDamageAmped()){

            if(SHOULD[0]){
                event.getPoseStack().popPose();
            }
            Vec3 dir = event.getEntity().getDeltaMovement().multiply(1,1,1).normalize();
            float yaw =(float) Math.atan2(dir.x,dir.z);
            float pitch =(float) Math.asin(dir.y);
            VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(BLITZ_TRAIL_LOCATION));
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            event.getPoseStack().translate(0,event.getEntity().getBbHeight()/2.0f,0);
            PoseStack.Pose pose = poseStack.last();
            Vec3 trDir = new Vec3(0.3,0,0).yRot(yaw);
            dir = event.getEntity().getDeltaMovement().scale(-3);

            int i = event.getPackedLight();
            Vec3 yDir = new Vec3(0,0.3,0).xRot(pitch).yRot(yaw);
            Vec3 trDir1=trDir.add(yDir);
            Vec3 trDir2=trDir.subtract(yDir);

            vertex(pose,consumer, trDir1.x, trDir1.y,trDir1.z,1,0,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir2.x, trDir2.y,trDir2.z,1,1,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir2.x+dir.x, trDir2.y+dir.y,trDir2.z+dir.z,0,1,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir1.x+dir.x, trDir1.y+dir.y,trDir1.z+dir.z,0,0,0,-1,0,i,1.0f);
            trDir=trDir.scale(-1);
             trDir1=trDir.add(yDir);
             trDir2=trDir.subtract(yDir);
            vertex(pose,consumer, trDir1.x, trDir1.y,trDir1.z,1,0,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir2.x, trDir2.y,trDir2.z,1,1,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir2.x+dir.x, trDir2.y+dir.y,trDir2.z+dir.z,0,1,0,-1,0,i,1.0f);
            vertex(pose,consumer, trDir1.x+dir.x, trDir1.y+dir.y,trDir1.z+dir.z,0,0,0,-1,0,i,1.0f);


            poseStack.popPose();

            event.getPoseStack().pushPose();
            float rot = (event.getEntity().tickCount+event.getPartialTick())/2.0f;
            event.getPoseStack().translate(0,event.getEntity().getBbHeight()/2.0f,0);
            event.getPoseStack().mulPose(new Quaternionf().rotationXYZ(0,yaw+3.14f/2.0f,rot));
            event.getPoseStack().translate(0,-event.getEntity().getBbHeight()/2.0f,0);

            SHOULD[0]=true;
        }
    }
    private static boolean[] SHOULD = {false};
    @SubscribeEvent
    static void postRenderEntity(RenderLivingEvent.Post event) {
        if(SHOULD[0]&&event.getEntity() instanceof LivingEntityGoopAccessor accessor&&accessor.isFallDamageAmped()&&!event.getEntity().isDeadOrDying()){
            System.out.println("IF THIS CRASHES, WE ARE SO FUCKED");

            SHOULD[0]=false;
            event.getPoseStack().popPose();


        }
        if(event.getEntity().level()!=null){
            LivingEntity living = event.getEntity();

            if(living instanceof LivingEntityGoopAccessor accessor&&accessor.getGoop()>0.1f){
                float modA=accessor.getGoop();
                if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
                    CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
                float yOff = 0.0f;
                float pie = (float)Math.PI;
                if(!living.onGround()){
                    Vec3 vec3 = living.getPosition(1);
                    Vec3 vec31 = vec3.add(new Vec3(0,-5,0));
                    Level level = living.level();
                    BlockHitResult result = level.clip(new ClipContext(vec3, vec31, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, living));
                    yOff=-(float)result.getLocation().distanceTo(living.getPosition(1));
                }

                VertexConsumer consumer = event.getMultiBufferSource().getBuffer(CFRenderTypes.entityAmbushCutout(GUNK_LOCATION));
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();
                poseStack.translate(0,yOff,0);
                PoseStack.Pose pose = poseStack.last();
                Vec3 norDir = living.getDeltaMovement().multiply(1,0,1).scale(3);
                float xScale = (float) (Math.abs(norDir.x));
                float zScale = (float) (Math.abs(norDir.z));

                float sinAge = (living.tickCount+event.getPartialTick())/7.0f;

                norDir=norDir.scale(0.4);

                float norLength = Math.clamp((float)norDir.length()*4.0f,0.0f,1.0f);

                Vec3 oldDir = new Vec3(1.01,0,0).scale(living.getBbWidth()).yRot(pie/4.0f+sinAge/4.0f)
                        .multiply(1.0f+xScale,1,1.0f+zScale).add(norDir.x,0,norDir.z);
                float oldDot =Mth.lerp(norLength,1.0f,(float)oldDir.normalize().dot(norDir.normalize())/2.0f+0.6f);

                for (int i = 0; i < 8; i++) {
                    Vec3 dir2 = new Vec3(1.01,0,0).scale(living.getBbWidth()).yRot(sinAge/4.0f+(i+1)/4.0f*pie+pie/4.0f)
                            .multiply(1.0f+xScale,1,1.0f+zScale).add(norDir.x,0,norDir.z);
                    float dot2 = Mth.lerp(norLength,1.0f,(float)dir2.normalize().dot(norDir.normalize())/2.0f+0.6f);
                    float yOff1 = Mth.sin(i/4.0f*pie+sinAge)/6.0f;
                    float yOff2 = Mth.sin((i+1)/4.0f*pie+sinAge)/6.0f;

                    vertex(pose,consumer,oldDir.x*1.4f*(1.0f+yOff1),(1.0f+yOff1)*oldDot,oldDir.z*1.4f*(1.0f+yOff1),(i)/2.0f,0.0f,0,-1,0,255,modA);
                    vertex(pose,consumer,oldDir.x*0.74f,0,oldDir.z*0.74f,(i)/2.0f,1.0f,0,-1,0,event.getPackedLight(),modA);
                    vertex(pose,consumer,dir2.x*0.74f,0,dir2.z*0.74f,(i+1)/2.0f,1.0f,0,-1,0,event.getPackedLight(),modA);
                    vertex(pose,consumer,dir2.x*1.4f*(1.0f+yOff2),(1.0f+yOff2)*dot2,dir2.z*1.4f*(1.0f+yOff2),(i+1)/2.0f,0.0f,0,-1,0,255,modA);

                    vertex(pose,consumer,oldDir.x*1.4f,(1.0f+yOff1)*-0.2*oldDot,oldDir.z*1.4f,(i)/2.0f,1.0f,0,-1,0,event.getPackedLight(),0.0f);
                    vertex(pose,consumer,oldDir.x*0.74f,0,oldDir.z*0.74f,(i)/2.0f,0.8f,0,-1,0,event.getPackedLight(),modA);
                    vertex(pose,consumer,dir2.x*0.74f,0,dir2.z*0.74f,(i+1)/2.0f,0.8f,0,-1,0,event.getPackedLight(),modA);
                    vertex(pose,consumer,dir2.x*1.4f,(1.0f+yOff2)*-0.2*dot2,dir2.z*1.4f,(i+1)/2.0f,1.0f,0,-1,0,event.getPackedLight(),0.0f);

                    oldDir=dir2;
                    oldDot=dot2;
                }
                poseStack.popPose();
            }
        }
    }
    public static int[] SIZED = {0,0};
    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre post) throws IOException {
        if(SIZED[0]!=Minecraft.getInstance().getWindow().getWidth()){
            SIZED[0]=Minecraft.getInstance().getWindow().getWidth();
            SIZED[1]=Minecraft.getInstance().getWindow().getHeight();

            RABIES_TARGET.resize(SIZED[0],SIZED[1],true);
            RABIES.resize(SIZED[0],SIZED[1]);
            SLEEP.resize(SIZED[0],SIZED[1]);
            MINEDFLAYER.resize(SIZED[0],SIZED[1]);
        }
        if(Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.hasEffect(CFMobEffectInit.SLEEPY)){
            PROXY.eebyDeebyNess=Math.min(1.0f,PROXY.eebyDeebyNess+0.02f);
        }
        else if(PROXY.eebyDeebyNess>0.0f){
            PROXY.eebyDeebyNess=Math.max(0.0f,PROXY.eebyDeebyNess-0.02f);
        }
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CFParticleInit.SLEEPY_PARTICLE.get(), SleepyParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.SLEEPY_EXPLODE_PARTICLE.get(), SleepyExplodeParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.LEAF_PARTICLE.get(), LeafParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.RABIES_PARTICLE.get(), RabiesParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.TOOT_PARTICLE.get(), VertigoHornParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.CLEAVE_PARTICLE.get(), RotatedVertigoHornParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.FLOWER_PARTICLE.get(), FlowerParticle.Provider::new);
        event.registerSpriteSet(CFParticleInit.CRIT_PARTICLE.get(), CritParticle.Provider::new);


    }
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MindsEntityModel.LAYER_LOCATION, MindsEntityModel::createBodyLayer);
        event.registerLayerDefinition(MachinationEntityModel.LAYER_LOCATION, MachinationEntityModel::createBodyLayer);
        event.registerLayerDefinition(SinisterEntityModel.LAYER_LOCATION, SinisterEntityModel::createBodyLayer);
        event.registerLayerDefinition(BeautyEntityModel.LAYER_LOCATION, BeautyEntityModel::createBodyLayer);
        event.registerLayerDefinition(BlitzEntityModel.LAYER_LOCATION, BlitzEntityModel::createBodyLayer);
        event.registerLayerDefinition(EeperEntityModel.LAYER_LOCATION, EeperEntityModel::createBodyLayer);
        event.registerLayerDefinition(MinedFlayerEntityModel.LAYER_LOCATION, MinedFlayerEntityModel::createBodyLayer);
        event.registerLayerDefinition(NoThingEntityModel.LAYER_LOCATION, NoThingEntityModel::createBodyLayer);
        event.registerLayerDefinition(CanaryEntityModel.LAYER_LOCATION, CanaryEntityModel::createBodyLayer);
        event.registerLayerDefinition(CanaryEntityPartModel.LAYER_LOCATION, CanaryEntityPartModel::createBodyLayer);
        event.registerLayerDefinition(VertigoEntityModel.LAYER_LOCATION, VertigoEntityModel::createBodyLayer);
        event.registerLayerDefinition(FiendEntityModel.LAYER_LOCATION, FiendEntityModel::createBodyLayer);

    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CFEntityInit.MINDS.get(), MindsEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.MACHINATION.get(), MachinationEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.SINISTER.get(), SinisterEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.BEAUTY.get(), BeautyEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.BLITZ.get(), BlitzEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.EEPER.get(), EeperEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.MINEDFLAYER.get(), MinedFlayerEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.NOTHING.get(), NoThingEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.CANARY.get(), CanaryEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.CANARY_PART.get(), CanaryPartRenderer::new);
        event.registerEntityRenderer(CFEntityInit.VERTIGO.get(), VertigoEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.FIEND.get(), FiendEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.EEP.get(), EepEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.KICKED_BLOCK.get(), KickedBlockEntityRenderer::new);
        event.registerEntityRenderer(CFEntityInit.MURKY_PEARL.get(), (c)->{return new MurkyPearlEntityRenderer(c,1.0f,false);});
    }
    public static void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
