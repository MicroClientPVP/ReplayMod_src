package com.replaymod.render.mixin;

import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({EntityRenderer.class})
public abstract class Mixin_PreserveDepthDuringHandRendering {
   @ModifyArg(
      method = {"renderWorldPass"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlStateManager;clear(I)V",
         ordinal = 1
      ),
      index = 0
   )
   private int replayModRender_skipClearWhenRecordingDepth(int mask) {
      EntityRendererHandler handler = ((EntityRendererHandler.IEntityRenderer)this).replayModRender_getHandler();
      if (handler != null && handler.getSettings().isDepthMap()) {
         mask &= -257;
      }

      return mask;
   }
}
