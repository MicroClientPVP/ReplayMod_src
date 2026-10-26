package com.replaymod.replay.mixin;

import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ItemRenderer.class})
public abstract class Mixin_FixHandOffsetTickDelta {
   @Redirect(
      method = {"rotateWithPlayerRotations"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F"
      )
   )
   private float getYaw(EntityPlayerSP instance, EntityPlayerSP arg, float tickDelta) {
      return instance instanceof CameraEntity ? instance.prevRotationYaw + (instance.rotationYaw - instance.prevRotationYaw) * tickDelta : instance.rotationYaw;
   }

   @Redirect(
      method = {"rotateWithPlayerRotations"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F"
      )
   )
   private float getPitch(EntityPlayerSP instance, EntityPlayerSP arg, float tickDelta) {
      return instance instanceof CameraEntity ? instance.prevRotationPitch + (instance.rotationPitch - instance.prevRotationPitch) * tickDelta : instance.rotationPitch;
   }
}
