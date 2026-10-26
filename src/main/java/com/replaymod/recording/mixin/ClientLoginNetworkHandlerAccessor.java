package com.replaymod.recording.mixin;

import net.minecraft.client.network.NetHandlerLoginClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({NetHandlerLoginClient.class})
public interface ClientLoginNetworkHandlerAccessor {
}
