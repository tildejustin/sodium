package me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting;

import me.jellysquid.mods.sodium.client.model.light.EntityLighter;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.entity.EntityLightSampler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.AoOption;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> implements EntityLightSampler<T> {
    @Inject(method = "getLight", at = @At("HEAD"), cancellable = true)
    private void preGetLight(T entity, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        // Use smooth entity lighting if enabled
        if (MinecraftClient.getInstance().options.ao == AoOption.MAX) {
            cir.setReturnValue(EntityLighter.getBlendedLight(this, entity, tickDelta));
        }
    }

    @Inject(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Frustum;isVisible(Lnet/minecraft/util/math/Box;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void preShouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        // If the entity isn't culled already by other means, try to perform a second pass
        if (cir.getReturnValue() && !SodiumWorldRenderer.getInstance().isEntityVisible(entity)) {
//            MinecraftClient.getInstance().worldRenderer.regularEntityCount++;
            cir.setReturnValue(false);
        }
    }

    @Override
    public int bridge$getBlockLight(T entity, BlockPos pos) {
        return entity.isOnFire() ? 15 : entity.world.getLightLevel(LightType.BLOCK, pos);
    }

    @Override
    public int bridge$getSkyLight(T entity, BlockPos pos) {
        return entity.world.getLightLevel(LightType.SKY, pos);
    }
}
