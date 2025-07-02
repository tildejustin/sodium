package me.jellysquid.mods.sodium.mixin.features.anti_xray;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "processF3", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/options/GameOptions;advancedItemTooltips:Z", shift = At.Shift.AFTER))
    private void testXraySwap(int key, CallbackInfoReturnable<Boolean> cir) {
        SodiumClientMod.options().speedrun.xrayFix = this.client.options.advancedItemTooltips;
    }
}
