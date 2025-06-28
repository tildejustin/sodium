package me.jellysquid.mods.sodium.mixin.features.anti_xray;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameOverlayRenderer.class)
public abstract class MixinInGameOverlayRenderer {
    @ModifyExpressionValue(method = "getInWallBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEyeY()D"))
    private static double useCameraYForBlockOverlay(double original) {
        boolean xrayFix = SodiumClientMod.options().speedrun.xrayFix;
        double cameraY = MinecraftClient.getInstance().gameRenderer.getCamera().getPos().y;
        return xrayFix ? cameraY : original;
    }
}
