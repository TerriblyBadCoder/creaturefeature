package net.atired.creaturefeature.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.creaturefeature.accessors.PoseStackAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;

@Mixin(PoseStack.class)
public class IamActivelyHavingaMigraineMixin implements PoseStackAccessor {
    @Shadow @Final private Deque<PoseStack.Pose> poseStack;

    @Override
    public Deque<PoseStack.Pose> getPoseStacks() {
        return this.poseStack;
    }
}
