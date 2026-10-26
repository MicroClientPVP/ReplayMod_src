package com.replaymod.render.mixin;

import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({EntityRenderer.class})
public abstract class Mixin_Omnidirectional_Camera implements EntityRendererHandler.IEntityRenderer {
   @Redirect(
      method = {"setupCameraTransform"},
      at = @At(
         value = "INVOKE",
         target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V",
         remap = false
      )
   )
   private void replayModRender_gluPerspective$0(float fovY, float aspect, float zNear, float zFar) {
      this.replayModRender_gluPerspective(fovY, aspect, zNear, zFar);
   }

   @Redirect(
      method = {"renderWorldPass"},
      at = @At(
         value = "INVOKE",
         target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V",
         remap = false
      )
   )
   private void replayModRender_gluPerspective$1(float fovY, float aspect, float zNear, float zFar) {
      this.replayModRender_gluPerspective(fovY, aspect, zNear, zFar);
   }

   @Redirect(
      method = {"renderCloudsCheck"},
      at = @At(
         value = "INVOKE",
         target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V",
         remap = false
      )
   )
   private void replayModRender_gluPerspective$2(float fovY, float aspect, float zNear, float zFar) {
      this.replayModRender_gluPerspective(fovY, aspect, zNear, zFar);
   }

   private void replayModRender_gluPerspective(float fovY, float aspect, float zNear, float zFar) {
      if (this.replayModRender_getHandler() != null && this.replayModRender_getHandler().omnidirectional) {
         fovY = 90.0F;
         aspect = 1.0F;
      }

      Project.gluPerspective(fovY, aspect, zNear, zFar);
   }
}
