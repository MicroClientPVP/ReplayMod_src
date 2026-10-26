package com.replaymod.render.utils;

import com.google.common.base.Objects;
import com.replaymod.core.ReplayMod;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Util;

public class PixelBufferObject implements AutoCloseable {
   public static final boolean SUPPORTED = GLContext.getCapabilities().GL_ARB_pixel_buffer_object || GLContext.getCapabilities().OpenGL15;
   private static final boolean arb = !GLContext.getCapabilities().OpenGL15;
   private static ThreadLocal<Integer> bound = new ThreadLocal<>();
   private static ThreadLocal<Integer> mapped = new ThreadLocal<>();
   private final long size;
   private long handle;

   public PixelBufferObject(long size, PixelBufferObject.Usage usage) {
      if (!SUPPORTED) {
         throw new UnsupportedOperationException("PBOs not supported.");
      } else {
         this.size = size;
         this.handle = arb ? (long)ARBBufferObject.glGenBuffersARB() : (long)GL15.glGenBuffers();
         this.bind();
         if (arb) {
            ARBBufferObject.glBufferDataARB(35051, size, usage.arb);
         } else {
            GL15.glBufferData(35051, size, usage.gl15);
         }

         this.unbind();
      }
   }

   private int getHandle() {
      if (this.handle == -1L) {
         throw new IllegalStateException("PBO not allocated.");
      } else {
         return (int)this.handle;
      }
   }

   public void bind() {
      if (arb) {
         ARBBufferObject.glBindBufferARB(35051, this.getHandle());
      } else {
         GL15.glBindBuffer(35051, this.getHandle());
      }

      bound.set(this.getHandle());
   }

   public void unbind() {
      this.checkBound();
      if (arb) {
         ARBBufferObject.glBindBufferARB(35051, 0);
      } else {
         GL15.glBindBuffer(35051, 0);
      }

      bound.set(0);
   }

   private void checkBound() {
      if (!Objects.equal(this.getHandle(), bound.get())) {
         throw new IllegalStateException("Buffer not bound.");
      }
   }

   private void checkNotMapped() {
      if (Objects.equal(this.getHandle(), mapped.get())) {
         throw new IllegalStateException("Buffer already mapped.");
      }
   }

   public ByteBuffer mapReadOnly() {
      this.checkBound();
      this.checkNotMapped();
      ByteBuffer buffer;
      if (arb) {
         buffer = ARBBufferObject.glMapBufferARB(35051, 35000, this.size, null);
      } else {
         buffer = GL15.glMapBuffer(35051, 35000, this.size, null);
      }

      if (buffer == null) {
         Util.checkGLError();
      }

      mapped.set(this.getHandle());
      return buffer;
   }

   public ByteBuffer mapWriteOnly() {
      this.checkBound();
      this.checkNotMapped();
      ByteBuffer buffer;
      if (arb) {
         buffer = ARBBufferObject.glMapBufferARB(35051, 35001, this.size, null);
      } else {
         buffer = GL15.glMapBuffer(35051, 35001, this.size, null);
      }

      if (buffer == null) {
         Util.checkGLError();
      }

      mapped.set(this.getHandle());
      return buffer;
   }

   public ByteBuffer mapReadWrite() {
      this.checkBound();
      this.checkNotMapped();
      ByteBuffer buffer;
      if (arb) {
         buffer = ARBBufferObject.glMapBufferARB(35051, 35002, this.size, null);
      } else {
         buffer = GL15.glMapBuffer(35051, 35002, this.size, null);
      }

      if (buffer == null) {
         Util.checkGLError();
      }

      mapped.set(this.getHandle());
      return buffer;
   }

   public void unmap() {
      this.checkBound();
      if (!Objects.equal(mapped.get(), this.getHandle())) {
         throw new IllegalStateException("Buffer not mapped.");
      } else {
         if (arb) {
            ARBBufferObject.glUnmapBufferARB(35051);
         } else {
            GL15.glUnmapBuffer(35051);
         }

         mapped.set(0);
      }
   }

   @Override
   public void close() {
      if (this.handle != -1L) {
         if (arb) {
            ARBBufferObject.glDeleteBuffersARB(this.getHandle());
         } else {
            GL15.glDeleteBuffers(this.getHandle());
         }

         this.handle = -1L;
      }
   }

   @Override
   protected void finalize() throws Throwable {
      super.finalize();
      if (this.handle != -1L) {
         LogManager.getLogger().warn("PBO garbage collected before deleted!");
         ReplayMod.instance.runLater(this::close);
      }
   }

   public static enum Usage {
      COPY(35042, 35042),
      DRAW(35040, 35040),
      READ(35041, 35041);

      private final int arb;
      private final int gl15;

      private Usage(int arb, int gl15) {
         this.arb = arb;
         this.gl15 = gl15;
      }
   }
}
