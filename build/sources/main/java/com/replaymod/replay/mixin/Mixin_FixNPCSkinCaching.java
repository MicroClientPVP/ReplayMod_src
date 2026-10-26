package com.replaymod.replay.mixin;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractClientPlayer.class})
public abstract class Mixin_FixNPCSkinCaching {
   @Shadow
   @Nullable
   protected abstract NetworkPlayerInfo getPlayerInfo();

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void forceCachePlayerListEntry(CallbackInfo ci) {
      if ((Object)this instanceof EntityOtherPlayerMP) {
         if (Minecraft.getMinecraft().getNetHandler() != null) {
            try {
               this.getPlayerInfo();
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }
      }
   }
}
