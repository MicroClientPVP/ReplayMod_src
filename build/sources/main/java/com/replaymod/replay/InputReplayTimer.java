package com.replaymod.replay;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.versions.MCVer;
import com.replaymod.replay.camera.CameraController;
import com.replaymod.replay.camera.CameraEntity;
import java.io.IOException;
import net.minecraft.client.Minecraft;

public class InputReplayTimer {
   public static void updateInReplay() {
      ReplayModReplay mod = ReplayModReplay.instance;
      Minecraft mc = mod.getCore().getMinecraft();
      ReplayMod.instance.runTasks();
      if (mod.getReplayHandler() != null && mc.theWorld != null && mc.thePlayer != null) {
         if (mc.currentScreen != null) {
            try {
               mc.currentScreen.handleInput();
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }

         if (mc.currentScreen == null || mc.currentScreen.allowUserInput) {
            ((MCVer.MinecraftMethodAccessor)mc).replayModSetEarlyReturnFromRunTick(true);

            try {
               mc.runTick();
            } catch (IOException var3) {
               var3.printStackTrace();
            }

            ((MCVer.MinecraftMethodAccessor)mc).replayModSetEarlyReturnFromRunTick(false);
         }
      }
   }

   public static void handleScroll(int wheel) {
      if (wheel != 0) {
         ReplayHandler replayHandler = ReplayModReplay.instance.getReplayHandler();
         if (replayHandler != null) {
            CameraEntity cameraEntity = replayHandler.getCameraEntity();
            if (cameraEntity != null) {
               CameraController controller;
               for (controller = cameraEntity.getCameraController(); wheel > 0; wheel--) {
                  controller.increaseSpeed();
               }

               while (wheel < 0) {
                  controller.decreaseSpeed();
                  wheel++;
               }
            }
         }
      }
   }
}
