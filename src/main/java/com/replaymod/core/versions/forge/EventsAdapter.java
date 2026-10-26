package com.replaymod.core.versions.forge;

import com.replaymod.core.events.KeyBindingEventCallback;
import com.replaymod.core.events.PostRenderCallback;
import com.replaymod.core.events.PostRenderWorldCallback;
import com.replaymod.core.events.PreRenderCallback;
import com.replaymod.core.events.PreRenderHandCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.MatrixStack;
import com.replaymod.replay.events.RenderHotbarCallback;
import com.replaymod.replay.events.RenderSpectatorCrosshairCallback;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class EventsAdapter extends EventRegistrations {
   @SubscribeEvent
   public void onKeyEvent(KeyInputEvent event) {
      KeyBindingEventCallback.EVENT.invoker().onKeybindingEvent();
   }

   @SubscribeEvent
   public void onMouseInput(MouseInputEvent event) {
      KeyBindingEventCallback.EVENT.invoker().onKeybindingEvent();
   }

   @SubscribeEvent
   public void preRender(RenderTickEvent event) {
      if (event.phase == Phase.START) {
         PreRenderCallback.EVENT.invoker().preRender();
      }
   }

   @SubscribeEvent
   public void postRender(RenderTickEvent event) {
      if (event.phase == Phase.END) {
         PostRenderCallback.EVENT.invoker().postRender();
      }
   }

   @SubscribeEvent
   public void renderCameraPath(RenderWorldLastEvent event) {
      PostRenderWorldCallback.EVENT.invoker().postRenderWorld(new MatrixStack());
   }

   @SubscribeEvent
   public void oRenderHand(RenderHandEvent event) {
      if (PreRenderHandCallback.EVENT.invoker().preRenderHand()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public void preRenderGameOverlay(Pre event) {
      Boolean result = null;
      switch (event.type) {
         case CROSSHAIRS:
            result = RenderSpectatorCrosshairCallback.EVENT.invoker().shouldRenderSpectatorCrosshair();
            break;
         case HOTBAR:
            result = RenderHotbarCallback.EVENT.invoker().shouldRenderHotbar();
      }

      if (result == Boolean.FALSE) {
         event.setCanceled(true);
      }
   }
}
