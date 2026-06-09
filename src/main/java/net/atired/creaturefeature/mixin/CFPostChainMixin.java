package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.accessors.PostChainDepthPassAccessor;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PostChain.class)
public class CFPostChainMixin implements PostChainDepthPassAccessor {
    @Shadow @Final private List<PostPass> passes;

    @Override
    public List<PostPass> getDemPostPasses() {
        return this.passes;
    }

    @Override
    public void depthEmPostPasses() {
        if(CreatureFeatureClient.RABIES_TARGET!=null)
            for(PostPass i : this.passes){
                i.getEffect().setSampler("TrueDepthSampler", CreatureFeatureClient.RABIES_TARGET::getDepthTextureId);
            }
    }
}
