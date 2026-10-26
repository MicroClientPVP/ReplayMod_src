package com.replaymod.render.mixin;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ChunkRenderDispatcher.class})
public interface ChunkRenderDispatcherAccessor {
   @Accessor
   List<ChunkRenderWorker> getListThreadedWorkers();

   @Accessor
   BlockingQueue<ChunkCompileTaskGenerator> getQueueChunkUpdates();

   @Accessor
   void setQueueChunkUpdates(BlockingQueue<ChunkCompileTaskGenerator> var1);
}
