package com.replaymod.editor.gui;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.versions.MCVer;
import com.replaymod.lib.org.apache.commons.lang3.tuple.Pair;
import com.replaymod.replaystudio.PacketData;
import com.replaymod.replaystudio.data.Marker;
import com.replaymod.replaystudio.filter.DimensionTracker;
import com.replaymod.replaystudio.filter.SquashFilter;
import com.replaymod.replaystudio.filter.StreamFilter;
import com.replaymod.replaystudio.io.ReplayInputStream;
import com.replaymod.replaystudio.io.ReplayOutputStream;
import com.replaymod.replaystudio.lib.viaversion.api.protocol.packet.State;
import com.replaymod.replaystudio.protocol.PacketTypeRegistry;
import com.replaymod.replaystudio.replay.ReplayFile;
import com.replaymod.replaystudio.replay.ReplayMetaData;
import com.replaymod.replaystudio.stream.IteratorStream;
import com.replaymod.replaystudio.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

public class MarkerProcessor {
   public static final String MARKER_NAME_START_CUT = "_RM_START_CUT";
   public static final String MARKER_NAME_END_CUT = "_RM_END_CUT";
   public static final String MARKER_NAME_SPLIT = "_RM_SPLIT";

   private static boolean hasWork(Path path) throws IOException {
      ReplayFile inputReplayFile = ReplayMod.instance.files.open(path);
      Throwable var2 = null;

      boolean var3;
      try {
         var3 = ((Set<Marker>)inputReplayFile.getMarkers().or(HashSet::new)).stream().anyMatch(m -> m.getName() != null && m.getName().startsWith("_RM_"));
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (inputReplayFile != null) {
            if (var2 != null) {
               try {
                  inputReplayFile.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               inputReplayFile.close();
            }
         }
      }

      return var3;
   }

   public static boolean producesAnyOutput(ReplayFile replayFile) throws IOException {
      return !getOutputSuffixes(replayFile).isEmpty();
   }

   private static List<String> getOutputSuffixes(ReplayFile inputReplayFile) throws IOException {
      List<Marker> markers = ((Set<Marker>)inputReplayFile.getMarkers().or(HashSet::new))
         .stream()
         .sorted(Comparator.comparing(Marker::getTime))
         .collect(Collectors.toList());
      int nextSuffix = 0;
      List<String> suffixes = new ArrayList<>();
      MarkerProcessor.OutputState state = MarkerProcessor.OutputState.Writing;

      for (Marker marker : markers) {
         if ("_RM_START_CUT".equals(marker.getName())) {
            if (marker.getTime() == 0) {
               state = MarkerProcessor.OutputState.NotYetWriting;
            } else {
               state = MarkerProcessor.OutputState.Paused;
            }
         } else if ("_RM_END_CUT".equals(marker.getName())) {
            state = MarkerProcessor.OutputState.Writing;
         } else if ("_RM_SPLIT".equals(marker.getName())) {
            switch (state) {
               case NotYetWriting:
               default:
                  break;
               case Writing:
                  suffixes.add("_" + nextSuffix++);
                  state = MarkerProcessor.OutputState.Writing;
                  break;
               case Paused:
                  suffixes.add("_" + nextSuffix++);
                  state = MarkerProcessor.OutputState.NotYetWriting;
            }
         }
      }

      if (state != MarkerProcessor.OutputState.NotYetWriting) {
         suffixes.add("_" + nextSuffix);
      }

      return suffixes.size() == 1 ? Collections.singletonList("") : suffixes;
   }

   public static List<Pair<Path, ReplayMetaData>> apply(Path path, Consumer<Float> progress) throws IOException {
      ReplayMod mod = ReplayMod.instance;
      if (!hasWork(path)) {
         ReplayFile inputReplayFile = mod.files.open(path);
         Throwable var196 = null;

         ReplayMetaData metaData;
         try {
            metaData = inputReplayFile.getMetaData();
         } catch (Throwable var182) {
            var196 = var182;
            throw var182;
         } finally {
            if (inputReplayFile != null) {
               if (var196 != null) {
                  try {
                     inputReplayFile.close();
                  } catch (Throwable var181) {
                     var196.addSuppressed(var181);
                  }
               } else {
                  inputReplayFile.close();
               }
            }
         }

         return Collections.singletonList(Pair.of(path, metaData));
      } else {
         String replayName = FilenameUtils.getBaseName(path.getFileName().toString());
         int splitCounter = 0;
         PacketTypeRegistry registry = MCVer.getPacketTypeRegistry(State.LOGIN);
         DimensionTracker dimensionTracker = new DimensionTracker();
         SquashFilter squashFilter = new SquashFilter(null, null, null);
         List<Pair<Path, ReplayMetaData>> outputPaths = new ArrayList<>();
         Path rawFolder = ReplayMod.instance.folders.getRawReplayFolder();
         Path inputPath = rawFolder.resolve(path.getFileName());

         for (int i = 1; Files.exists(inputPath); i++) {
            inputPath = inputPath.resolveSibling(replayName + "." + i + ".mcpr");
         }

         Files.move(path, inputPath);
         ReplayFile inputReplayFile = mod.files.open(inputPath);
         Throwable var12 = null;

         try {
            List<Marker> markers = ((Set<Marker>)inputReplayFile.getMarkers().or(HashSet::new))
               .stream()
               .sorted(Comparator.comparing(Marker::getTime))
               .collect(Collectors.toList());
            Iterator<Marker> markerIterator = markers.iterator();
            Iterator<String> outputFileSuffixes = getOutputSuffixes(inputReplayFile).iterator();
            boolean anySplit = markers.stream().anyMatch(m -> "_RM_SPLIT".equals(m.getName()));
            int inputDuration = inputReplayFile.getMetaData().getDuration();
            ReplayInputStream replayInputStream = inputReplayFile.getPacketData(registry);
            int timeOffset = 0;
            SquashFilter cutFilter = null;
            int startCutOffset = 0;
            PacketData nextPacket = replayInputStream.readPacket();
            Marker nextMarker = markerIterator.next();

            while (nextPacket != null && outputFileSuffixes.hasNext()) {
               Path outputPath = path.resolveSibling(replayName + outputFileSuffixes.next() + ".mcpr");
               ReplayFile outputReplayFile = mod.files.open(null, outputPath);
               Throwable var26 = null;

               try {
                  long duration = 0L;
                  Set<Marker> outputMarkers = new HashSet<>();
                  ReplayMetaData metaData = inputReplayFile.getMetaData();
                  metaData.setDate(metaData.getDate() + (long)timeOffset);
                  ReplayOutputStream replayOutputStream = outputReplayFile.writePacketData();
                  Throwable var32 = null;

                  try {
                     if (cutFilter != null) {
                        cutFilter.release();
                        cutFilter = squashFilter.copy();
                     } else if (splitCounter > 0) {
                        List<PacketData> packets = new ArrayList<>();
                        squashFilter.copy().onEnd(new IteratorStream(packets.listIterator(), (StreamFilter)null), (long)timeOffset);

                        for (PacketData packet : packets) {
                           replayOutputStream.write(0L, packet.getPacket());
                        }
                     }

                     boolean hasFurtherOutputs = outputFileSuffixes.hasNext();

                     while (nextPacket != null) {
                        if (nextMarker != null && nextPacket.getTime() >= (long)nextMarker.getTime()) {
                           if ("_RM_START_CUT".equals(nextMarker.getName())) {
                              if (cutFilter != null) {
                                 cutFilter.release();
                              }

                              startCutOffset = nextMarker.getTime();
                              cutFilter = new SquashFilter(dimensionTracker);
                           } else if (!"_RM_END_CUT".equals(nextMarker.getName())) {
                              if ("_RM_SPLIT".equals(nextMarker.getName())) {
                                 splitCounter++;
                                 timeOffset = nextMarker.getTime();
                                 startCutOffset = timeOffset;
                                 nextMarker = markerIterator.hasNext() ? markerIterator.next() : null;
                                 break;
                              }

                              nextMarker.setTime(nextMarker.getTime() - timeOffset);
                              outputMarkers.add(nextMarker);
                           } else {
                              timeOffset += nextMarker.getTime() - startCutOffset;
                              if (cutFilter != null) {
                                 List<PacketData> packets = new ArrayList<>();
                                 cutFilter.onEnd(new IteratorStream(packets.listIterator(), (StreamFilter)null), (long)nextMarker.getTime());

                                 for (PacketData packet : packets) {
                                    replayOutputStream.write((long)(nextMarker.getTime() - timeOffset), packet.getPacket());
                                 }

                                 cutFilter = null;
                              }
                           }

                           nextMarker = markerIterator.hasNext() ? markerIterator.next() : null;
                        } else {
                           dimensionTracker.onPacket(null, nextPacket);
                           if (hasFurtherOutputs) {
                              squashFilter.onPacket(null, nextPacket);
                           }

                           if (cutFilter != null) {
                              cutFilter.onPacket(null, nextPacket);
                           } else {
                              replayOutputStream.write(nextPacket.getTime() - (long)timeOffset, nextPacket.getPacket().copy());
                              duration = nextPacket.getTime() - (long)timeOffset;
                           }

                           nextPacket.release();
                           nextPacket = replayInputStream.readPacket();
                           if (nextPacket != null) {
                              progress.accept((float)nextPacket.getTime() / (float)inputDuration);
                           } else {
                              progress.accept(1.0F);
                           }
                        }
                     }
                  } catch (Throwable var188) {
                     var32 = var188;
                     throw var188;
                  } finally {
                     if (replayOutputStream != null) {
                        if (var32 != null) {
                           try {
                              replayOutputStream.close();
                           } catch (Throwable var180) {
                              var32.addSuppressed(var180);
                           }
                        } else {
                           replayOutputStream.close();
                        }
                     }
                  }

                  metaData.setDuration((int)duration);
                  outputReplayFile.writeMetaData(registry, metaData);
                  outputReplayFile.writeMarkers(outputMarkers);
                  outputReplayFile.writeModInfo(inputReplayFile.getModInfo());
                  Map<Integer, String> resourcePackIndex = inputReplayFile.getResourcePackIndex();
                  if (resourcePackIndex != null) {
                     outputReplayFile.writeResourcePackIndex(resourcePackIndex);

                     for (String hash : resourcePackIndex.values()) {
                        try (
                           InputStream in = (InputStream)inputReplayFile.getResourcePack(hash).get();
                           OutputStream out = outputReplayFile.writeResourcePack(hash);
                        ) {
                           Utils.copy(in, out);
                        }
                     }
                  }

                  outputReplayFile.save();
                  outputPaths.add(Pair.of(outputPath, metaData));
               } catch (Throwable var190) {
                  var26 = var190;
                  throw var190;
               } finally {
                  if (outputReplayFile != null) {
                     if (var26 != null) {
                        try {
                           outputReplayFile.close();
                        } catch (Throwable var177) {
                           var26.addSuppressed(var177);
                        }
                     } else {
                        outputReplayFile.close();
                     }
                  }
               }
            }

            squashFilter.release();
            if (cutFilter != null) {
               cutFilter.release();
            }
         } catch (Throwable var192) {
            var12 = var192;
            throw var192;
         } finally {
            if (inputReplayFile != null) {
               if (var12 != null) {
                  try {
                     inputReplayFile.close();
                  } catch (Throwable var176) {
                     var12.addSuppressed(var176);
                  }
               } else {
                  inputReplayFile.close();
               }
            }
         }

         return outputPaths;
      }
   }

   private static enum OutputState {
      NotYetWriting,
      Writing,
      Paused;
   }
}
