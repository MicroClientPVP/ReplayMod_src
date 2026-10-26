package com.replaymod.render.mixin;

import com.replaymod.core.versions.MCVer;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ChunkRenderWorker.class})
public abstract class MixinChunkRenderWorker implements MCVer.ChunkRenderWorkerAccessor {
   @Shadow
   abstract void processTask(ChunkCompileTaskGenerator var1) throws InterruptedException;

   @Override
   public void doRunTask(ChunkCompileTaskGenerator task) throws InterruptedException {
      this.processTask(task);
   }
}
