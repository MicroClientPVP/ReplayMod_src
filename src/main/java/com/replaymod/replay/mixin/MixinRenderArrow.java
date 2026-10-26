package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({RenderArrow.class})
public abstract class MixinRenderArrow extends Render {
   protected MixinRenderArrow(RenderManager renderManager) {
      super(renderManager);
   }

   public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ) {
      return ReplayModReplay.instance.getReplayHandler() != null || super.shouldRender(entity, camera, camX, camY, camZ);
   }
}
