package net.atired.creaturefeature.accessors;

import net.minecraft.client.renderer.PostPass;

import java.util.List;

public interface PostChainDepthPassAccessor {
    List<PostPass> getDemPostPasses();
    void depthEmPostPasses();
}
