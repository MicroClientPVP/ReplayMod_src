package com.replaymod.recording.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.util.Map.Entry;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public abstract class MixinClientConnection {
   @Shadow
   private Channel channel;

   @Inject(
      method = {"setCompressionTreshold"},
      at = {@At("RETURN")}
   )
   private void ensureReplayModRecorderIsAfterDecompress(CallbackInfo ci) {
      ChannelHandler recorder = null;

      for (Entry<String, ChannelHandler> entry : this.channel.pipeline()) {
         String key = entry.getKey();
         if ("replay_recorder_raw".equals(key)) {
            recorder = entry.getValue();
         }

         if ("decompress".equals(key) && recorder != null) {
            this.channel.pipeline().remove(recorder);
            this.channel.pipeline().addBefore("decoder", "replay_recorder_raw", recorder);
            return;
         }
      }
   }
}
