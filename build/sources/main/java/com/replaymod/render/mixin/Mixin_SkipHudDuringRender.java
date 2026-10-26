package com.replaymod.render.mixin;

import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiIngame.class})
public abstract class Mixin_SkipHudDuringRender {
   @Inject(
      method = {"renderGameOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replayModRender_skipHudDuringRender(CallbackInfo ci) {
      if (((EntityRendererHandler.IEntityRenderer)Minecraft.getMinecraft().entityRenderer).replayModRender_getHandler() != null) {
         ci.cancel();
      }
   }
}
