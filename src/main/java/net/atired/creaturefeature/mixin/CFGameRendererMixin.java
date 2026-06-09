package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.accessors.PostChainDepthPassAccessor;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.accessors.GameRendererResourceManagerAccessor;
import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CFGameRendererMixin implements GameRendererResourceManagerAccessor {
    @Shadow @Final private ResourceManager resourceManager;
    @Inject(method = "renderLevel",at= @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",ordinal = 2,shift= At.Shift.BEFORE))
    private void renderDepthCF(DeltaTracker deltaTracker, CallbackInfo ci) {
        if(CreatureFeatureClient.RABIES_TARGET!=null){
            PostChain[] chains = {CreatureFeatureClient.MINEDFLAYER,CreatureFeatureClient.RABIES,CreatureFeatureClient.SLEEP};
            CreatureFeatureClient.RABIES_TARGET.clear(Minecraft.ON_OSX);
            CreatureFeatureClient.RABIES_TARGET.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            for(PostChain i : chains){
                if(i instanceof PostChainDepthPassAccessor accessor){
                    accessor.depthEmPostPasses();
                }
            }
        }

    }
        @Inject(method = "render",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V",ordinal = 0,shift= At.Shift.BEFORE))
    private void renderCF(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci){
        if(Minecraft.getInstance().levelRenderer!=null&&CreatureFeatureClient.RABIES_TARGET!=null&&Minecraft.getInstance().levelRenderer.getSectionRenderDispatcher()!=null&&
                CreatureFeatureClient.MINEDFLAYER !=null&&Minecraft.getInstance().player!=null&&
                Minecraft.getInstance().gameRenderer instanceof GameRendererResourceManagerAccessor accessor){

            if(Minecraft.getInstance().player instanceof PlayerBrainrotAccessor accessor1&&
                    Minecraft.getInstance().player instanceof LivingEntityGoopAccessor accessor2&&(accessor1.getBrainrot()>0.01f||accessor2.getGoop()>0.01f)){

                PostChain chain = CreatureFeatureClient.MINEDFLAYER;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",Math.max(accessor1.getBrainrot(),Math.clamp(accessor2.getGoop(),0.0f,0.33f)));
                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
            if(Minecraft.getInstance().player instanceof PlayerBrainrotAccessor accessor1&&accessor1.getRabies()>0.01f){

                PostChain chain = CreatureFeatureClient.RABIES;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",accessor1.getRabies());

                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
            if(CreatureFeatureClient.PROXY!=null&&CreatureFeatureClient.PROXY.eebyDeebyNess>0.0f){

                PostChain chain = CreatureFeatureClient.SLEEP;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",CreatureFeatureClient.PROXY.eebyDeebyNess);
                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
        }
    }
    @Override
    public ResourceManager creaturefeature$myPrecious() {
        return this.resourceManager;
    }
}
