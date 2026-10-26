package com.replaymod.core;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ReplayModMixinConfigPlugin implements IMixinConfigPlugin {
   private final Logger logger = LogManager.getLogger("replaymod/mixin");
   private final boolean hasOF = hasClass("optifine.OptiFineForgeTweaker") || hasClass("me.modmuss50.optifabric.mod.Optifabric");

   static boolean hasClass(String name) throws IOException {
      return Launch.classLoader.getClassBytes(name) != null;
   }

   public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
      if (this.hasOF) {
      }

      if (mixinClassName.endsWith("_OF")) {
         return this.hasOF;
      } else {
         return mixinClassName.endsWith("_NoOF") ? !this.hasOF : true;
      }
   }

   public void onLoad(String mixinPackage) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public ReplayModMixinConfigPlugin() throws IOException {
      this.logger.debug("hasOF: " + this.hasOF);
   }
}
