package net.minecraftforge.fml.client.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

public class ClientRegistry {
    public static void registerKeyBinding(KeyBinding binding) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.gameSettings != null) {
            mc.gameSettings.keyBindings = ArrayUtils.add(mc.gameSettings.keyBindings, binding);
        }
    }
}
