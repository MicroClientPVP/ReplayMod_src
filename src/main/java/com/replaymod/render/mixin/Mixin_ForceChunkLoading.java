package com.replaymod.render.mixin;

import com.replaymod.render.hooks.ForceChunkLoadingHook;
import com.replaymod.render.hooks.IForceChunkLoading;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderGlobal.class})
public abstract class Mixin_ForceChunkLoading implements IForceChunkLoading {
   private ForceChunkLoadingHook replayModRender_hook;
   private boolean replayModRender_passThroughSetupTerrain;
   @Shadow
   private boolean displayListEntitiesDirty;
   @Shadow
   private ChunkRenderDispatcher renderDispatcher;

   @Override
   public void replayModRender_setHook(ForceChunkLoadingHook hook) {
      this.replayModRender_hook = hook;
   }

   @Shadow
   public abstract void setupTerrain(Entity var1, double var2, ICamera var4, int var5, boolean var6);

   @Inject(
      method = {"setupTerrain"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replayModRender_setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo ci) {
      if (this.replayModRender_hook != null && !this.replayModRender_passThroughSetupTerrain) {
            this.replayModRender_passThroughSetupTerrain = true;

            do {
               this.setupTerrain(viewEntity, partialTicks, camera, this.replayModRender_hook.nextFrameId(), playerSpectator);
               this.replayModRender_hook.updateChunks();
            } while (this.displayListEntitiesDirty);

            this.displayListEntitiesDirty = true;
            this.replayModRender_passThroughSetupTerrain = false;
            ci.cancel();
         }
   }

   @Inject(
      method = {"isPositionInRenderChunk"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void replayModRender_isPositionInRenderChunk(BlockPos pos, RenderChunk chunk, CallbackInfoReturnable<Boolean> ci) {
      if (this.replayModRender_hook != null) {
         ci.setReturnValue(true);
      }
   }

   @Inject(
      method = {"updateChunks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void replayModRender_updateChunks(long finishTimeNano, CallbackInfo ci) {
      if (this.replayModRender_hook != null) {
         this.replayModRender_hook.updateChunks();
         ci.cancel();
      }
   }

   @Inject(
      method = {"loadRenderers"},
      at = {@At("RETURN")}
   )
   private void setupChunkLoadingRenderGlobal(CallbackInfo ci) {
      if (this.replayModRender_hook != null) {
         this.replayModRender_hook.updateRenderDispatcher(this.renderDispatcher);
      }
   }
}
