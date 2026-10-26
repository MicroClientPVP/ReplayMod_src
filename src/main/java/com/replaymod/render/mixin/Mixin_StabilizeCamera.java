package com.replaymod.render.mixin;

import com.replaymod.core.versions.MCVer;
import com.replaymod.render.RenderSettings;
import com.replaymod.render.hooks.EntityRendererHandler;
import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class Mixin_StabilizeCamera {
   private float orgYaw;
   private float orgPitch;
   private float orgPrevYaw;
   private float orgPrevPitch;
   private float orgRoll;
   private float orgHeadYaw;
   private float orgPrevHeadYaw;

   private EntityRendererHandler getHandler() {
      return ((EntityRendererHandler.IEntityRenderer)MCVer.getMinecraft().entityRenderer).replayModRender_getHandler();
   }

   @Inject(
      method = {"setupCameraTransform"},
      at = {@At("HEAD")}
   )
   private void replayModRender_beforeSetupCameraTransform(float partialTicks, int renderPass, CallbackInfo ci) {
      if (this.getHandler() != null) {
         Entity entity = MCVer.getMinecraft().getRenderViewEntity();
         this.orgYaw = entity.rotationYaw;
         this.orgPitch = entity.rotationPitch;
         this.orgPrevYaw = entity.prevRotationYaw;
         this.orgPrevPitch = entity.prevRotationPitch;
         this.orgRoll = entity instanceof CameraEntity ? ((CameraEntity)entity).roll : 0.0F;
         if (entity instanceof EntityLivingBase) {
            this.orgHeadYaw = ((EntityLivingBase)entity).rotationYawHead;
            this.orgPrevHeadYaw = ((EntityLivingBase)entity).prevRotationYawHead;
         }
      }
   }

   @Inject(
      method = {"orientCamera"},
      at = {@At("HEAD")}
   )
   private void replayModRender_resetRotationIfNeeded(float partialTicks, CallbackInfo ci) {
      if (this.getHandler() != null) {
         Entity entity = MCVer.getMinecraft().getRenderViewEntity();
         RenderSettings settings = this.getHandler().getSettings();
         if (settings.isStabilizeYaw()) {
            entity.prevRotationYaw = 0.0F;
            entity.rotationYaw = 0.0F;
            if (entity instanceof EntityLivingBase) {
               ((EntityLivingBase)entity).prevRotationYawHead = ((EntityLivingBase)entity).rotationYawHead = 0.0F;
            }
         }

         if (settings.isStabilizePitch()) {
            entity.prevRotationPitch = 0.0F;
            entity.rotationPitch = 0.0F;
         }

         if (settings.isStabilizeRoll() && entity instanceof CameraEntity) {
            ((CameraEntity)entity).roll = 0.0F;
         }
      }
   }

   @Inject(
      method = {"setupCameraTransform"},
      at = {@At("RETURN")}
   )
   private void replayModRender_afterSetupCameraTransform(float partialTicks, int renderPass, CallbackInfo ci) {
      if (this.getHandler() != null) {
         Entity entity = MCVer.getMinecraft().getRenderViewEntity();
         entity.rotationYaw = this.orgYaw;
         entity.rotationPitch = this.orgPitch;
         entity.prevRotationYaw = this.orgPrevYaw;
         entity.prevRotationPitch = this.orgPrevPitch;
         if (entity instanceof CameraEntity) {
            ((CameraEntity)entity).roll = this.orgRoll;
         }

         if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).rotationYawHead = this.orgHeadYaw;
            ((EntityLivingBase)entity).prevRotationYawHead = this.orgPrevHeadYaw;
         }
      }
   }
}
