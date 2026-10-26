package com.replaymod.pathing.properties;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.NonNull;
import com.replaymod.lib.org.apache.commons.lang3.tuple.Triple;
import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.camera.CameraEntity;
import com.replaymod.replaystudio.pathing.change.Change;
import com.replaymod.replaystudio.pathing.property.AbstractProperty;
import com.replaymod.replaystudio.pathing.property.AbstractPropertyGroup;
import com.replaymod.replaystudio.pathing.property.PropertyPart;
import com.replaymod.replaystudio.pathing.property.PropertyParts.ForDoubleTriple;
import com.replaymod.replaystudio.pathing.property.PropertyParts.ForFloatTriple;
import com.replaymod.replaystudio.pathing.property.PropertyParts.TripleElement;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;

public class CameraProperties extends AbstractPropertyGroup {
   public static final CameraProperties GROUP = new CameraProperties();
   public static final CameraProperties.Position POSITION = new CameraProperties.Position();
   public static final CameraProperties.Rotation ROTATION = new CameraProperties.Rotation();

   private CameraProperties() {
      super("camera", "replaymod.gui.camera");
   }

   public Optional<Callable<Change>> getSetter() {
      return Optional.empty();
   }

   public static class Position extends AbstractProperty<Triple<Double, Double, Double>> {
      public final PropertyPart<Triple<Double, Double, Double>> X = new ForDoubleTriple(this, true, TripleElement.LEFT);
      public final PropertyPart<Triple<Double, Double, Double>> Y = new ForDoubleTriple(this, true, TripleElement.MIDDLE);
      public final PropertyPart<Triple<Double, Double, Double>> Z = new ForDoubleTriple(this, true, TripleElement.RIGHT);

      private Position() {
         super("position", "replaymod.gui.position", CameraProperties.GROUP, Triple.of(0.0, 0.0, 0.0));
      }

      public Collection<PropertyPart<Triple<Double, Double, Double>>> getParts() {
         return Arrays.asList(this.X, this.Y, this.Z);
      }

      public void applyToGame(Triple<Double, Double, Double> value, @NonNull Object replayHandler) {
         ReplayHandler handler = (ReplayHandler)replayHandler;
         handler.spectateCamera();
         CameraEntity cameraEntity = handler.getCameraEntity();
         if (cameraEntity != null) {
            cameraEntity.setCameraPosition((Double)value.getLeft(), (Double)value.getMiddle(), (Double)value.getRight());
         }
      }

      public void toJson(JsonWriter writer, Triple<Double, Double, Double> value) throws IOException {
         writer.beginArray().value((Number)value.getLeft()).value((Number)value.getMiddle()).value((Number)value.getRight()).endArray();
      }

      public Triple<Double, Double, Double> fromJson(JsonReader reader) throws IOException {
         reader.beginArray();

         Triple var2;
         try {
            var2 = Triple.of(reader.nextDouble(), reader.nextDouble(), reader.nextDouble());
         } finally {
            reader.endArray();
         }

         return var2;
      }
   }

   public static class Rotation extends AbstractProperty<Triple<Float, Float, Float>> {
      public final PropertyPart<Triple<Float, Float, Float>> YAW = new ForFloatTriple(this, true, 360.0F, TripleElement.LEFT);
      public final PropertyPart<Triple<Float, Float, Float>> PITCH = new ForFloatTriple(this, true, 360.0F, TripleElement.MIDDLE);
      public final PropertyPart<Triple<Float, Float, Float>> ROLL = new ForFloatTriple(this, true, 360.0F, TripleElement.RIGHT);

      private Rotation() {
         super("rotation", "replaymod.gui.rotation", CameraProperties.GROUP, Triple.of(0.0F, 0.0F, 0.0F));
      }

      public Collection<PropertyPart<Triple<Float, Float, Float>>> getParts() {
         return Arrays.asList(this.YAW, this.PITCH, this.ROLL);
      }

      public void applyToGame(Triple<Float, Float, Float> value, @NonNull Object replayHandler) {
         ReplayHandler handler = (ReplayHandler)replayHandler;
         handler.spectateCamera();
         CameraEntity cameraEntity = handler.getCameraEntity();
         if (cameraEntity != null) {
            cameraEntity.setCameraRotation((Float)value.getLeft(), (Float)value.getMiddle(), (Float)value.getRight());
         }
      }

      public void toJson(JsonWriter writer, Triple<Float, Float, Float> value) throws IOException {
         writer.beginArray().value((Number)value.getLeft()).value((Number)value.getMiddle()).value((Number)value.getRight()).endArray();
      }

      public Triple<Float, Float, Float> fromJson(JsonReader reader) throws IOException {
         reader.beginArray();

         Triple var2;
         try {
            var2 = Triple.of((float)reader.nextDouble(), (float)reader.nextDouble(), (float)reader.nextDouble());
         } finally {
            reader.endArray();
         }

         return var2;
      }
   }
}
