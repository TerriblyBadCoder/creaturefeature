package net.atired.creaturefeature.accessors;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
public class NoBlockContainerLevelAccess implements ContainerLevelAccess {
    private final Entity entity;

    public NoBlockContainerLevelAccess(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void execute(BiConsumer<Level, BlockPos> levelPosConsumer) {
        ContainerLevelAccess.super.execute(levelPosConsumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> evaluate(BiFunction<Level, BlockPos,T> func) {
        return Optional.ofNullable(func.apply(entity.level(), entity.blockPosition()));
    }

}
