package com.replaymod.replay.mixin;

import com.replaymod.core.versions.MCVer;
import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRenderLivingBase {
   @Inject(
      method = {"canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void replayModReplay_canRenderInvisibleName(EntityLivingBase entity, CallbackInfoReturnable<Boolean> ci) {
      EntityPlayer thePlayer = MCVer.getMinecraft().thePlayer;
      if (thePlayer instanceof CameraEntity && entity.isInvisible()) {
         ci.setReturnValue(false);
      }
   }

   @Redirect(
      method = {"renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/EntityLivingBase;isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z"
      )
   )
   private boolean replayModReplay_shouldInvisibleNotBeRendered(EntityLivingBase entity, EntityPlayer thePlayer) {
      return thePlayer instanceof CameraEntity || entity.isInvisibleToPlayer(thePlayer);
   }
}
