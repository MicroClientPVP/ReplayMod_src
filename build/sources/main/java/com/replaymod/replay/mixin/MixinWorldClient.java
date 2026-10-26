package com.replaymod.replay.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldClient.class})
public abstract class MixinWorldClient {
   @Inject(
      method = {"addEntityToWorld"},
      at = {@At("HEAD")}
   )
   public void replayModReplay_fix_addEntityToWorld(int entityId, Entity entity, CallbackInfo ci) {
      entity.setEntityId(entityId);
   }
}
