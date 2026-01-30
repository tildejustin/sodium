package me.jellysquid.mods.sodium.mixin.features.texture_updates;

import me.jellysquid.mods.sodium.client.util.color.ColorMixer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sprite.Interpolation.class)
public class MixinSpriteInterpolated {
    @Shadow
    @Final
    private NativeImage[] images;

    @Unique
    private Sprite parent;

    private static final int STRIDE = 4;

    /**
     * @author IMS
     * @reason Replace fragile Shadow
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    public void assignParent(Sprite parent, Sprite.Info info, int maxLevel, CallbackInfo ci) {
        this.parent = parent;
    }

    /**
     * @author JellySquid
     * @reason Drastic optimizations
     */
    @Overwrite
    void apply(Sprite.class_5790 animation) {
        Sprite.class_5791 animationFrame = animation.field_28472.get(animation.field_28470);

        int curIndex = animationFrame.field_28475;
        int nextIndex = animation.field_28472.get((animation.field_28470 + 1) % animation.field_28472.size()).index;

        if (curIndex == nextIndex) {
            return;
        }

        float delta = 1.0F - (float) animation.field_28471 / (float) animationFrame.field_28476;

        int f1 = ColorMixer.getStartRatio(delta);
        int f2 = ColorMixer.getEndRatio(delta);

        for (int layer = 0; layer < this.images.length; layer++) {
            int width = this.parent.getWidth() >> layer;
            int height = this.parent.getHeight() >> layer;

            int curX = ((curIndex % animation.field_28473) * width);
            int curY = ((curIndex / animation.field_28473) * height);

            int nextX = ((nextIndex % animation.field_28473) * width);
            int nextY = ((nextIndex / animation.field_28473) * height);

            NativeImage src = this.parent.images[layer];
            NativeImage dst = this.images[layer];

            // Source pointers
            long s1p = src.pointer + (curX + ((long) curY * src.getWidth()) * STRIDE);
            long s2p = src.pointer + (nextX + ((long) nextY * src.getWidth()) * STRIDE);

            // Destination pointers
            long dp = dst.pointer;

            int pixelCount = width * height;

            for (int i = 0; i < pixelCount; i++) {
                MemoryUtil.memPutInt(dp, ColorMixer.mixARGB(MemoryUtil.memGetInt(s1p), MemoryUtil.memGetInt(s2p), f1, f2));

                s1p += STRIDE;
                s2p += STRIDE;
                dp += STRIDE;
            }
        }

        this.parent.upload(0, 0, this.images);
    }

}
