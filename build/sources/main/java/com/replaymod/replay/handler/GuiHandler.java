package com.replaymod.replay.handler;

import com.replaymod.core.gui.GuiReplayButton;
import com.replaymod.core.versions.MCVer;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.VanillaGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiTooltip;
import com.replaymod.lib.de.johni0702.minecraft.gui.layout.CustomLayout;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.Point;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.InitScreenCallback;
import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.Setting;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler extends EventRegistrations {
   private static final int BUTTON_REPLAY_VIEWER = 17890234;
   private static final int BUTTON_EXIT_REPLAY = 17890235;
   private final ReplayModReplay mod;

   public GuiHandler(ReplayModReplay mod) {
      this.on(InitScreenCallback.EVENT, this::injectIntoIngameMenu);
      this.on(InitScreenCallback.EVENT, (screen, buttons) -> this.ensureReplayStopped(screen));
      this.on(InitScreenCallback.EVENT, this::injectIntoMainMenu);
      this.mod = mod;
   }

   private void injectIntoIngameMenu(GuiScreen guiScreen, Collection<GuiButton> buttonList) {
      if (guiScreen instanceof GuiIngameMenu) {
         if (this.mod.getReplayHandler() != null) {
            this.mod.getReplayHandler().getReplaySender().setReplaySpeed(0.0);
            int BUTTON_EXIT_SERVER = 1;
            int BUTTON_ADVANCEMENTS = 5;
            int BUTTON_STATS = 6;
            int BUTTON_OPEN_TO_LAN = 7;
            GuiButton openToLan = null;
            GuiButton achievements = null;
            GuiButton stats = null;

            for (GuiButton b : new ArrayList<>(buttonList)) {
               boolean remove = false;
               Integer id = b.id;
               if (id.equals(1)) {
                  remove = true;
                  MCVer.addButton(
                     guiScreen,
                     new GuiHandler.InjectedButton(
                        guiScreen, 17890235, b.xPosition, b.yPosition, b.getButtonWidth(), 20, "replaymod.gui.exit", null, this::onButton
                     )
                  );
               } else if (id.equals(5)) {
                  remove = true;
                  achievements = b;
               } else if (id.equals(6)) {
                  remove = true;
                  stats = b;
               } else if (id.equals(7)) {
                  remove = true;
                  openToLan = b;
               }

               if (remove) {
                  b.xPosition = -1000;
                  b.yPosition = -1000;
               }
            }

            if (achievements != null && stats != null) {
               this.moveAllButtonsInRect(
                  buttonList, achievements.xPosition, stats.xPosition + stats.getButtonWidth(), achievements.yPosition, Integer.MAX_VALUE, -24
               );
            }

            if (openToLan != null) {
               this.moveAllButtonsInRect(
                  buttonList, openToLan.xPosition, openToLan.xPosition + openToLan.getButtonWidth(), openToLan.yPosition, Integer.MAX_VALUE, -24
               );
            }
         }
      }
   }

   private void moveAllButtonsInRect(Collection<GuiButton> buttons, int xStart, int xEnd, int yStart, int yEnd, int moveBy) {
      buttons.stream()
         .filter(button -> button.xPosition <= xEnd && button.xPosition + button.getButtonWidth() >= xStart)
         .filter(button -> button.yPosition <= yEnd && button.yPosition + 20 >= yStart)
         .forEach(button -> button.yPosition += moveBy);
   }

   private void ensureReplayStopped(GuiScreen guiScreen) {
      if (guiScreen instanceof GuiMainMenu || guiScreen instanceof GuiMultiplayer) {
         if (this.mod.getReplayHandler() != null) {
            try {
               this.mod.getReplayHandler().endReplay();
            } catch (IOException var6) {
               ReplayModReplay.LOGGER.error("Trying to stop broken replay: ", var6);
            } finally {
               if (this.mod.getReplayHandler() != null) {
                  this.mod.forcefullyStopReplay();
               }
            }
         }
      }
   }

   private void injectIntoMainMenu(GuiScreen guiScreen, Collection<GuiButton> buttonList) {
      if (guiScreen instanceof GuiMainMenu) {
         this.legacyInjectIntoMainMenu(guiScreen, buttonList);
      }
   }

   private void legacyInjectIntoMainMenu(GuiScreen guiScreen, Collection<GuiButton> buttonList) {
      boolean isCustomMainMenuMod = guiScreen.getClass().getName().endsWith("custommainmenu.gui.GuiFakeMain");
      final GuiHandler.MainMenuButtonPosition buttonPosition = GuiHandler.MainMenuButtonPosition.valueOf(
         this.mod.getCore().getSettingsRegistry().get(Setting.MAIN_MENU_BUTTON)
      );
      if (buttonPosition != GuiHandler.MainMenuButtonPosition.BIG && !isCustomMainMenuMod) {
         VanillaGuiScreen vanillaGui = VanillaGuiScreen.wrap(guiScreen);
         final GuiReplayButton replayButton = new GuiReplayButton();
         replayButton.onClick(() -> new GuiReplayViewer(this.mod).display())
            .setTooltip(new GuiTooltip().setI18nText("replaymod.gui.replayviewer", new Object[0]));
         vanillaGui.setLayout(new CustomLayout<com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiScreen>(vanillaGui.getLayout()) {
            private Point pos;

            protected void layout(com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiScreen container, int width, int height) {
               if (this.pos == null) {
                  this.pos = GuiHandler.this.determineButtonPos(buttonPosition, guiScreen, buttonList);
               }

               this.size(replayButton, 20, 20);
               this.pos(replayButton, this.pos.getX(), this.pos.getY());
            }
         }).addElements(null, new GuiElement[]{replayButton});
      } else {
         int x = guiScreen.width / 2 - 100;
         int y = MCVer.findButton(buttonList, "menu.online", 14)
            .map(Optional::of)
            .orElse(MCVer.findButton(buttonList, "menu.multiplayer", 2))
            .<Integer>map(it -> it.yPosition)
            .orElse(guiScreen.height / 4 + 10 + 96);
         this.moveAllButtonsInRect(buttonList, x, x + 200, Integer.MIN_VALUE, y, -24);
         GuiHandler.InjectedButton button = new GuiHandler.InjectedButton(
            guiScreen, 17890234, x, y, 200, 20, "replaymod.gui.replayviewer", null, this::onButton
         );
         if (isCustomMainMenuMod) {
            buttonList.add(button);
         } else {
            MCVer.addButton(guiScreen, button);
         }
      }
   }

   private Point determineButtonPos(GuiHandler.MainMenuButtonPosition buttonPosition, GuiScreen guiScreen, Collection<GuiButton> buttonList) {
      Point topRight = new Point(guiScreen.width - 20 - 5, 5);
      if (buttonPosition == GuiHandler.MainMenuButtonPosition.TOP_LEFT) {
         return new Point(5, 5);
      } else if (buttonPosition == GuiHandler.MainMenuButtonPosition.TOP_RIGHT) {
         return topRight;
      } else {
         return buttonPosition == GuiHandler.MainMenuButtonPosition.DEFAULT
            ? Stream.of(
                  MCVer.findButton(buttonList, "menu.singleplayer", 1),
                  MCVer.findButton(buttonList, "menu.multiplayer", 2),
                  MCVer.findButton(buttonList, "menu.online", 14),
                  MCVer.findButton(buttonList, "modmenu.title", 6)
               )
               .flatMap(it -> it.map(Stream::of).orElseGet(Stream::empty))
               .filter(
                  it -> buttonList.stream()
                        .noneMatch(
                           button -> button.xPosition <= it.xPosition + it.getButtonWidth() + 4 + 20
                                 && button.yPosition <= it.yPosition + 20
                                 && button.xPosition + button.getButtonWidth() >= it.xPosition + it.getButtonWidth() + 4
                                 && button.yPosition + 20 >= it.yPosition
                        )
               )
               .max(Comparator.<GuiButton>comparingInt(it -> it.yPosition).thenComparingInt(it -> it.xPosition))
               .map(it -> new Point(it.xPosition + it.getButtonWidth() + 4, it.yPosition))
               .orElse(topRight)
            : Optional.of(buttonList).flatMap(buttons -> {
               switch (buttonPosition) {
                  case LEFT_OF_SINGLEPLAYER:
                  case RIGHT_OF_SINGLEPLAYER:
                     return MCVer.findButton(buttons, "menu.singleplayer", 1);
                  case LEFT_OF_MULTIPLAYER:
                  case RIGHT_OF_MULTIPLAYER:
                     return MCVer.findButton(buttons, "menu.multiplayer", 2);
                  case LEFT_OF_REALMS:
                  case RIGHT_OF_REALMS:
                     return MCVer.findButton(buttons, "menu.online", 14);
                  case LEFT_OF_MODS:
                  case RIGHT_OF_MODS:
                     return MCVer.findButton(buttons, "modmenu.title", 6);
                  default:
                     throw new RuntimeException();
               }
            }).map(button -> {
               switch (buttonPosition) {
                  case LEFT_OF_SINGLEPLAYER:
                  case LEFT_OF_MULTIPLAYER:
                  case LEFT_OF_REALMS:
                  case LEFT_OF_MODS:
                     return new Point(button.xPosition - 4 - 20, button.yPosition);
                  case RIGHT_OF_MODS:
                  case RIGHT_OF_SINGLEPLAYER:
                  case RIGHT_OF_MULTIPLAYER:
                  case RIGHT_OF_REALMS:
                     return new Point(button.xPosition + button.getButtonWidth() + 4, button.yPosition);
                  default:
                     throw new RuntimeException();
               }
            }).orElse(topRight);
      }
   }

   private int determineButtonIndex(Collection<GuiButton> buttons, GuiButton button) {
      GuiButton best = null;
      int bestIndex = -1;
      int index = 0;

      for (GuiButton other : buttons) {
         if (other.yPosition <= button.yPosition && (other.yPosition != button.yPosition || other.xPosition <= button.xPosition)) {
            if (best == null
               || other.yPosition > best.yPosition
               || other.yPosition == best.yPosition && other.xPosition > best.xPosition) {
               best = other;
               bestIndex = index + 1;
            }

            index++;
         } else {
            index++;
         }
      }

      return bestIndex;
   }

   @SubscribeEvent
   public void onButton(Pre event) {
      GuiScreen guiScreen = event.gui;
      GuiButton button = event.button;
      if (button.enabled) {
         if (guiScreen instanceof GuiMainMenu && button.id == 17890234) {
            new GuiReplayViewer(this.mod).display();
         }

         if (guiScreen instanceof GuiIngameMenu && this.mod.getReplayHandler() != null && button.id == 17890235) {
            button.enabled = false;

            try {
               this.mod.getReplayHandler().endReplay();
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }
      }
   }

   public static class InjectedButton extends GuiButton {
      public final GuiScreen guiScreen;
      public final int id;
      private Consumer<GuiHandler.InjectedButton> onClick;

      public InjectedButton(GuiScreen guiScreen, int buttonId, int x, int y, int width, int height, String buttonText, String tooltip, Consumer<Pre> onClick) {
         super(buttonId, x, y, width, height, I18n.format(buttonText, new Object[0]));
         this.guiScreen = guiScreen;
         this.id = buttonId;
         this.onClick = null;
      }
   }

   public static enum MainMenuButtonPosition {
      BIG,
      DEFAULT,
      TOP_LEFT,
      TOP_RIGHT,
      LEFT_OF_SINGLEPLAYER,
      RIGHT_OF_SINGLEPLAYER,
      LEFT_OF_MULTIPLAYER,
      RIGHT_OF_MULTIPLAYER,
      LEFT_OF_REALMS,
      RIGHT_OF_REALMS,
      LEFT_OF_MODS,
      RIGHT_OF_MODS;
   }
}
