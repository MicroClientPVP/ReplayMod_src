package com.replaymod.replay.mixin;

import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ViewFrustum.class})
public abstract class MixinViewFrustum {
   @Redirect(
      method = {"updateChunkPositions"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;setPosition(Lnet/minecraft/util/BlockPos;)V"
      )
   )
   private void replayModReplay_updatePositionAndMarkForUpdate(RenderChunk renderChunk, BlockPos pos) {
      if (!pos.equals(renderChunk.getPosition())) {
         renderChunk.setPosition(pos);
         renderChunk.setNeedsUpdate(true);
      }
   }
}
