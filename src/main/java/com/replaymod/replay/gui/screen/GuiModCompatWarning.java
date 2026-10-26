package com.replaymod.replay.gui.screen;

import com.replaymod.core.utils.ModCompat;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiPanel;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiVerticalList;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.CustomLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.HorizontalLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.VerticalLayout;
import com.replaymod.replaystudio.data.ModInfo;
import com.replaymod.replaystudio.util.I18n;
import java.util.Map.Entry;

public class GuiModCompatWarning extends AbstractGuiScreen<GuiModCompatWarning> {
   public final GuiVerticalList content = new GuiVerticalList(this).setDrawShadow(true).setDrawSlider(true);
   public final GuiButton loadButton = new GuiButton().setI18nLabel("replaymod.gui.load", new Object[0]).setSize(200, 20);
   public final GuiButton cancelButton = new GuiButton().setI18nLabel("gui.cancel", new Object[0]).setSize(200, 20);
   public final GuiPanel closeButtons = new GuiPanel(this)
      .setLayout(new HorizontalLayout().setSpacing(5))
      .addElements(null, new GuiElement[]{this.loadButton, this.cancelButton});

   public GuiModCompatWarning(ModCompat.ModInfoDifference difference) {
      this.setTitle(new GuiLabel().setI18nText("replaymod.gui.modwarning.title", new Object[0]));
      this.setLayout(
         new CustomLayout<GuiModCompatWarning>() {
            protected void layout(GuiModCompatWarning container, int width, int height) {
               this.pos(GuiModCompatWarning.this.content, 10, 35);
               this.pos(
                  GuiModCompatWarning.this.closeButtons,
                  width / 2 - this.width(GuiModCompatWarning.this.closeButtons) / 2,
                  height - 10 - this.height(GuiModCompatWarning.this.closeButtons)
               );
               this.size(
                  GuiModCompatWarning.this.content, width - 20, this.y(GuiModCompatWarning.this.closeButtons) - 10 - this.y(GuiModCompatWarning.this.content)
               );
            }
         }
      );
      this.content.getListLayout().setSpacing(8);
      VerticalLayout.Data data = new VerticalLayout.Data(0.5);
      GuiPanel content = this.content.getListPanel();
      content.addElements(data, new GuiElement[]{new GuiLabel().setI18nText("replaymod.gui.modwarning.message1", new Object[0])});
      content.addElements(data, new GuiElement[]{new GuiLabel().setI18nText("replaymod.gui.modwarning.message2", new Object[0])});
      content.addElements(data, new GuiElement[]{new GuiLabel().setI18nText("replaymod.gui.modwarning.message3", new Object[0])});
      if (!difference.getMissing().isEmpty()) {
         content.addElements(data, new GuiElement[]{new GuiLabel()});
         content.addElements(data, new GuiElement[]{new GuiLabel().setI18nText("replaymod.gui.modwarning.missing", new Object[0])});

         for (ModInfo modInfo : difference.getMissing()) {
            content.addElements(
               data,
               new GuiElement[]{
                  new GuiPanel()
                     .setLayout(new VerticalLayout().setSpacing(3))
                     .addElements(
                        null,
                        new GuiElement[]{
                           GuiPanel.builder()
                              .layout(new HorizontalLayout().setSpacing(3))
                              .with(new GuiLabel().setI18nText("replaymod.gui.modwarning.name", new Object[0]), null)
                              .with(new GuiLabel().setText(modInfo.getName()), null)
                              .build(),
                           GuiPanel.builder()
                              .layout(new HorizontalLayout().setSpacing(3))
                              .with(new GuiLabel().setI18nText("replaymod.gui.modwarning.id", new Object[0]), null)
                              .with(new GuiLabel().setText(modInfo.getId()), null)
                              .build(),
                           new GuiLabel().setText(I18n.format("replaymod.gui.modwarning.version.expected", new Object[0]) + ": " + modInfo.getVersion())
                        }
                     )
               }
            );
         }
      }

      if (!difference.getDiffering().isEmpty()) {
         content.addElements(data, new GuiElement[]{new GuiLabel()});
         content.addElements(data, new GuiElement[]{new GuiLabel().setI18nText("replaymod.gui.modwarning.version", new Object[0])});

         for (Entry<ModInfo, String> entry : difference.getDiffering().entrySet()) {
            content.addElements(
               data,
               new GuiElement[]{
                  new GuiPanel()
                     .setLayout(new VerticalLayout().setSpacing(3))
                     .addElements(
                        null,
                        new GuiElement[]{
                           GuiPanel.builder()
                              .layout(new HorizontalLayout().setSpacing(3))
                              .with(new GuiLabel().setI18nText("replaymod.gui.modwarning.name", new Object[0]), null)
                              .with(new GuiLabel().setText(entry.getKey().getName()), null)
                              .build(),
                           GuiPanel.builder()
                              .layout(new HorizontalLayout().setSpacing(3))
                              .with(new GuiLabel().setI18nText("replaymod.gui.modwarning.id", new Object[0]), null)
                              .with(new GuiLabel().setText(entry.getKey().getId()), null)
                              .build(),
                           new GuiLabel()
                              .setText(
                                 I18n.format("replaymod.gui.modwarning.version.expected", new Object[0])
                                    + ": "
                                    + entry.getKey().getVersion()
                                    + ", "
                                    + I18n.format("replaymod.gui.modwarning.version.found", new Object[0])
                                    + ": "
                                    + entry.getValue()
                              )
                        }
                     )
               }
            );
         }
      }

      this.cancelButton.onClick(() -> this.getMinecraft().displayGuiScreen(null));
   }

   protected GuiModCompatWarning getThis() {
      return this;
   }
}
