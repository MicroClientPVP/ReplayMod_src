package com.replaymod.recording;

import com.replaymod.core.KeyBindingRegistry;
import com.replaymod.core.Module;
import com.replaymod.core.ReplayMod;
import com.replaymod.recording.handler.ConnectionEventHandler;
import com.replaymod.recording.handler.GuiHandler;
import com.replaymod.recording.mixin.NetworkManagerAccessor;
import com.replaymod.recording.packet.PacketListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReplayModRecording implements Module {
   private static final Logger LOGGER = LogManager.getLogger();
   public static ReplayModRecording instance;
   private ReplayMod core;
   private ConnectionEventHandler connectionEventHandler;

   public ReplayModRecording(ReplayMod mod) {
      instance = this;
      this.core = mod;
      this.core.getSettingsRegistry().register(Setting.class);
   }

   @Override
   public void registerKeyBindings(KeyBindingRegistry registry) {
      registry.registerKeyBinding("replaymod.input.marker", 50, new Runnable() {
         @Override
         public void run() {
            PacketListener packetListener = ReplayModRecording.this.connectionEventHandler.getPacketListener();
            if (packetListener != null) {
               packetListener.addMarker(null);
               ReplayModRecording.this.core.printInfoToChat("replaymod.chat.addedmarker");
            }
         }
      }, false);
   }

   @Override
   public void initClient() {
      this.connectionEventHandler = new ConnectionEventHandler(LOGGER, this.core);
      new GuiHandler(this.core).register();
   }

   public void initiateRecording(NetworkManager networkManager) {
      if (this.connectionEventHandler == null) {
         return;
      }
      Channel channel = ((NetworkManagerAccessor)networkManager).getChannel();
      if (channel.pipeline().get("ReplayModReplay_packetHandler") == null) {
         this.connectionEventHandler.onConnectedToServerEvent(networkManager);
      }
   }

   public ConnectionEventHandler getConnectionEventHandler() {
      return this.connectionEventHandler;
   }

   @Sharable
   private static class RestrictionsChannelHandler extends ChannelDuplexHandler {
      private RestrictionsChannelHandler() {
      }
   }
}
