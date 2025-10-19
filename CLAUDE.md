# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

OpenGraphKt is a minimalist Kotlin multiplatform library for parsing Open Graph protocol tags from HTML. It wraps Ksoup (a Kotlin port of JSoup) to extract and structure Open Graph metadata.

**Current Status**: Pre-alpha - Protocol implementation is complete for `og:` tags, but type system needs refinement.

## Project Structure

This is a multi-module Gradle project:

- `opengraphkt/` - Core library module (published to Maven Central as `fr.lengrand:opengraphkt`)
- `demo/` - Local file parsing examples
- `demo-remote/` - Remote URL parsing examples (see Main.kt for usage)
- `scrape-test/` - Testing/scraping utilities

## Common Commands

### Build and Test
```bash
./gradlew build                  # Build all modules
./gradlew test                   # Run all tests
./gradlew :opengraphkt:test      # Run tests for core library only
```

### Code Coverage
```bash
./gradlew koverXmlReport         # Generate XML coverage report
./gradlew koverVerify            # Verify coverage meets 70% minimum threshold
```

### Publishing
```bash
./gradlew publishToMavenLocal    # Publish to local Maven repo for testing
```

## Architecture

### Core Components

**Parser (`Parser.kt`)**: Main entry point that accepts multiple input types:
- `parse(url: URL)` - Fetches and parses remote HTML
- `parse(html: String)` - Parses raw HTML string
- `parse(file: File)` - Parses local HTML file
- `parse(document: Document)` - Parses Ksoup Document

The parser extracts `meta[property^=og:]` tags and builds structured data models.

**Data Models (`Models.kt`)**: Type-safe representations of Open Graph data:
- `Data` - Main container with `isValid()` method checking required fields (title, type, image, url)
- Base types: `Image`, `Video`, `Audio`
- Content-specific types: `Article`, `Book`, `Profile`
- Music types: `MusicSong`, `MusicAlbum`, `MusicPlaylist`, `MusicRadioStation`
- Video types: `VideoMovie`, `VideoEpisode`

### Key Implementation Details

**Tag Grouping**: Tags are grouped by namespace (prefix before first colon) to handle structured properties like `og:image:width`, `og:image:height` that belong to the preceding `og:image` tag.

**Date Handling**: ISO 8601 datetime parsing with fallback for date-only formats (appends `T00:00:00Z`).

**Structured Property Association**: Images/Videos/Audio with their metadata (width, height, type, etc.) are associated by parsing sequential tags - each base tag (`og:image`) is paired with following attribute tags (`og:image:width`) until the next base tag.

## Development Notes

- **JVM Toolchain**: Java 24 (see `jvmToolchain(24)` in build files)
- **Testing**: CI matrix tests on Java 17 and 23 via GitHub Actions
- **Dependencies**: Core library uses Ksoup (v0.2.5) for HTML parsing and network requests
- **Maven Coordinates**: Group `fr.lengrand`, artifact `opengraphkt`, currently at `0.1.2-SNAPSHOT`
- **Code Coverage**: Kover plugin enforces 70% minimum coverage threshold
