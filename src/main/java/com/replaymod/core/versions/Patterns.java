package com.replaymod.core.versions;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.replaymod.core.mixin.MinecraftAccessor;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

class Patterns {
   private static void addCrashCallable(CrashReportCategory category, String name, Callable<String> callable) {
      category.addCrashSectionCallable(name, callable);
   }

   private static double Entity_getX(Entity entity) {
      return entity.posX;
   }

   private static double Entity_getY(Entity entity) {
      return entity.posY;
   }

   private static double Entity_getZ(Entity entity) {
      return entity.posZ;
   }

   private static void Entity_setYaw(Entity entity, float value) {
      entity.rotationYaw = value;
   }

   private static float Entity_getYaw(Entity entity) {
      return entity.rotationYaw;
   }

   private static void Entity_setPitch(Entity entity, float value) {
      entity.rotationPitch = value;
   }

   private static float Entity_getPitch(Entity entity) {
      return entity.rotationPitch;
   }

   private static void Entity_setPos(Entity entity, double x, double y, double z) {
      entity.posX = x;
      entity.posY = y;
      entity.posZ = z;
   }

   private static int getX(GuiButton button) {
      return button.xPosition;
   }

   private static int getY(GuiButton button) {
      return button.yPosition;
   }

   private static void setX(GuiButton button, int value) {
      button.xPosition = value;
   }

   private static void setY(GuiButton button, int value) {
      button.yPosition = value;
   }

   private static void setWidth(GuiButton button, int value) {
      button.xPosition = button.xPosition;
   }

   private static int getWidth(GuiButton button) {
      return button.getButtonWidth();
   }

   private static int getHeight(GuiButton button) {
      return 20;
   }

   private static String readString(PacketBuffer buffer, int max) {
      return buffer.readStringFromBuffer(max);
   }

   private static Entity getRenderViewEntity(Minecraft mc) {
      return mc.getRenderViewEntity();
   }

   private static void setRenderViewEntity(Minecraft mc, Entity entity) {
      mc.setRenderViewEntity(entity);
   }

   private static Entity getVehicle(Entity passenger) {
      return passenger.ridingEntity;
   }

   private static InventoryPlayer getInventory(EntityPlayer entity) {
      return entity.inventory;
   }

   private static Iterable<Entity> loadedEntityList(WorldClient world) {
      return world.loadedEntityList;
   }

   private static Collection<Entity>[] getEntitySectionArray(Chunk chunk) {
      return chunk.getEntityLists();
   }

   private static List<? extends EntityPlayer> playerEntities(World world) {
      return world.playerEntities;
   }

   private static boolean isOnMainThread(Minecraft mc) {
      return mc.isCallingFromMinecraftThread();
   }

   private static void scheduleOnMainThread(Minecraft mc, Runnable runnable) {
      mc.addScheduledTask(runnable);
   }

   private static Window getWindow(Minecraft mc) {
      return new Window(mc);
   }

   private static WorldRenderer Tessellator_getBuffer(Tessellator tessellator) {
      return tessellator.getWorldRenderer();
   }

   private static void VertexConsumer_next() {
   }

   private static void BufferBuilder_beginPosCol(WorldRenderer buffer, int mode) {
      buffer.begin(mode, DefaultVertexFormats.POSITION_COLOR);
   }

   private static void BufferBuilder_addPosCol(WorldRenderer buffer, double x, double y, double z, int r, int g, int b, int a) {
      buffer.pos(x, y, z).color(r, g, b, a).endVertex();
   }

   private static void BufferBuilder_beginPosTex(WorldRenderer buffer, int mode) {
      buffer.begin(mode, DefaultVertexFormats.POSITION_TEX);
   }

   private static void BufferBuilder_addPosTex(WorldRenderer buffer, double x, double y, double z, float u, float v) {
      buffer.pos(x, y, z).tex((double)u, (double)v).endVertex();
   }

   private static void BufferBuilder_beginPosTexCol(WorldRenderer buffer, int mode) {
      buffer.begin(mode, DefaultVertexFormats.POSITION_TEX_COLOR);
   }

   private static void BufferBuilder_addPosTexCol(WorldRenderer buffer, double x, double y, double z, float u, float v, int r, int g, int b, int a) {
      buffer.pos(x, y, z).tex((double)u, (double)v).color(r, g, b, a).endVertex();
   }

   private static Tessellator Tessellator_getInstance() {
      return Tessellator.getInstance();
   }

   private static RenderManager getEntityRenderDispatcher(Minecraft mc) {
      return mc.getRenderManager();
   }

   private static float getCameraYaw(RenderManager dispatcher) {
      return dispatcher.playerViewY;
   }

   private static float getCameraPitch(RenderManager dispatcher) {
      return dispatcher.playerViewX;
   }

   private static float getRenderPartialTicks(Minecraft mc) {
      return ((MinecraftAccessor)mc).getTimer().renderPartialTicks;
   }

   private static TextureManager getTextureManager(Minecraft mc) {
      return mc.getTextureManager();
   }

   private static String getBoundKeyName(KeyBinding keyBinding) {
      return Keyboard.getKeyName(keyBinding.getKeyCode());
   }

   private static PositionedSoundRecord master(ResourceLocation sound, float pitch) {
      return PositionedSoundRecord.create(sound, pitch);
   }

   private static boolean isKeyBindingConflicting(KeyBinding a, KeyBinding b) {
      return a.getKeyCode() == b.getKeyCode();
   }

   private static void BufferBuilder_beginLineStrip() {
   }

   private static void BufferBuilder_beginLines() {
   }

   private static void BufferBuilder_beginQuads() {
   }

   private static void GL11_glLineWidth(float width) {
      GL11.glLineWidth(width);
   }

   private static void GL11_glTranslatef(float x, float y, float z) {
      GL11.glTranslatef(x, y, z);
   }

   private static void GL11_glRotatef(float angle, float x, float y, float z) {
      GL11.glRotatef(angle, x, y, z);
   }

   private static void Futures_addCallback(ListenableFuture future, FutureCallback callback) {
      Futures.addCallback(future, callback);
   }

   private static void setCrashReport(Minecraft mc, CrashReport report) {
      mc.crashed(report);
   }

   private static ReportedException crashReportToException(Minecraft mc) {
      return new ReportedException(((MinecraftAccessor)mc).getCrashReporter());
   }

   private static Vec3 getTrackedPosition(Entity entity) {
      return MCVer.getTrackedPosition(entity);
   }

   private static IChatComponent newTextLiteral(String str) {
      return new ChatComponentText(str);
   }

   private static IChatComponent newTextTranslatable(String key, Object... args) {
      return new ChatComponentTranslation(key, args);
   }

   private static void getTrackedPos() {
   }

   private static void setGamma(GameSettings options, double value) {
      options.gammaSetting = (float)value;
   }

   private static double getGamma(GameSettings options) {
      return (double)options.gammaSetting;
   }

   private static int getViewDistance(GameSettings options) {
      return options.renderDistanceChunks;
   }

   private static double getFov(GameSettings options) {
      return (double)options.fovSetting;
   }

   private static int getGuiScale(GameSettings options) {
      return options.guiScale;
   }

   private static IResource getResource(IResourceManager manager, ResourceLocation id) throws IOException {
      return manager.getResource(id);
   }

   private static List<ItemStack> DefaultedList_ofSize_ItemStack_Empty(int size) {
      return Arrays.asList();
   }

   private static void setSoundVolume(GameSettings options, SoundCategory category, float value) {
      options.setSoundLevel(category, value);
   }

   private static void SoundEvent_of() {
   }

   private static void POSITIVE_X() {
   }

   private static void POSITIVE_Y() {
   }

   private static void POSITIVE_Z() {
   }

   private static void getDegreesQuaternion() {
   }

   private static void Quaternion_mul() {
   }

   private static void Quaternion_getX() {
   }

   private static void Quaternion_getY() {
   }

   private static void Quaternion_getZ() {
   }

   private static void Quaternion_getW() {
   }

   private static void Quaternion_copy() {
   }

   private static void Matrix4f_multiply() {
   }

   private static void Matrix4f_translate() {
   }

   private static void Matrix4f_perspectiveMatrix() {
   }

   private static void REGISTRIES() {
   }

   public World getWorld(Entity entity) {
      return entity.worldObj;
   }

   public Object channel(S3FPacketCustomPayload packet) {
      return packet.getChannelName();
   }

   public void getPacketId() {
   }

   public void UnloadChunkPacket_getZ() {
   }

   public void UnloadChunkPacket_getX() {
   }

   public UUID getId(AddPlayerData entry) {
      return entry.getProfile().getId();
   }

   public ResourceLocation getSkinTexture(AbstractClientPlayer player) {
      return player.getLocationSkin();
   }

   public boolean isDebugHudEnabled(Minecraft mc) {
      return mc.gameSettings.showDebugInfo;
   }

   public IChatComponent getMessage(S40PacketDisconnect packet) {
      return packet.getReason();
   }
}
