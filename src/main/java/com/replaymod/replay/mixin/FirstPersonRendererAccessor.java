package com.replaymod.replay.mixin;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ItemRenderer.class})
public interface FirstPersonRendererAccessor {
   @Accessor
   void setItemToRender(ItemStack var1);

   @Accessor
   void setEquippedItemSlot(int var1);

   @Accessor
   void setEquippedProgress(float var1);

   @Accessor
   void setPrevEquippedProgress(float var1);
}
