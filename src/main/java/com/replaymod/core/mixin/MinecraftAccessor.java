package com.replaymod.core.mixin;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Minecraft.class})
public interface MinecraftAccessor {
   @Accessor
   Timer getTimer();

   @Accessor
   void setTimer(Timer var1);

   @Accessor
   Queue<FutureTask<?>> getScheduledTasks();

   @Accessor
   CrashReport getCrashReporter();

   @Accessor
   List<IResourcePack> getDefaultResourcePacks();

   @org.spongepowered.asm.mixin.gen.Invoker("resize")
   void callResize(int width, int height);
}
