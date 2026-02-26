# Tarkeez — تركيز

> **Tarkeez** (Arabic: تركيز) means *Focus*. A cross-platform desktop productivity app that combines a **Pomodoro timer** with a **rich HTML note editor** and **ambient audio** — everything you need to stay in the zone.

<p align="center">
  <img src="assets/icon.png" alt="Tarkeez Logo" width="120"/>
</p>

---

## Table of Contents

1. [About the Project](#about-the-project)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Prerequisites](#prerequisites)
5. [Getting Started](#getting-started)
   - [Clone the Repository](#clone-the-repository)
   - [Build](#build)
   - [Run (Development)](#run-development)
   - [Run (JAR)](#run-jar)
6. [Native Installers](#native-installers)
7. [Project Structure](#project-structure)
8. [How It Works](#how-it-works)
   - [Pomodoro Timer](#pomodoro-timer)
   - [HTML Note Editor](#html-note-editor)
   - [Ambient Audio Player](#ambient-audio-player)
   - [Dark / Light Mode](#dark--light-mode)
   - [Toast Notifications](#toast-notifications)
9. [CI/CD Pipeline](#cicd-pipeline)
10. [Contributing](#contributing)
11. [License](#license)

---

## About the Project

Tarkeez is a **JavaFX** desktop application designed to boost productivity through the [Pomodoro Technique](https://en.wikipedia.org/wiki/Pomodoro_Technique). It lets you:

- Stay focused with a configurable work/break countdown timer.
- Take rich, formatted notes directly inside the app.
- Drown out distractions with looping ambient soundscapes.
- Work comfortably in either **dark mode** or **light mode**.

The app ships as a single executable (`.exe`, `.deb`, `.rpm`, or `.dmg`) built by GitHub Actions, so there is no Java installation required on end-user machines.

---

## Features

| Feature | Details |
|---|---|
| ⏱️ **Pomodoro Timer** | Configurable work (default 25 min) and break (default 5 min) durations. Displays a smooth rotating progress circle and tracks completed sessions. |
| 📝 **Rich HTML Editor** | Full-featured WYSIWYG editor backed by JavaFX `HTMLEditor`. Create, open, and save `.html` notes with formatting, lists, headings, and more. |
| 🔊 **Ambient Audio** | Three built-in soundscapes — **Rain**, **Forest**, and **Ocean**. Loops continuously with adjustable volume and an animated waveform indicator. |
| 🌙 **Dark / Light Mode** | Toggle between a dark theme (default) and a light theme at any time; preference is reflected instantly across the entire UI. |
| 🔔 **Toast Notifications** | Non-intrusive slide-in notifications that auto-dismiss after 5 seconds; three severity levels: `SUCCESS`, `INFO`, and `ERROR`. |
| ⚙️ **Settings Dialog** | Configure work and break durations (1–120 minutes each) through a modal dialog without restarting the app. |
| 💾 **File I/O** | **New**, **Open**, **Save**, and **Save As** operations for `.html` note files. |
| 📦 **Native Packaging** | GitHub Actions automatically builds platform-native installers for Windows, Linux, and macOS. |

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| UI Framework | JavaFX 21 (Controls, FXML, Web, Media) |
| Layout / Styling | FXML + CSS (dark/light theming via CSS variables) |
| Icons | [Ikonli](https://kordamp.org/ikonli/) 12.3.1 |
| HTML Parsing | [jsoup](https://jsoup.org/) 1.17.2 |
| Build Tool | Apache Maven 3.x (Maven Wrapper included) |
| CI/CD | GitHub Actions |
| Packaging | `jpackage` (JDK built-in tool) |
| Testing | JUnit Jupiter 5.12.1 |

---

## Prerequisites

To **build from source** you need:

- **JDK 25** or later — e.g. [Eclipse Temurin](https://adoptium.net/)
- **Maven 3.6+** (or use the included `mvnw` / `mvnw.cmd` wrapper — no separate Maven install needed)

To **run a pre-built installer** you need nothing; the native package bundles its own JRE.

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/moabdulhakim/tarkeez.git
cd tarkeez
```

### Build

```bash
# Linux / macOS
./mvnw clean package

# Windows
mvnw.cmd clean package
```

This compiles the source, runs tests, and produces a fat JAR at:

```
target/tarkeez-1.0-SNAPSHOT.jar
```

### Run (Development)

Use the JavaFX Maven plugin for a quick launch with the correct module path:

```bash
# Linux / macOS
./mvnw javafx:run

# Windows
mvnw.cmd javafx:run
```

### Run (JAR)

```bash
java -jar target/tarkeez-1.0-SNAPSHOT.jar
```

> **Note:** Running the JAR directly requires JavaFX modules to be available on the module path. Using the Maven plugin is recommended during development.

---

## Native Installers

Pre-built installers are produced by the [GitHub Actions CI pipeline](#cicd-pipeline) and are uploaded as workflow artifacts. To download them:

1. Go to the **Actions** tab of this repository.
2. Select the latest **Build Tarkeez Native Apps** workflow run.
3. Download the artifact for your platform:

| Platform | Artifact | Installer Format |
|---|---|---|
| Windows | `Tarkeez-Installers-windows-latest` | `.exe` |
| Linux (Debian/Ubuntu) | `Tarkeez-Installers-ubuntu-latest` | `.deb` |
| Linux (Fedora/Red Hat) | `Tarkeez-Installers-ubuntu-latest` | `.rpm` |
| macOS | `Tarkeez-Installers-macos-latest` | `.dmg` |

To build installers locally, run:

```bash
# 1. Build the fat JAR first
./mvnw clean package

# 2. Run jpackage (example for Linux .deb)
jpackage --type deb \
  --name Tarkeez \
  --input target/ \
  --main-jar tarkeez-1.0-SNAPSHOT.jar \
  --main-class com.example.tarkeez.Launcher \
  --icon assets/icon.png
```

See `.github/workflows/build.yml` for the exact commands used for each platform.

---

## Project Structure

```
tarkeez/
├── assets/                          # Platform icons for native packaging
│   ├── icon.ico                     #   Windows
│   ├── icon.icns                    #   macOS
│   └── icon.png                     #   Linux
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java     # Java module descriptor
│       │   └── com/example/tarkeez/
│       │       ├── Launcher.java                 # Main entry point
│       │       ├── TarkeezApp.java               # JavaFX Application bootstrap
│       │       ├── EditorController.java         # Primary UI controller
│       │       ├── SettingsController.java       # Settings dialog controller
│       │       ├── models/
│       │       │   ├── Timer.java                # Pomodoro timer logic
│       │       │   ├── TimerStateChangeEvent.java# Timer state enum
│       │       │   └── AudioPlayer.java          # Ambient audio playback
│       │       └── utils/
│       │           ├── Toast.java                # Notification popup
│       │           └── ToastStatus.java          # Notification severity enum
│       └── resources/
│           ├── images/icon.png                   # App icon (classpath)
│           ├── audio/
│           │   ├── Rain.wav                      # Ambient soundscape
│           │   ├── Forest.wav
│           │   └── Ocean.wav
│           └── com/example/tarkeez/
│               ├── view.fxml                     # Main window layout
│               ├── settings.fxml                 # Settings dialog layout
│               └── style.css                     # Dark / Light theme styles
├── .github/workflows/build.yml      # CI/CD pipeline
├── pom.xml                          # Maven build descriptor
├── mvnw / mvnw.cmd                  # Maven wrapper scripts
└── .gitignore
```

---

## How It Works

### Pomodoro Timer

`Timer.java` runs a background `Thread` that counts down every second. When a work session ends it automatically switches to a break, and vice versa. The `StateChangeListener` callback notifies `EditorController` on every state transition (`START_SESSION`, `BREAK_TIME`, `SESSION_INCREMENT`, `RESET`) so the UI can update the progress arc, session counter, and fire toast notifications — all on the JavaFX Application Thread via `Platform.runLater()`.

Default durations (configurable via Settings):

| Phase | Default |
|---|---|
| Work | 25 minutes |
| Break | 5 minutes |

### HTML Note Editor

The editor is a standard `HTMLEditor` component from JavaFX. Files are read and written as raw HTML. jsoup is bundled for future HTML pre-processing needs. Toolbar buttons trigger `execCommand` calls on the underlying `WebView`.

### Ambient Audio Player

`AudioPlayer.java` wraps JavaFX `MediaPlayer`. Each soundscape (`Rain.wav`, `Forest.wav`, `Ocean.wav`) is loaded from the classpath and set to loop indefinitely. A volume slider in the UI drives `MediaPlayer.setVolume()`. An animated waveform bar provides a visual playback indicator.


### Dark / Light Mode

`style.css` uses CSS custom properties (variables) to define the entire color palette. The theme toggle button in `EditorController` swaps a root-level CSS class between `dark-mode` (default) and `light-mode`, which redefines all variables at once.

Key dark-mode colors:

```css
-base-color:    #1e2233;   /* Background */
-accent-orange: #ff8c00;   /* Primary highlight / timer */
-accent-blue:   #00a8ff;   /* Secondary highlight */
-break-color:   #f2ff23;   /* Break phase accent */
```

### Toast Notifications

`Toast.java` creates a small overlay pane anchored to the bottom of the screen. It fades in, waits 5 seconds, then fades out. Three status types map to distinct colors:

| Status | Colour |
|---|---|
| `SUCCESS` | Green |
| `INFO` | Blue |
| `ERROR` | Red |

---

## CI/CD Pipeline

The workflow in `.github/workflows/build.yml` is triggered **manually** from the GitHub Actions UI (`workflow_dispatch`). It runs three parallel jobs — one per OS — using a build matrix:

```
┌────────────────────────────────────────────────────────────────┐
│  Build Tarkeez Native Apps  (manual trigger)                   │
│                                                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐ │
│  │  Windows     │  │  Ubuntu      │  │  macOS               │ │
│  │  windows-    │  │  ubuntu-     │  │  macos-latest        │ │
│  │  latest      │  │  latest      │  │                      │ │
│  │              │  │              │  │                      │ │
│  │ mvn package  │  │ mvn package  │  │ mvn package          │ │
│  │ jpackage exe │  │ jpackage deb │  │ jpackage dmg         │ │
│  │              │  │ jpackage rpm │  │                      │ │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘ │
│         │                 │                      │             │
│         └─────────────────┴──────────────────────┘             │
│                      upload-artifact                           │
└────────────────────────────────────────────────────────────────┘
```

Each job:
1. Checks out the code.
2. Sets up **JDK 25** (Eclipse Temurin) with Maven caching.
3. Builds the fat JAR with `mvn clean package`.
4. Calls `jpackage` to produce the platform installer.
5. Uploads the installer as a downloadable workflow artifact.

---

## Contributing

Contributions are welcome! Here is how to get started:

1. **Fork** the repository and create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes and **add tests** where applicable.
3. Verify the build passes:
   ```bash
   ./mvnw clean package
   ```
4. Open a **Pull Request** describing what you changed and why.

Please keep pull requests focused — one feature or fix per PR.

---

## License

This project is open source. See the repository for licensing details.

---

<p align="center">Built with ☕ Java and ❤️ — Stay focused, stay productive.</p>
