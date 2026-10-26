package com.replaymod.render.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderGlobal.class})
public abstract class Mixin_ChromaKeyColorSky {
   @Shadow
   @Final
   private Minecraft mc;

   @Inject(
      method = {"renderSky(FI)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void chromaKeyingSky(CallbackInfo ci) {
      EntityRendererHandler handler = ((EntityRendererHandler.IEntityRenderer)this.mc.entityRenderer).replayModRender_getHandler();
      if (handler != null) {
         ReadableColor color = handler.getSettings().getChromaKeyingColor();
         if (color != null) {
            GlStateManager.clearColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
            GlStateManager.clear(16384);
            ci.cancel();
         }
      }
   }
}
