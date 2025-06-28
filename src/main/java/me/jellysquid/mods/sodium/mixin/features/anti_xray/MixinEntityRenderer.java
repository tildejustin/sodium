package me.jellysquid.mods.sodium.mixin.features.anti_xray;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.entity.EntityRendererHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Inject(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Frustum;isVisible(Lnet/minecraft/util/math/Box;)Z", shift = At.Shift.AFTER), cancellable = true)
    private <T extends Entity> void cullEntitiesInUnbuiltChunks(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        boolean xrayFix = SodiumClientMod.options().speedrun.xrayFix;
        if (cir.getReturnValue() && xrayFix && !SodiumWorldRenderer.getInstance().hasRenderData(entity.chunkX, entity.chunkY, entity.chunkZ)) {
            if (!EntityRendererHelper.shownIfCulled(entity)) {
                MinecraftClient.getInstance().worldRenderer.regularEntityCount++;
            }
            cir.setReturnValue(false);
        }
    }
}
