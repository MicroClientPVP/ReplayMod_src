package com.replaymod.core.mixin;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Timer.class})
public interface TimerAccessor {
   @Accessor
   float getTimerSpeed();

   @Accessor
   void setTimerSpeed(float var1);
}
