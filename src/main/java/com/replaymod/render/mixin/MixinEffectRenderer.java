package com.replaymod.render.mixin;

import com.replaymod.render.blend.BlendState;
import com.replaymod.render.blend.exporters.ParticlesExporter;
import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({EffectRenderer.class})
public abstract class MixinEffectRenderer {
   @Redirect(
      method = {"renderParticles"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/EntityFX;renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V"
      )
   )
   private void renderNormalParticle(
      EntityFX fx, WorldRenderer worldRenderer, Entity view, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY
   ) {
      this.renderParticle(fx, worldRenderer, view, partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
   }

   @Redirect(
      method = {"renderLitParticles"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/EntityFX;renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V"
      )
   )
   private void renderLitParticle(
      EntityFX fx, WorldRenderer worldRenderer, Entity view, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY
   ) {
      this.renderParticle(fx, worldRenderer, view, partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
   }

   private void renderParticle(
      EntityFX fx, WorldRenderer worldRenderer, Entity view, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY
   ) {
      EntityRendererHandler handler = ((EntityRendererHandler.IEntityRenderer)Minecraft.getMinecraft().entityRenderer).replayModRender_getHandler();
      if (handler != null && handler.omnidirectional) {
         double dx = fx.prevPosX + (fx.posX - fx.prevPosX) * (double)partialTicks - view.posX;
         double dy = fx.prevPosY + (fx.posY - fx.prevPosY) * (double)partialTicks - view.posY;
         double dz = fx.prevPosZ + (fx.posZ - fx.prevPosZ) * (double)partialTicks - view.posZ;
         double pitch = -Math.atan2(dy, Math.sqrt(dx * dx + dz * dz));
         double yaw = -Math.atan2(dx, dz);
         rotX = (float)Math.cos(yaw);
         rotZ = (float)Math.sin(yaw);
         rotXZ = (float)Math.cos(pitch);
         rotYZ = (float)((double)(-rotZ) * Math.sin(pitch));
         rotXY = (float)((double)rotX * Math.sin(pitch));
      }

      BlendState blendState = BlendState.getState();
      if (blendState != null) {
         blendState.get(ParticlesExporter.class).onRender(fx, partialTicks);
      }

      fx.renderParticle(worldRenderer, view, partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
   }
}
