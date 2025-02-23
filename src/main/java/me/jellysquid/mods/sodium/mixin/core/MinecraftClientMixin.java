package me.jellysquid.mods.sodium.mixin.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        String[] cpus = new String[] {
                "8x 11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz",
                "8x i7-1165G7 @ 2.80",
                "24x AMD Ryzen 9 5900X 12-Core Processor ",
                "24x R9 5900X 12-Core",
                "32x AMD Ryzen 9 7950X3D 16-Core Processor ",
                "32x R9 7950X3D 16-Core",
                "12x AMD Ryzen 5 5600X 6-Core Processor ",
                "AMD Ryzen 7 2700 Eight-Core Processor ",
                "16x AMD Ryzen 7 5800H with Radeon Graphics ",
                "Snapdragon(R)  Elite - X1E8100 - Qualcomm(R) Oryon(TM) CPU",
                "Snapdragon (TM) 8cx Gen 2 @ 3.15 GHz",
                "32x 13th Gen Intel(R) Core(TM) i9-13900k",
                "16x Intel(R) Xeon(R) CPU E5-2640 v3 @ 2.60GHZ",
                "8x Apple M1",
                "E: 11CPU: ",
                "E: 11/111CPU: "
        };
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        for (String cpu : cpus) {
            System.out.printf("%s: %s\n", cpu, tr.getWidth(cpu));
        }
    }
}
