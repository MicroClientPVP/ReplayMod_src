package com.replaymod.recording.gui;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.lib.de.johni0702.minecraft.gui.MinecraftGuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.MatrixStack;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.RenderHudCallback;
import com.replaymod.recording.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiRecordingOverlay extends EventRegistrations {
   private final Minecraft mc;
   private final SettingsRegistry settingsRegistry;
   private final GuiRecordingControls guiControls;

   public GuiRecordingOverlay(Minecraft mc, SettingsRegistry settingsRegistry, GuiRecordingControls guiControls) {
      this.on(RenderHudCallback.EVENT, (stack, partialTicks) -> this.renderRecordingIndicator(stack));
      this.mc = mc;
      this.settingsRegistry = settingsRegistry;
      this.guiControls = guiControls;
   }

   private void renderRecordingIndicator(MatrixStack stack) {
      if (!this.guiControls.isStopped()) {
         if (this.settingsRegistry.get(Setting.INDICATOR)) {
            FontRenderer fontRenderer = this.mc.fontRendererObj;
            String text = this.guiControls.isPaused()
               ? I18n.format("replaymod.gui.paused", new Object[0])
               : I18n.format("replaymod.gui.recording", new Object[0]);
            MinecraftGuiRenderer renderer = new MinecraftGuiRenderer(stack);
            renderer.drawString(30, 18 - fontRenderer.FONT_HEIGHT / 2, -1, text.toUpperCase());
            renderer.bindTexture(ReplayMod.TEXTURE);
            GlStateManager.enableAlpha();
            renderer.drawTexturedRect(10, 10, 58, 20, 16, 16, 16, 16, 256, 256);
         }
      }
   }
}
