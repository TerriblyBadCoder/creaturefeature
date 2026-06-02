package net.atired.creaturefeature.accessors;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Deque;

public interface PoseStackAccessor {
    Deque<PoseStack.Pose> getPoseStacks();

}
