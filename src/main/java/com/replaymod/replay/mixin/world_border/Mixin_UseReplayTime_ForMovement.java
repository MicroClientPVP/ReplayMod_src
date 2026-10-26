package com.replaymod.replay.mixin.world_border;

import com.replaymod.core.versions.MCVer;
import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.ReplayModReplay;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldBorder.class})
public class Mixin_UseReplayTime_ForMovement {
   @Redirect(
      method = {"*"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/lang/System;currentTimeMillis()J"
      )
   )
   private long getWorldBorderTime() {
      ReplayHandler replayHandler = ReplayModReplay.instance.getReplayHandler();
      return replayHandler != null ? (long)replayHandler.getReplaySender().currentTimeStamp() : MCVer.milliTime();
   }
}
