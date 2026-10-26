# ReplayMod (MicroClient Fork)

> [!NOTE]
> **This repository is a fork of the official [ReplayMod](https://github.com/ReplayMod/ReplayMod)** specifically maintained and adapted for **[MicroClient](https://github.com/MicroClientPVP)** (Minecraft 1.8.9).
>
> **Original Upstream:** [https://github.com/ReplayMod/ReplayMod](https://github.com/ReplayMod/ReplayMod)  
> **Official Website & Documentation:** [https://www.replaymod.com](https://www.replaymod.com)

---

### About this Fork

This fork adapts ReplayMod 1.8.9 for seamless integration into the **MicroClient** ecosystem:
- **Standalone 1.8.9 Mixin Build**: Pre-configured build script (`build.gradle`) with standalone Gradle wrapper and embedded dependencies in `libs/replaymod-libs.jar` for reproducible, unattended builds.
- **MicroClient UI & Capsule Theme Integration**: Integrated with MicroClient's sleek glassmorphic menu styling, modern cards, and dynamic capsule themes.
- **Client Sync & Compatibility**: Tailored mixins and hooks for client-side recording, replay viewer, and rendering without conflicting with MicroClient modules.

---

# Original ReplayMod Documentation

A Minecraft mod to record game sessions and replay them afterwards from any perspective.

## Features

- **Session Recording**: Records everything happening in your Minecraft world with virtually zero performance impact.
- **Replay Viewer**: Watch recorded replays from any angle, pause time, and freely fly around your world.
- **Keyframe Engine & Camera Paths**: Create cinematic camera movements using an intuitive timeline editor.
- **High-Definition Video Rendering**: Export your replays directly to MP4/MKV video files with custom shaders, stereoscopic 3D, and high resolutions.

---

## Building

Make sure you have Java 8 installed and configured (`JAVA_HOME`).

### Command Line
You can build the mod directly using the bundled Gradle wrapper:
```bash
./gradlew build
```
On Windows:
```cmd
gradlew.bat build
```

The resulting compiled JAR file will be located at:
```
build/libs/ReplayMod-MicroClient-1.8.9.jar
```

### IntelliJ IDEA Setup
1. Import the project as a Gradle project in IntelliJ.
2. Enable annotation processing:
   - Go to **File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors**
   - Check **"Enable annotation processing"**
3. Set Project SDK to **Java 1.8**.

---

## Credits & License

- **Original Creators**: The ReplayMod Team ([CrushedPixel](https://github.com/CrushedPixel), [johni0702](https://github.com/johni0702), and contributors).
- **Fork Maintainers**: [MicroClient Team](https://github.com/MicroClientPVP).
- **License**: ReplayMod is licensed under the [GNU General Public License v3.0](LICENSE).
