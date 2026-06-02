package net.atired.creaturefeature.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Function;

@EventBusSubscriber(modid = CreatureFeature.MODID,value = Dist.CLIENT)
public class CFRenderTypes {
    public static ShaderInstance OUTLINED_SHADER_INSTANCE = null;
    public static ShaderInstance getOutlinedShaderInstance(){return OUTLINED_SHADER_INSTANCE;}
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_OUTLINED_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard
            (CFRenderTypes::getOutlinedShaderInstance);
    public static final Function<ResourceLocation, RenderType> ENTITY_OUTLINED_CUTOUT = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_OUTLINED_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.EmptyTextureStateShard(()->{
                            if(Minecraft.getInstance().getMainRenderTarget()!=null)
                                RenderSystem.setShaderTexture(0,Minecraft.getInstance().getMainRenderTarget().getDepthTextureId());
                        },()->{}))
                        .setCullState(RenderType.NO_CULL)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_outlined", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, rendertype$compositestate);
            }
    );
    public static RenderType entityOutlinedCutout(ResourceLocation location) {
        return ENTITY_OUTLINED_CUTOUT.apply(location);
    }
    public static ShaderInstance AMBUSH_SHADER_INSTANCE = null;
    public static ShaderInstance getAmbushShaderInstance(){return AMBUSH_SHADER_INSTANCE;}
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_AMBUSH_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard
            (CFRenderTypes::getAmbushShaderInstance);
    public static final Function<ResourceLocation, RenderType> ENTITY_AMBUSH_CUTOUT = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_AMBUSH_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286169_, false, false))
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_ambush", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, rendertype$compositestate);
            }
    );
    public static final Function<ResourceLocation, RenderType> ENTITY_AMBUSH_UNLIT_CUTOUT = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_AMBUSH_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286169_, false, false))
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.NO_LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_ambush", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, rendertype$compositestate);
            }
    );
    public static RenderType entityAmbushCutout(ResourceLocation location) {
        return ENTITY_AMBUSH_CUTOUT.apply(location);
    }
    public static RenderType entityUnlitAmbushCutout(ResourceLocation location) {
        return ENTITY_AMBUSH_UNLIT_CUTOUT.apply(location);
    }
    @SubscribeEvent
    public static void loadShaders(RegisterShadersEvent registerShadersEvent) throws IOException {
        registerShadersEvent.registerShader(
                new ShaderInstance(registerShadersEvent.getResourceProvider(), CreatureFeature.getId("rendertype_entity_outlined"), DefaultVertexFormat.NEW_ENTITY)
                ,(a)->{OUTLINED_SHADER_INSTANCE=a;});
        registerShadersEvent.registerShader(
                new ShaderInstance(registerShadersEvent.getResourceProvider(), CreatureFeature.getId("rendertype_entity_ambush"), DefaultVertexFormat.NEW_ENTITY)
                ,(a)->{AMBUSH_SHADER_INSTANCE=a;});
    }
}
