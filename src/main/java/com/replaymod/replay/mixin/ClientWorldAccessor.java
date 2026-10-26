package com.replaymod.replay.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({WorldClient.class})
public interface ClientWorldAccessor {
   @org.spongepowered.asm.mixin.gen.Invoker("onEntityRemoved")
   void callOnEntityRemoved(net.minecraft.entity.Entity entity);
}
