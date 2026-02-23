package me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting.accessor;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Accessor
    int getRegularEntityCount();

    @Accessor
    void setRegularEntityCount(int regularEntityCount);
}
