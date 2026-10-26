package com.replaymod.recording.mixin;

import com.replaymod.recording.ReplayModRecording;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetHandlerLoginClient.class})
public abstract class MixinNetHandlerLoginClient {
   @Final
   @Shadow
   private NetworkManager networkManager;

   @Inject(
      method = {"handleLoginSuccess"},
      at = {@At("HEAD")}
   )
   public void replayModRecording_initiateRecording(CallbackInfo cb) {
      if (ReplayModRecording.instance != null) {
         ReplayModRecording.instance.initiateRecording(this.networkManager);
      }
   }
}
