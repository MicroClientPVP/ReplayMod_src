package com.replaymod.extras.playeroverview.mixin;

import com.replaymod.extras.ReplayModExtras;
import com.replaymod.extras.playeroverview.PlayerOverview;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {Render.class},
   priority = 1200
)
public abstract class MixinRender {
   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void replayModExtras_isPlayerHidden(Entity entity, @Coerce Object camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> ci) {
      ReplayModExtras.instance.get(PlayerOverview.class).ifPresent(playerOverview -> {
         if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (playerOverview.isHidden(player.getUniqueID())) {
               ci.setReturnValue(false);
            }
         }
      });
   }
}
