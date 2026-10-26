package com.replaymod.core.gui;

import com.replaymod.core.SettingsRegistry;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiPanel;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiToggleButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.advanced.GuiDropdownMenu;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.CustomLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.HorizontalLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.VerticalLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.Consumer;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.Dimension;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiReplaySettings extends AbstractGuiScreen<GuiReplaySettings> {
   public GuiReplaySettings(GuiScreen parent, SettingsRegistry settingsRegistry) {
      final GuiButton doneButton = new GuiButton(this).setI18nLabel("gui.done", new Object[0]).setSize(200, 20).onClick(new Runnable() {
         @Override
         public void run() {
            GuiReplaySettings.this.getMinecraft().displayGuiScreen(parent);
         }
      });
      final GuiPanel allElements = new GuiPanel(this).setLayout(new HorizontalLayout().setSpacing(10));
      GuiPanel leftColumn = new GuiPanel().setLayout(new VerticalLayout().setSpacing(4));
      GuiPanel rightColumn = new GuiPanel().setLayout(new VerticalLayout().setSpacing(4));
      allElements.addElements(new VerticalLayout.Data(0.0), new GuiElement[]{leftColumn, rightColumn});
      HorizontalLayout.Data leftHorizontalData = new HorizontalLayout.Data(1.0);
      HorizontalLayout.Data rightHorizontalData = new HorizontalLayout.Data(0.0);
      int i = 0;

      for (final SettingsRegistry.SettingKey<?> key : settingsRegistry.getSettings()) {
         if (key.getDisplayString() != null) {
            GuiElement<?> element;
            if (key.getDefault() instanceof Boolean) {
               @SuppressWarnings("unchecked")
               final SettingsRegistry.SettingKey<Boolean> boolKey = (SettingsRegistry.SettingKey<Boolean>)key;
               final GuiToggleButton button = new GuiToggleButton();
               button.setSize(150, 20);
               button.setI18nLabel(key.getDisplayString());
               button.setSelected(Boolean.TRUE.equals(settingsRegistry.get(boolKey)) ? 0 : 1);
               button.setValues(I18n.format("options.on"), I18n.format("options.off"));
               element = button.onClick(new Runnable() {
                  @Override
                  public void run() {
                     settingsRegistry.set(boolKey, button.getSelected() == 0);
                     settingsRegistry.save();
                  }
               });
            } else {
               if (!(key instanceof SettingsRegistry.MultipleChoiceSettingKey)) {
                  throw new IllegalArgumentException("Type " + key.getDefault().getClass() + " not supported.");
               }

               final SettingsRegistry.MultipleChoiceSettingKey<?> multipleChoiceKey = (SettingsRegistry.MultipleChoiceSettingKey<?>)key;
               List<?> values = multipleChoiceKey.getChoices();
               GuiReplaySettings.MultipleChoiceDropdownEntry[] entries = new GuiReplaySettings.MultipleChoiceDropdownEntry[values.size()];
               int selected = 0;
               Object currentValue = settingsRegistry.get(multipleChoiceKey);

               for (int j = 0; j < entries.length; j++) {
                  Object value = values.get(j);
                  entries[j] = new GuiReplaySettings.MultipleChoiceDropdownEntry(
                     value,
                     I18n.format(multipleChoiceKey.getDisplayString(), new Object[0]) + ": " + I18n.format(value.toString(), new Object[0])
                  );
                  if (currentValue.equals(value)) {
                     selected = j;
                  }
               }

               final GuiDropdownMenu<GuiReplaySettings.MultipleChoiceDropdownEntry> menu = new GuiDropdownMenu<GuiReplaySettings.MultipleChoiceDropdownEntry>() {
                  @Override
                  protected ReadableDimension calcMinSize() {
                     ReadableDimension size = super.calcMinSize();
                     return (size.getWidth() > 150 ? new Dimension(150, size.getHeight()) : size);
                  }
               };
               menu.setSize(150, 20);
               menu.setValues(entries);
               menu.setSelected(selected).onSelection(new Consumer<Integer>() {
                  public void consume(Integer obj) {
                     settingsRegistry.set((SettingsRegistry.SettingKey<Object>)multipleChoiceKey, menu.getSelectedValue().value);
                     settingsRegistry.save();
                  }
               });
               element = menu;
            }

            if (i++ % 2 == 0) {
               leftColumn.addElements(leftHorizontalData, new GuiElement[]{element});
            } else {
               rightColumn.addElements(rightHorizontalData, new GuiElement[]{element});
            }
         }
      }

      this.setLayout(new CustomLayout<GuiReplaySettings>() {
         protected void layout(GuiReplaySettings container, int width, int height) {
            this.pos(allElements, width / 2 - 155, height / 6);
            this.pos(doneButton, width / 2 - 100, height - 27);
         }
      });
      this.setTitle(new GuiLabel().setI18nText("replaymod.gui.settings.title", new Object[0]));
   }

   protected GuiReplaySettings getThis() {
      return this;
   }

   private static class MultipleChoiceDropdownEntry {
      private final Object value;
      private final String text;

      public MultipleChoiceDropdownEntry(Object value, String text) {
         this.value = value;
         this.text = text;
      }

      @Override
      public String toString() {
         return this.text;
      }
   }
}
