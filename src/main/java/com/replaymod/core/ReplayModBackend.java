package com.replaymod.core;

import com.replaymod.core.mixin.MinecraftAccessor;
import com.replaymod.core.versions.MCVer;
import com.replaymod.core.versions.forge.EventsAdapter;
import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.common.config.Configuration;

public class ReplayModBackend {
   public static ReplayModBackend instance;
   public final ReplayMod mod;
   private final EventsAdapter eventsAdapter = new EventsAdapter();
   public static Configuration config;

   public ReplayModBackend() {
      instance = this;
      this.mod = new ReplayMod(this);
   }

   public void init() {
      File configDir = new File(Minecraft.getMinecraft().mcDataDir, "config");
      if (!configDir.exists()) {
         configDir.mkdirs();
      }
      config = new Configuration(new File(configDir, "replaymod.cfg"));
      config.load();
      SettingsRegistry settingsRegistry = this.mod.getSettingsRegistry();
      settingsRegistry.backend.setConfiguration(config);
      settingsRegistry.save();

      this.mod.initModules();
      this.eventsAdapter.register();
   }

   public String getVersion() {
      return "2.6.24";
   }

   public String getMinecraftVersion() {
      return "1.8.9";
   }

   public boolean isModLoaded(String id) {
      return false;
   }

   static {
      try {
         List<IResourcePack> defaultResourcePacks = ((MinecraftAccessor)MCVer.getMinecraft()).getDefaultResourcePacks();
         if (ReplayMod.jGuiResourcePack != null) {
            defaultResourcePacks.add(ReplayMod.jGuiResourcePack);
         }
      } catch (Throwable ignored) {}
   }
}
