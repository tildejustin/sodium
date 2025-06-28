package me.jellysquid.mods.sodium.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class EntityRendererHelper {
    // WorldRenderer#render, in entityRenderDispatcher.shouldRender loop
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean shownIfCulled(Entity entity) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        return entity.hasPassengerDeep(MinecraftClient.getInstance().player) && (entity != camera.getFocusedEntity()
                || camera.isThirdPerson()
                || camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity) camera.getFocusedEntity()).isSleeping());
    }
}
