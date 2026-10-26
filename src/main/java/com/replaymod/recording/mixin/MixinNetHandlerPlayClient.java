package com.replaymod.recording.mixin;

import com.replaymod.core.versions.MCVer;
import com.replaymod.recording.handler.RecordingEventHandler;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S38PacketPlayerListItem.Action;
import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetHandlerPlayClient.class})
public abstract class MixinNetHandlerPlayClient {
   private static Minecraft mcStatic = MCVer.getMinecraft();
   @Shadow
   private Map<UUID, NetworkPlayerInfo> playerInfoMap;

   public RecordingEventHandler getRecordingEventHandler() {
      if (mcStatic.renderGlobal instanceof RecordingEventHandler.RecordingEventSender) {
         return ((RecordingEventHandler.RecordingEventSender)mcStatic.renderGlobal).getRecordingEventHandler();
      }
      return null;
   }

   @Inject(
      method = {"handlePlayerListItem"},
      at = {@At("HEAD")}
   )
   public void recordOwnJoin(S38PacketPlayerListItem packet, CallbackInfo ci) {
      if (mcStatic.isCallingFromMinecraftThread()) {
         if (mcStatic.thePlayer != null) {
            RecordingEventHandler handler = this.getRecordingEventHandler();
            if (handler != null && packet.getAction() == Action.ADD_PLAYER) {
               for (AddPlayerData entry : packet.getEntries()) {
                  UUID uuid = entry.getProfile().getId();
                  if (uuid.equals(mcStatic.thePlayer.getGameProfile().getId()) && !this.playerInfoMap.containsKey(uuid)) {
                     handler.spawnRecordingPlayer();
                  }
               }
            }
         }
      }
   }

   @Inject(
      method = {"handleRespawn"},
      at = {@At("RETURN")}
   )
   public void recordOwnRespawn(S07PacketRespawn packet, CallbackInfo ci) {
      RecordingEventHandler handler = this.getRecordingEventHandler();
      if (handler != null) {
         handler.spawnRecordingPlayer();
      }
   }
}
