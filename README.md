# Cosmetic Wings

Fabric client mod for Minecraft 1.21.10.

## GitHub Actions

This repository intentionally does **not** include a Gradle Wrapper JAR. GitHub Actions installs Gradle 8.14 directly, which avoids wrapper-checksum validation problems.

The project uses Java 21, Fabric Loader, Fabric API and Fabric Loom 1.11.1.

## Build locally

Install Gradle 8.14 and Java 21, then run:

```bash
gradle build
```

The built JAR will appear in `build/libs/`.
