package com.replaymod.lib.de.johni0702.minecraft.gui.versions.forge;

import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.MatrixStack;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.InitScreenCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.OpenGuiScreenCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PostRenderScreenCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PreTickCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.RenderHudCallback;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class EventsAdapter extends EventRegistrations {
   public static GuiScreen getScreen(GuiScreenEvent event) {
      return event.gui;
   }

   public static List<GuiButton> getButtonList(InitGuiEvent event) {
      return event.buttonList;
   }

   @SubscribeEvent
   public void preGuiInit(Pre event) {
      InitScreenCallback.Pre.EVENT.invoker().preInitScreen(getScreen(event));
   }

   @SubscribeEvent
   public void onGuiInit(Post event) {
      InitScreenCallback.EVENT.invoker().initScreen(getScreen(event), getButtonList(event));
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void onGuiClosed(GuiOpenEvent event) {
      OpenGuiScreenCallback.EVENT.invoker().openGuiScreen(event.gui);
   }

   public static float getPartialTicks(RenderGameOverlayEvent event) {
      return event.partialTicks;
   }

   public static float getPartialTicks(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event) {
      return event.renderPartialTicks;
   }

   @SubscribeEvent
   public void onGuiRender(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event) {
      PostRenderScreenCallback.EVENT.invoker().postRenderScreen(new MatrixStack(), getPartialTicks(event));
   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void renderOverlay(Text event) {
      RenderHudCallback.EVENT.invoker().renderHud(new MatrixStack(), getPartialTicks(event));
   }

   @SubscribeEvent
   public void tickOverlay(ClientTickEvent event) {
      if (event.phase == Phase.START) {
         PreTickCallback.EVENT.invoker().preTick();
      }
   }
}
