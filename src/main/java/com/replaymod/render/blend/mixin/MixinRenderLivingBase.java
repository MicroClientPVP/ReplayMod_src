package com.replaymod.render.blend.mixin;

import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.EntityExporter;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRenderLivingBase {
   @Inject(
      method = {"doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/RendererLivingEntity;preRenderCallback(Lnet/minecraft/entity/EntityLivingBase;F)V",
         shift = Shift.AFTER
      )}
   )
   private void recordModelMatrix(CallbackInfo ci) {
      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(EntityExporter.class).postEntityLivingSetup();
      }
   }
}
