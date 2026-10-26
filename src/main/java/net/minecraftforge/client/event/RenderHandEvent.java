package net.minecraftforge.client.event;
import net.minecraftforge.fml.common.eventhandler.Event;
public class RenderHandEvent extends Event {
    public final float partialTicks;
    public final int renderPass;
    public RenderHandEvent(float partialTicks, int renderPass) { this.partialTicks = partialTicks; this.renderPass = renderPass; }
}