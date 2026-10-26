package com.replaymod.core.mixin;

import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({GuiScreen.class})
public interface GuiScreenAccessor {
   @Accessor("buttonList")
   List<GuiButton> getButtons();
}
