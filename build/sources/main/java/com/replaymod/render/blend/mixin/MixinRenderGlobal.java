package com.replaymod.render.blend.mixin;

import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.EntityExporter;
import com.replaymod.render.blend.exporters.TileEntityExporter;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderGlobal.class})
public abstract class MixinRenderGlobal {
   @Inject(
      method = {"renderEntities"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntitySimple(Lnet/minecraft/entity/Entity;F)Z"
      )}
   )
   private void preEntityRender(CallbackInfo ci) {
      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(EntityExporter.class).preEntitiesRender();
      }
   }

   @Inject(
      method = {"renderEntities"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/RenderManager;renderEntitySimple(Lnet/minecraft/entity/Entity;F)Z",
         shift = Shift.AFTER
      )}
   )
   private void postEntityRender(CallbackInfo ci) {
      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(EntityExporter.class).postEntitiesRender();
      }
   }

   @Inject(
      method = {"renderEntities"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;renderTileEntity(Lnet/minecraft/tileentity/TileEntity;FI)V"
      )}
   )
   private void preTileEntityRender(CallbackInfo ci) {
      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(TileEntityExporter.class).preTileEntitiesRender();
      }
   }

   @Inject(
      method = {"renderEntities"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;renderTileEntity(Lnet/minecraft/tileentity/TileEntity;FI)V",
         shift = Shift.AFTER
      )}
   )
   private void postTileEntityRender(CallbackInfo ci) {
      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(TileEntityExporter.class).postTileEntitiesRender();
      }
   }
}
