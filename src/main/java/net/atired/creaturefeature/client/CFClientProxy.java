package net.atired.creaturefeature.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.FriendEntityRenderer;
import net.atired.creaturefeature.client.renderers.SaintSolisEntityRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.SequencedMap;

import static net.atired.creaturefeature.client.renderers.StarProjEntityRenderer.STARPROJ_LOCATION;

public class CFClientProxy {
    public float eebyDeebyNess = 0.0f;
    public float flayed2 = 0.0f;
    public float rabies2 = 0.0f;
    public float gasLeak = 0.0f;
    public float wobble = 0.0f;
    public float bacterial = 0.0f;
    public ItemStack wobblyItem=null;
    public int attachment = 0;
    public float[] colours = {0.0f,0.0f,0.0f,0.0f};
    private static final ResourceLocation DETRITUS_LOCATION = CreatureFeature.getId("textures/entity/detritus.png");
    public static MultiBufferSource.BufferSource DETRITUS_SOURCE = null;

    public static MultiBufferSource.BufferSource getDetritusSource(){
        if(DETRITUS_SOURCE!=null){
            return DETRITUS_SOURCE;
        }else{
            SequencedMap<RenderType,ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(CFRenderTypes.entityMonochromeCull(DETRITUS_LOCATION),
                    new ByteBufferBuilder(CFRenderTypes.entityMonochromeCull(DETRITUS_LOCATION).bufferSize()));
            DETRITUS_SOURCE=MultiBufferSource.immediateWithBuffers(buffers,new ByteBufferBuilder(256));
            return DETRITUS_SOURCE;
        }
    }
    private static final ResourceLocation FEND_LOCATION = CreatureFeature.getId("textures/entity/fend.png");
    public static MultiBufferSource.BufferSource FEND_SOURCE = null;

    public static MultiBufferSource.BufferSource getFendSource(){
        if(FEND_SOURCE!=null){
            return FEND_SOURCE;
        }else{
            SequencedMap<RenderType,ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(CFRenderTypes.entityFendCutout(FEND_LOCATION),
                    new ByteBufferBuilder(CFRenderTypes.entityFendCutout(FEND_LOCATION).bufferSize()));
            FEND_SOURCE=MultiBufferSource.immediateWithBuffers(buffers,new ByteBufferBuilder(256));
            return FEND_SOURCE;
        }
    }
    public static MultiBufferSource.BufferSource SUN_SOURCE = null;

    public static MultiBufferSource.BufferSource getSunSource(){
        if(SUN_SOURCE!=null){
            return SUN_SOURCE;
        }else{
            SequencedMap<RenderType,ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(RenderType.entityTranslucent((SaintSolisEntityRenderer.SUN_LOCATION)),
                    new ByteBufferBuilder(RenderType.entityTranslucent(SaintSolisEntityRenderer.SUN_LOCATION).bufferSize()));
            buffers.put(RenderType.entityTranslucent(STARPROJ_LOCATION),
                    new ByteBufferBuilder(RenderType.entityTranslucent(STARPROJ_LOCATION).bufferSize()));

            SUN_SOURCE=MultiBufferSource.immediateWithBuffers(buffers,new ByteBufferBuilder(256));
            return SUN_SOURCE;
        }
    }
    public static MultiBufferSource.BufferSource FRIEND_SOURCE = null;

    public static MultiBufferSource.BufferSource getFriendSource(){
        if(FRIEND_SOURCE!=null){
            return FRIEND_SOURCE;
        }else{
            SequencedMap<RenderType,ByteBufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(CFRenderTypes.entityFriendCutout(FriendEntityRenderer.CUBE_LOCATION),
                    new ByteBufferBuilder(CFRenderTypes.entityFriendCutout(FriendEntityRenderer.CUBE_LOCATION).bufferSize()));
            buffers.put(CFRenderTypes.entitySilkCull(FriendEntityRenderer.CUBE_LOCATION),
                    new ByteBufferBuilder(CFRenderTypes.entitySilkCull(FriendEntityRenderer.CUBE_LOCATION).bufferSize()));
            buffers.put(RenderType.entityTranslucent(FriendEntityRenderer.SMILE_FRIENDLESS_LOCATION),
                    new ByteBufferBuilder(RenderType.entityTranslucent(FriendEntityRenderer.SMILE_FRIENDLESS_LOCATION).bufferSize()));

            buffers.put(RenderType.entityTranslucent(FriendEntityRenderer.SMILE_LOCATION),
                    new ByteBufferBuilder(RenderType.entityTranslucent(FriendEntityRenderer.SMILE_LOCATION).bufferSize()));
            FRIEND_SOURCE=MultiBufferSource.immediateWithBuffers(buffers,new ByteBufferBuilder(256));
            return FRIEND_SOURCE;
        }
    }
}
