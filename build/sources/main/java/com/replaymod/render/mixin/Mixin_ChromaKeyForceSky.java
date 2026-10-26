package com.replaymod.render.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({EntityRenderer.class})
public abstract class Mixin_ChromaKeyForceSky {
   @Shadow
   @Final
   private Minecraft mc;

   @ModifyConstant(
      method = {"renderWorldPass"},
      constant = {@Constant(
         intValue = 4
      )}
   )
   private int forceSkyWhenChromaKeying(int value) {
      EntityRendererHandler handler = ((EntityRendererHandler.IEntityRenderer)this.mc.entityRenderer).replayModRender_getHandler();
      if (handler != null) {
         ReadableColor color = handler.getSettings().getChromaKeyingColor();
         if (color != null) {
            return 0;
         }
      }

      return value;
   }
}
