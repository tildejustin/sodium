package me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @WrapWithCondition(method = "render", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=blockentities"))
    private boolean doNothing(Profiler profiler, String location) {
        if (!((WorldRendererExtended) this).getSodiumWorldRenderer().getUseEntityCulling()) {
            return true;
        }
        profiler.swap("turn off entity culling for blockentities");
        profiler.pop();
        return false;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = "ldc=destroyProgress"))
    private boolean doNothing2(Profiler profiler, String location) {
        if (!((WorldRendererExtended) this).getSodiumWorldRenderer().getUseEntityCulling()) {
            return true;
        }
        profiler.push(location);
        return false;
    }
}
