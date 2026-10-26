package com.replaymod.lib.de.johni0702.minecraft.gui.versions.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.versions.ScreenExt;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({GuiScreen.class})
public abstract class Mixin_PassEventsExt implements ScreenExt {
   @Shadow
   public boolean allowUserInput;

   @Override
   public boolean doesPassEvents() {
      return this.allowUserInput;
   }

   @Override
   public void setPassEvents(boolean passEvents) {
      this.allowUserInput = passEvents;
   }
}
