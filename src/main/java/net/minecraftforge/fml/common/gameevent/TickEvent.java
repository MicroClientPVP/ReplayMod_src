package net.minecraftforge.fml.common.gameevent;
import net.minecraftforge.fml.common.eventhandler.Event;
public class TickEvent extends Event {
    public final Phase phase;
    public TickEvent(Phase phase) { this.phase = phase; }
    public enum Phase { START, END }
    public static class ClientTickEvent extends TickEvent {
        public ClientTickEvent(Phase phase) { super(phase); }
    }
    public static class RenderTickEvent extends TickEvent {
        public RenderTickEvent(Phase phase) { super(phase); }
    }
}