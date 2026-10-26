package com.replaymod.replay.mixin;

import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerControllerMP.class})
public abstract class MixinPlayerControllerMP {
   @Shadow
   private Minecraft mc;

   @Inject(
      method = {"isSpectator"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replayModReplay_isSpectator(CallbackInfoReturnable<Boolean> ci) {
      if (this.mc.thePlayer instanceof CameraEntity) {
         ci.setReturnValue(this.mc.thePlayer.isSpectator());
      }
   }
}
