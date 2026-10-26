package net.minecraftforge.client.event;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
public class EntityViewRenderEvent extends Event {
    public static class CameraSetup extends EntityViewRenderEvent {
        public final Entity entity;
        public final Block block;
        public final double renderPartialTicks;
        public float yaw, pitch, roll;
        public CameraSetup(Entity entity, Block block, double renderPartialTicks, float yaw, float pitch, float roll) {
            this.entity = entity; this.block = block; this.renderPartialTicks = renderPartialTicks;
            this.yaw = yaw; this.pitch = pitch; this.roll = roll;
        }
    }
}