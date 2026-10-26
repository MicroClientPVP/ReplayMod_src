package net.minecraftforge.client.event;
import net.minecraftforge.fml.common.eventhandler.Event;
public class RenderGameOverlayEvent extends Event {
    public final float partialTicks;
    public final ElementType type;
    public RenderGameOverlayEvent(float partialTicks, ElementType type) { this.partialTicks = partialTicks; this.type = type; }
    public float getPartialTicks() { return partialTicks; }
    public ElementType getType() { return type; }
    public enum ElementType {
        ALL, HELMET, PORTAL, CROSSHAIRS, BOSSHEALTH, ARMOR, HEALTH, FOOD, AIR, HOTBAR, EXPERIENCE, TEXT, HEALTHMOUNT, JUMPBAR, CHAT, PLAYER_LIST, DEBUG
    }
    public static class Pre extends RenderGameOverlayEvent {
        public Pre(float partialTicks, ElementType type) { super(partialTicks, type); }
    }
    public static class Post extends RenderGameOverlayEvent {
        public Post(float partialTicks, ElementType type) { super(partialTicks, type); }
    }
    public static class Text extends Pre {
        public Text(float partialTicks, ElementType type) { super(partialTicks, type); }
    }
}