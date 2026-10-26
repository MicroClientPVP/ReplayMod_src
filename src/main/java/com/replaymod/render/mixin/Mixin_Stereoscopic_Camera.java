package com.replaymod.render.mixin;

import com.replaymod.render.capturer.StereoscopicOpenGlFrameCapturer;
import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class Mixin_Stereoscopic_Camera implements EntityRendererHandler.IEntityRenderer {
   @Inject(
      method = {"setupCameraTransform"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlStateManager;loadIdentity()V",
         shift = Shift.AFTER,
         ordinal = 0
      )}
   )
   private void replayModRender_setupStereoscopicProjection(CallbackInfo ci) {
      if (this.replayModRender_getHandler() != null) {
         if (this.replayModRender_getHandler().data == StereoscopicOpenGlFrameCapturer.Data.LEFT_EYE) {
            GL11.glTranslatef(0.07F, 0.0F, 0.0F);
         } else if (this.replayModRender_getHandler().data == StereoscopicOpenGlFrameCapturer.Data.RIGHT_EYE) {
            GL11.glTranslatef(-0.07F, 0.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"setupCameraTransform"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlStateManager;loadIdentity()V",
         shift = Shift.AFTER,
         ordinal = 1
      )}
   )
   private void replayModRender_setupStereoscopicModelView(CallbackInfo ci) {
      if (this.replayModRender_getHandler() != null) {
         if (this.replayModRender_getHandler().data == StereoscopicOpenGlFrameCapturer.Data.LEFT_EYE) {
            GL11.glTranslatef(0.1F, 0.0F, 0.0F);
         } else if (this.replayModRender_getHandler().data == StereoscopicOpenGlFrameCapturer.Data.RIGHT_EYE) {
            GL11.glTranslatef(-0.1F, 0.0F, 0.0F);
         }
      }
   }
}
