package com.replaymod.render.mixin;

import com.replaymod.core.versions.MCVer;
import com.replaymod.render.capturer.CubicOpenGlFrameCapturer;
import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class Mixin_Omnidirectional_Rotation {
   private EntityRendererHandler getHandler() {
      return ((EntityRendererHandler.IEntityRenderer)MCVer.getMinecraft().entityRenderer).replayModRender_getHandler();
   }

   @Inject(
      method = {"orientCamera"},
      at = {@At("HEAD")}
   )
   private void replayModRender_setupCubicFrameRotation(CallbackInfo ci) {
      if (this.getHandler() != null && this.getHandler().data instanceof CubicOpenGlFrameCapturer.Data) {
         CubicOpenGlFrameCapturer.Data data = (CubicOpenGlFrameCapturer.Data)this.getHandler().data;
         float angle = 0.0F;
         float x = 0.0F;
         float y = 0.0F;
         switch (data) {
            case FRONT:
               angle = 0.0F;
               y = 1.0F;
               break;
            case RIGHT:
               angle = 90.0F;
               y = 1.0F;
               break;
            case BACK:
               angle = 180.0F;
               y = 1.0F;
               break;
            case LEFT:
               angle = -90.0F;
               y = 1.0F;
               break;
            case TOP:
               angle = -90.0F;
               x = 1.0F;
               break;
            case BOTTOM:
               angle = 90.0F;
               x = 1.0F;
         }

         GL11.glRotatef(angle, x, y, 0.0F);
         MCVer.getMinecraft().renderGlobal.setDisplayListEntitiesDirty();
      }

      if (this.getHandler() != null && this.getHandler().omnidirectional) {
         GL11.glTranslatef(0.0F, 0.0F, 0.1F);
      }
   }
}
