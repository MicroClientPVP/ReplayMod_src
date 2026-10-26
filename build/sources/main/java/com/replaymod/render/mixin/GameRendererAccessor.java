package com.replaymod.render.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({EntityRenderer.class})
public interface GameRendererAccessor {
   @Accessor
   boolean getRenderHand();

   @Accessor
   void setRenderHand(boolean var1);
}
