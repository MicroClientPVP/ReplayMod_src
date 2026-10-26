package com.replaymod.recording.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.utils.Consumer;
import com.replaymod.recording.packet.ResourcePackRecorder;
import java.io.File;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ResourcePackRepository.class})
public abstract class MixinDownloadingPackFinder implements ResourcePackRecorder.IDownloadingPackFinder {
   private Consumer<File> requestCallback;

   @Override
   public void setRequestCallback(Consumer<File> callback) {
      this.requestCallback = callback;
   }

   @Inject(
      method = {"setResourcePackInstance"},
      at = {@At("HEAD")}
   )
   private void recordDownloadedPack(File file, CallbackInfoReturnable ci) {
      if (this.requestCallback != null) {
         this.requestCallback.consume(file);
         this.requestCallback = null;
      }
   }
}
