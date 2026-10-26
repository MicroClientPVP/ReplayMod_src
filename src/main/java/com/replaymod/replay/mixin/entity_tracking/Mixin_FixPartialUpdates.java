package com.replaymod.replay.mixin.entity_tracking;

import com.replaymod.replay.ext.EntityExt;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetHandlerPlayClient.class})
public class Mixin_FixPartialUpdates {
   private static final String ENTITY_UPDATE = "Lnet/minecraft/entity/Entity;setPositionAndRotation2(DDDFFIZ)V";
   @Unique
   private Entity entity;

   @Redirect(
      method = {"handleEntityMovement"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/entity/Entity;rotationYaw:F",
         opcode = 180
      )
   )
   private float getTrackedYaw(Entity instance) {
      return ((EntityExt)instance).replaymod$getTrackedYaw();
   }

   @Redirect(
      method = {"handleEntityMovement"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/entity/Entity;rotationPitch:F",
         opcode = 180
      )
   )
   private float getTrackedPitch(Entity instance) {
      return ((EntityExt)instance).replaymod$getTrackedPitch();
   }

   @ModifyVariable(
      method = {"handleEntityMovement", "handleEntityTeleport"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;setPositionAndRotation2(DDDFFIZ)V"
      ),
      ordinal = 0
   )
   private Entity captureEntity(Entity entity) {
      return this.entity = entity;
   }

   @Inject(
      method = {"handleEntityMovement", "handleEntityTeleport"},
      at = {@At("RETURN")}
   )
   private void resetEntityField(CallbackInfo ci) {
      this.entity = null;
   }

   @ModifyArg(
      method = {"handleEntityMovement", "handleEntityTeleport"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;setPositionAndRotation2(DDDFFIZ)V"
      ),
      index = 3
   )
   private float captureTrackedYaw(float value) {
      ((EntityExt)this.entity).replaymod$setTrackedYaw(value);
      return value;
   }

   @ModifyArg(
      method = {"handleEntityMovement", "handleEntityTeleport"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;setPositionAndRotation2(DDDFFIZ)V"
      ),
      index = 4
   )
   private float captureTrackedPitch(float value) {
      ((EntityExt)this.entity).replaymod$setTrackedPitch(value);
      return value;
   }
}
