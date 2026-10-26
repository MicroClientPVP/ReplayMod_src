package com.replaymod.recording.handler;

import com.replaymod.core.events.PreRenderCallback;
import com.replaymod.core.versions.MCVer;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PreTickCallback;
import com.replaymod.recording.mixin.IntegratedServerAccessor;
import com.replaymod.recording.packet.PacketListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.BlockPos;

public class RecordingEventHandler extends EventRegistrations {
   private final Minecraft mc = MCVer.getMinecraft();
   private final PacketListener packetListener;
   private Double lastX;
   private Double lastY;
   private Double lastZ;
   private static final int EQUIPMENT_SLOTS = 5;
   private final List<ItemStack> playerItems = Arrays.asList();
   private int ticksSinceLastCorrection;
   private boolean wasSleeping;
   private int lastRiding = -1;
   private Integer rotationYawHeadBefore;

   public RecordingEventHandler(PacketListener packetListener) {
      this.on(PreTickCallback.EVENT, this::onPlayerTick);
      this.on(PreRenderCallback.EVENT, this::checkForGamePaused);
      this.packetListener = packetListener;
   }

   @Override
   public void register() {
      super.register();
      ((RecordingEventHandler.RecordingEventSender)this.mc.renderGlobal).setRecordingEventHandler(this);
   }

   @Override
   public void unregister() {
      super.unregister();
      RecordingEventHandler.RecordingEventSender recordingEventSender = (RecordingEventHandler.RecordingEventSender)this.mc.renderGlobal;
      if (recordingEventSender.getRecordingEventHandler() == this) {
         recordingEventSender.setRecordingEventHandler(null);
      }
   }

   public void spawnRecordingPlayer() {
      try {
         EntityPlayerSP player = this.mc.thePlayer;

         assert player != null;

         this.packetListener.save(new S0CPacketSpawnPlayer(player));
         this.lastX = this.lastY = this.lastZ = null;
         Collections.fill(this.playerItems, null);
         this.lastRiding = -1;
         this.wasSleeping = false;
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private void onPlayerTick() {
      if (this.mc.thePlayer != null) {
         EntityPlayerSP player = this.mc.thePlayer;

         try {
            boolean force = false;
            if (this.lastX == null || this.lastY == null || this.lastZ == null) {
               force = true;
               this.lastX = player.posX;
               this.lastY = player.posY;
               this.lastZ = player.posZ;
            }

            this.ticksSinceLastCorrection++;
            if (this.ticksSinceLastCorrection >= 100) {
               this.ticksSinceLastCorrection = 0;
               force = true;
            }

            double dx = player.posX - this.lastX;
            double dy = player.posY - this.lastY;
            double dz = player.posZ - this.lastZ;
            this.lastX = player.posX;
            this.lastY = player.posY;
            this.lastZ = player.posZ;
            double maxRelDist = 4.0;
            Packet packet;
            if (!force && !(Math.abs(dx) > 4.0) && !(Math.abs(dy) > 4.0) && !(Math.abs(dz) > 4.0)) {
               byte newYaw = (byte)((int)(player.rotationYaw * 256.0F / 360.0F));
               byte newPitch = (byte)((int)(player.rotationPitch * 256.0F / 360.0F));
               packet = new S17PacketEntityLookMove(
                  player.getEntityId(),
                  (byte)((int)Math.round(dx * 32.0)),
                  (byte)((int)Math.round(dy * 32.0)),
                  (byte)((int)Math.round(dz * 32.0)),
                  newYaw,
                  newPitch,
                  player.onGround
               );
            } else {
               packet = new S18PacketEntityTeleport(player);
            }

            this.packetListener.save(packet);
            int rotationYawHead = (int)(player.rotationYawHead * 256.0F / 360.0F);
            if (!Objects.equals(rotationYawHead, this.rotationYawHeadBefore)) {
               this.packetListener.save(new S19PacketEntityHeadLook(player, (byte)rotationYawHead));
               this.rotationYawHeadBefore = rotationYawHead;
            }

            this.packetListener.save(new S12PacketEntityVelocity(player.getEntityId(), player.motionX, player.motionY, player.motionZ));
            if (player.isSwingInProgress && player.swingProgressInt == 0) {
               this.packetListener.save(new S0BPacketAnimation(player, 0));
            }

            for (int slot = 0; slot < 5; slot++) {
               ItemStack stack = player.getEquipmentInSlot(slot);
               if (!ItemStack.areItemStacksEqual(this.playerItems.get(slot), stack)) {
                  stack = stack != null ? stack.copy() : null;
                  this.playerItems.set(slot, stack);
                  this.packetListener.save(new S04PacketEntityEquipment(player.getEntityId(), slot, stack));
               }
            }

            Entity vehicle = player.ridingEntity;
            int vehicleId = vehicle == null ? -1 : vehicle.getEntityId();
            if (this.lastRiding != vehicleId) {
               this.lastRiding = vehicleId;
               this.packetListener.save(new S1BPacketEntityAttach(0, player, vehicle));
            }

            if (!player.isPlayerSleeping() && this.wasSleeping) {
               this.packetListener.save(new S0BPacketAnimation(player, 2));
               this.wasSleeping = false;
            }
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      }
   }

   public void onBlockBreakAnim(int breakerId, BlockPos pos, int progress) {
      EntityPlayer thePlayer = this.mc.thePlayer;
      if (thePlayer != null && breakerId == thePlayer.getEntityId()) {
         this.packetListener.save(new S25PacketBlockBreakAnim(breakerId, pos, progress));
      }
   }

   private void checkForGamePaused() {
      if (this.mc.isSingleplayer()) {
         IntegratedServer server = this.mc.getIntegratedServer();
         if (server != null && ((IntegratedServerAccessor)server).isGamePaused()) {
            this.packetListener.setServerWasPaused();
         }
      }
   }

   public interface RecordingEventSender {
      void setRecordingEventHandler(RecordingEventHandler var1);

      RecordingEventHandler getRecordingEventHandler();
   }
}
