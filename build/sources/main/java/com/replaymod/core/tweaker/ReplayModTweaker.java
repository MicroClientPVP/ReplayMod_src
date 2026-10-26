package com.replaymod.core.tweaker;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.MixinPlatformManager;

public class ReplayModTweaker implements ITweaker {
   private static final String MIXIN_TWEAKER = "org.spongepowered.asm.launch.MixinTweaker";

   public ReplayModTweaker() {
      this.injectMixinTweaker();
   }

   private void injectMixinTweaker() {
      List<String> tweakClasses = (List<String>)Launch.blackboard.get("TweakClasses");
      if (!tweakClasses.contains("org.spongepowered.asm.launch.MixinTweaker")) {
         if (GlobalProperties.get("mixin.initialised") == null) {
            System.out.println("Injecting MixinTweaker from ReplayModTweaker");
            Launch.classLoader
               .addClassLoaderExclusion("org.spongepowered.asm.launch.MixinTweaker".substring(0, "org.spongepowered.asm.launch.MixinTweaker".lastIndexOf(46)));
            List<ITweaker> tweaks = (List<ITweaker>)Launch.blackboard.get("Tweaks");

            try {
               tweaks.add((ITweaker)Class.forName("org.spongepowered.asm.launch.MixinTweaker", true, Launch.classLoader).newInstance());
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException var4) {
               throw new RuntimeException(var4);
            }
         }
      }
   }

   public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
   }

   public void injectIntoClassLoader(LaunchClassLoader classLoader) {
      URI uri;
      try {
         uri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
      } catch (URISyntaxException var4) {
         throw new RuntimeException(var4);
      }

      MixinPlatformManager platform = MixinBootstrap.getPlatform();
      platform.addContainer(uri);
   }

   public String getLaunchTarget() {
      return null;
   }

   public String[] getLaunchArguments() {
      return new String[0];
   }
}
