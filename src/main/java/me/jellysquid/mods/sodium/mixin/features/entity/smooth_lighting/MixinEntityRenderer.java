package me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.jellysquid.mods.sodium.client.model.light.EntityLighter;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.entity.EntityLightSampler;
import me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting.accessor.WorldRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.AoOption;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> implements EntityLightSampler<T> {
    @Shadow
    protected abstract int getBlockLight(T entity, BlockPos blockPos);

    @Shadow
    protected abstract int method_27950(T entity, BlockPos blockPos);

    @Inject(method = "getLight", at = @At("HEAD"), cancellable = true)
    private void preGetLight(T entity, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        // Use smooth entity lighting if enabled
        if (MinecraftClient.getInstance().options.ao == AoOption.MAX) {
            cir.setReturnValue(EntityLighter.getBlendedLight(this, entity, tickDelta));
        }
    }

    @ModifyExpressionValue(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Frustum;isVisible(Lnet/minecraft/util/math/Box;)Z"))
    private boolean preShouldRender(boolean original, T entity, Frustum frustum, double x, double y, double z) {
        // If the entity isn't culled already by other means, try to perform a second pass
        if (original && !SodiumWorldRenderer.getInstance().isEntityVisible(entity)) {
            WorldRendererAccessor wra = ((WorldRendererAccessor) MinecraftClient.getInstance().worldRenderer);
            wra.setRegularEntityCount(wra.getRegularEntityCount() + 1);
            return false;
        }
        return original;
    }

    @Override
    public int bridge$getBlockLight(T entity, BlockPos pos) {
        return this.getBlockLight(entity, pos);
    }

    @Override
    public int bridge$getSkyLight(T entity, BlockPos pos) {
        return this.method_27950(entity, pos);
    }
}
