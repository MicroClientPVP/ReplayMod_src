package com.replaymod.core.versions.scheduler;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.replaymod.core.mixin.MinecraftAccessor;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ReportedException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class SchedulerImpl implements Scheduler {
   private static final Minecraft mc = Minecraft.getMinecraft();
   private static final EventBus FML_BUS = MinecraftForge.EVENT_BUS;
   private boolean inRunLater = false;

   @Override
   public void runSync(Runnable runnable) throws InterruptedException, ExecutionException, TimeoutException {
      if (mc.isCallingFromMinecraftThread()) {
         runnable.run();
      } else {
         FutureTask<Void> future = new FutureTask<>(runnable, null);
         this.runLater(future);
         future.get(30L, TimeUnit.SECONDS);
      }
   }

   @Override
   public void runPostStartup(Runnable runnable) {
      this.runLater(runnable);
   }

   @Override
   public void runLaterWithoutLock(Runnable runnable) {
      this.runLater(() -> this.runLaterWithoutLock(runnable), runnable);
   }

   @Override
   public void runLater(Runnable runnable) {
      this.runLater(runnable, () -> this.runLater(runnable));
   }

   private void runLater(Runnable runnable, Runnable defer) {
      if (mc.isCallingFromMinecraftThread() && this.inRunLater) {
         FML_BUS.register(new Object() {
            @SubscribeEvent
            public void onRenderTick(RenderTickEvent event) {
               if (event.phase == Phase.START) {
                  SchedulerImpl.FML_BUS.unregister(this);
                  defer.run();
               }
            }
         });
      } else {
         Queue<FutureTask<?>> tasks = ((MinecraftAccessor)mc).getScheduledTasks();
         synchronized (tasks) {
            tasks.add(ListenableFutureTask.create(() -> {
               this.inRunLater = true;

               try {
                  runnable.run();
               } catch (ReportedException var6) {
                  var6.printStackTrace();
                  System.err.println(var6.getCrashReport().getCompleteReport());
                  mc.crashed(var6.getCrashReport());
               } finally {
                  this.inRunLater = false;
               }
            }, null));
         }
      }
   }

   @Override
   public void runTasks() {
   }
}
