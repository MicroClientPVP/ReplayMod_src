package com.replaymod.render.mixin;

import java.util.Set;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({RenderGlobal.class})
public interface WorldRendererAccessor {
   @Accessor
   void setRenderEntitiesStartupCounter(int var1);

   @Accessor
   ChunkRenderDispatcher getRenderDispatcher();

   @Accessor
   void setDisplayListEntitiesDirty(boolean var1);

   @Accessor
   Set<RenderChunk> getChunksToUpdate();
}
