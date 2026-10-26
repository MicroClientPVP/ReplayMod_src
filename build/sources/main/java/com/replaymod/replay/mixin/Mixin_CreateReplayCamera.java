package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerControllerMP.class})
public abstract class Mixin_CreateReplayCamera {
   @Shadow
   private Minecraft mc;
   @Shadow
   private NetHandlerPlayClient netClientHandler;

   @Inject(
      method = {"func_178892_a"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replayModReplay_createReplayCamera(World worldIn, StatFileWriter statFileWriter, CallbackInfoReturnable<EntityPlayerSP> ci) {
      if (ReplayModReplay.instance.getReplayHandler() != null) {
         ci.setReturnValue(new CameraEntity(this.mc, worldIn, this.netClientHandler, statFileWriter));
         ci.cancel();
      }
   }
}
