# Contributing to OpenGraphKt

Thank you for your interest in contributing to OpenGraphKt! This document provides guidelines and instructions for contributing to the project.

## Development Setup

### Prerequisites

- Java 17 or later (JVM toolchain configured for Java 17)
- Gradle 8.14.3+ (wrapper included)
- Git

### Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/jlengrand/OpenGraphKt.git
   cd OpenGraphKt
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run tests:
   ```bash
   ./gradlew test
   ```

4. Check code coverage:
   ```bash
   ./gradlew koverXmlReport
   ./gradlew koverVerify  # Enforces 70% minimum coverage
   ```

## Project Structure

- `opengraphkt/` - Core library module (published to Maven Central)
- `demo/` - Local file parsing examples
- `demo-remote/` - Remote URL parsing examples using published artifact
- `scrape-test/` - Testing/scraping utilities

## Making Changes

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs

### Testing

- Write tests for all new functionality
- Maintain minimum 70% code coverage (enforced by Kover)
- Run tests locally before submitting PR: `./gradlew test`

### Commits

- Write clear, concise commit messages
- Reference issue numbers when applicable
- Keep commits focused and atomic

## Submitting Changes

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Make your changes
4. Run tests and ensure coverage: `./gradlew test koverVerify`
5. Commit your changes
6. Push to your fork
7. Submit a Pull Request

## Publishing New Versions

This section is for maintainers only.

### Version Numbering

OpenGraphKt follows [Semantic Versioning](https://semver.org/):
- **MAJOR** version for incompatible API changes
- **MINOR** version for backwards-compatible functionality additions
- **PATCH** version for backwards-compatible bug fixes

### Pre-Release Checklist

1. **Update version number** in `opengraphkt/build.gradle.kts`:
   ```kotlin
   version = "0.1.3-SNAPSHOT"  // Change to "0.1.3" for release
   ```

2. **Update demo-remote dependency** in `demo-remote/build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation("fr.lengrand:opengraphkt:0.1.3")  // Match release version
   }
   ```

3. **Run full test suite**:
   ```bash
   ./gradlew clean build test koverVerify
   ```

4. **Test demo applications**:
   ```bash
   ./gradlew :demo:run
   ./gradlew :demo-remote:run
   ```

### Publishing to Maven Central

The project uses the `com.vanniktech.maven.publish` plugin for publishing. Publishing is automated through GitHub Actions.

1. **Bump version ** in `opengraphkt/build.gradle.kts`:
   ```kotlin
   version = "0.1.4"
   ```

2. **Publish to local Maven for testing** (optional):
   ```bash
   ./gradlew publishToMavenLocal
   ```

3. **Create GitHub release** (this triggers the publishing workflow):
   - Go to GitHub repository → Releases → "Draft a new release"
   - Click "Choose a tag" and create a new tag (e.g., `v0.1.3`)
   - Set the release title (e.g., `v0.1.3`)
   - Add release notes describing changes
   - Click "Publish release"

4. **GitHub Actions will automatically**:
   - Build the project
   - Run tests
   - Publish to Maven Central
   - The workflow is triggered automatically when you create a new release

5. After a few minutes, you will see the new version in [Maven Central](https://mvnrepository.com/artifact/fr.lengrand/opengraphkt). You can also directly check the real-time status on [Central Sonartype](https://central.sonatype.com/publishing/deployments).

### Post-Release Steps

1. **Bump version to next SNAPSHOT** in `opengraphkt/build.gradle.kts`:
   ```kotlin
   version = "0.1.5-SNAPSHOT"
   ```

2. **Commit version bump**:
   ```bash
   git commit -am "Bump version to 0.1.5-SNAPSHOT"
   git push
   ```

## Questions?

If you have questions or need help, please:
- Open an issue on GitHub
- Check existing issues and discussions

Thank you for contributing to OpenGraphKt!
