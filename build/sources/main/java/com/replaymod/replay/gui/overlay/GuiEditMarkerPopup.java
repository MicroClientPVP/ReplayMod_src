package com.replaymod.replay.gui.overlay;

import com.google.common.base.Strings;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiContainer;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiPanel;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiNumberField;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiTextField;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.IGuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.Click;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.KeyHandler;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.KeyInput;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.GridLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.HorizontalLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.VerticalLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.popup.AbstractGuiPopup;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.Colors;
import com.replaymod.replaystudio.data.Marker;
import java.util.function.Consumer;

public class GuiEditMarkerPopup extends AbstractGuiPopup<GuiEditMarkerPopup> implements KeyHandler {
   private final Consumer<Marker> onSave;
   public final GuiLabel title = new GuiLabel().setI18nText("replaymod.gui.editkeyframe.title.marker", new Object[0]);
   public final GuiTextField nameField = new GuiTextField().setSize(150, 20);
   public final GuiNumberField timeField = newGuiNumberField().setPrecision(0);
   public final GuiNumberField xField = newGuiNumberField().setPrecision(10);
   public final GuiNumberField yField = newGuiNumberField().setPrecision(10);
   public final GuiNumberField zField = newGuiNumberField().setPrecision(10);
   public final GuiNumberField yawField = newGuiNumberField().setPrecision(5);
   public final GuiNumberField pitchField = newGuiNumberField().setPrecision(5);
   public final GuiNumberField rollField = newGuiNumberField().setPrecision(5);
   public final GuiPanel inputs = GuiPanel.builder()
      .layout(new GridLayout().setColumns(2).setSpacingX(7).setSpacingY(3))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.markername", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.nameField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.timestamp", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.timeField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.xpos", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.xField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.ypos", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.yField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.zpos", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.zField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.camyaw", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.yawField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.campitch", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.pitchField, new GridLayout.Data(1.0, 0.5))
      .with(new GuiLabel().setI18nText("replaymod.gui.editkeyframe.camroll", new Object[0]), new GridLayout.Data(0.0, 0.5))
      .with(this.rollField, new GridLayout.Data(1.0, 0.5))
      .build();
   public final GuiButton saveButton = new GuiButton().onClick(new Runnable() {
      @Override
      public void run() {
         Marker marker = new Marker();
         marker.setName(Strings.emptyToNull(GuiEditMarkerPopup.this.nameField.getText()));
         marker.setTime(GuiEditMarkerPopup.this.timeField.getInteger());
         marker.setX(GuiEditMarkerPopup.this.xField.getDouble());
         marker.setY(GuiEditMarkerPopup.this.yField.getDouble());
         marker.setZ(GuiEditMarkerPopup.this.zField.getDouble());
         marker.setYaw(GuiEditMarkerPopup.this.yawField.getFloat());
         marker.setPitch(GuiEditMarkerPopup.this.pitchField.getFloat());
         marker.setRoll(GuiEditMarkerPopup.this.rollField.getFloat());
         GuiEditMarkerPopup.this.onSave.accept(marker);
         GuiEditMarkerPopup.this.close();
      }
   }).setSize(150, 20).setI18nLabel("replaymod.gui.save", new Object[0]);
   public final GuiButton cancelButton = new GuiButton().onClick(new Runnable() {
      @Override
      public void run() {
         GuiEditMarkerPopup.this.close();
      }
   }).setSize(150, 20).setI18nLabel("replaymod.gui.cancel", new Object[0]);
   public final GuiPanel buttons = new GuiPanel()
      .setLayout(new HorizontalLayout(HorizontalLayout.Alignment.CENTER).setSpacing(7))
      .addElements(new HorizontalLayout.Data(0.5), new GuiElement[]{this.saveButton, this.cancelButton});

   private static GuiNumberField newGuiNumberField() {
      return new GuiNumberField().setSize(150, 20).setValidateOnFocusChange(true);
   }

   public GuiEditMarkerPopup(GuiContainer container, Marker marker, Consumer<Marker> onSave) {
      super(container);
      this.onSave = onSave;
      this.setBackgroundColor(Colors.DARK_TRANSPARENT);
      this.popup
         .setLayout(new VerticalLayout().setSpacing(5))
         .addElements(new VerticalLayout.Data(0.5), new GuiElement[]{this.title, this.inputs, this.buttons});
      this.popup.invokeAll(IGuiLabel.class, e -> e.setColor(Colors.BLACK));
      this.nameField.setText(Strings.nullToEmpty(marker.getName()));
      this.timeField.setValue(marker.getTime());
      this.xField.setValue(marker.getX());
      this.yField.setValue(marker.getY());
      this.zField.setValue(marker.getZ());
      this.yawField.setValue((double)marker.getYaw());
      this.pitchField.setValue((double)marker.getPitch());
      this.rollField.setValue((double)marker.getRoll());
   }

   @Override
   public void open() {
      super.open();
   }

   protected GuiEditMarkerPopup getThis() {
      return this;
   }

   @Override
   public boolean handleKey(KeyInput keyInput) {
      if (keyInput.isEscape()) {
         this.cancelButton.onClick(new Click(-1, -1, 0, keyInput.modifiers));
         return true;
      } else {
         return false;
      }
   }
}
