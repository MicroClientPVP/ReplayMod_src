package com.replaymod.core.mixin;

import com.replaymod.core.ReplayMod;
import com.replaymod.replay.ReplayModReplay;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({KeyBinding.class})
public class Mixin_ContextualKeyBindings {
   @Shadow
   @Final
   private static List<KeyBinding> keybindArray;
   @Unique
   private static final List<KeyBinding> temporarilyRemoved = new ArrayList<>();

   @Unique
   private static Collection<KeyBinding> keyBindings() {
      return keybindArray;
   }

   @Inject(
      method = {"resetKeyBindingArrayAndHash"},
      at = {@At("HEAD")}
   )
   private static void preContextualKeyBindings(CallbackInfo ci) {
      ReplayMod mod = ReplayMod.instance;
      if (mod != null) {
         Set<KeyBinding> onlyInReplay = mod.getKeyBindingRegistry().getOnlyInReplay();
         if (ReplayModReplay.instance.getReplayHandler() != null) {
            keyBindings().removeIf(keyBinding -> {
               for (KeyBinding exclusiveBinding : onlyInReplay) {
                  if (keyBinding.getKeyCode() == exclusiveBinding.getKeyCode() && keyBinding != exclusiveBinding) {
                     temporarilyRemoved.add(keyBinding);
                     return true;
                  }
               }

               return false;
            });
         } else {
            keyBindings().removeIf(keyBinding -> {
               if (onlyInReplay.contains(keyBinding)) {
                  temporarilyRemoved.add(keyBinding);
                  return true;
               } else {
                  return false;
               }
            });
         }
      }
   }

   @Inject(
      method = {"resetKeyBindingArrayAndHash"},
      at = {@At("RETURN")}
   )
   private static void postContextualKeyBindings(CallbackInfo ci) {
      for (KeyBinding keyBinding : temporarilyRemoved) {
         keyBindings().add(keyBinding);
      }

      temporarilyRemoved.clear();
   }
}
