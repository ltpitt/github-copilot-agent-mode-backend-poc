# GitHub Copilot Agent Mode Backend POC

Backend proof-of-concept repository for testing GitHub Copilot agent mode techniques and best practices.

**ALWAYS follow these instructions first** and only fall back to additional search and context gathering if the information here is incomplete or found to be in error.

## Working Effectively

### Environment Setup
The development environment is pre-configured with:
- Java 17 (OpenJDK Runtime Environment Temurin-17.0.16+8)
- Maven 3.9.11 (/usr/bin/mvn)
- Gradle 9.0.0 (/usr/bin/gradle)
- Node.js 20.19.4 (/usr/local/bin/node) with npm 10.8.2
- Python 3.12.3 (/usr/bin/python3)
- Docker 28.0.4 (/usr/bin/docker)

### Repository Structure
This is currently a minimal repository containing:
```
.
├── README.md        # Basic project description
├── LICENSE          # MIT License
└── .gitignore       # Java-focused (*.class, *.jar, *.log, etc.)
```

### Creating a Java Project

#### Using Maven (Validated)
Generate a new Maven project:
```bash
mvn archetype:generate -DgroupId=com.example.poc -DartifactId=backend-poc -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -B
```
**Timing:** 12 seconds on first run (includes Maven dependency downloads)

#### Using Gradle (Validated)
Generate a new Gradle project:
```bash
gradle init --type java-application --dsl groovy --test-framework junit --project-name backend-poc --package com.example.poc --no-split-project --java-version 17
```
**Timing:** 5+ minutes on first run (includes Gradle daemon startup and wrapper download). **NEVER CANCEL** - Set timeout to 10+ minutes.

### Building and Testing

#### Maven Commands (All Validated)
- **Clean and compile:** `mvn clean compile`
  - **Timing:** 6 seconds (after dependencies cached)
  - **NEVER CANCEL** - Set timeout to 5+ minutes for first run
- **Run tests:** `mvn test`
  - **Timing:** 7 seconds (after dependencies cached)
  - **NEVER CANCEL** - Set timeout to 5+ minutes for first run
- **Full build with packaging:** `mvn clean package`
  - **Timing:** 5 seconds (after dependencies cached)
  - **NEVER CANCEL** - Set timeout to 5+ minutes for first run
- **Run application:** `java -cp target/[artifact-name]-[version].jar com.example.poc.App`

#### Gradle Commands (All Validated)
- **Build project:** `./gradlew build`
  - **Timing:** 16 seconds (after Gradle cached)
  - **NEVER CANCEL** - Set timeout to 10+ minutes for first run with daemon startup
- **Run tests:** `./gradlew test`
  - **Timing:** Included in build process
- **Run application:** `./gradlew run`
  - **Timing:** 1 second (after build cached)
  - **NEVER CANCEL** - Set timeout to 5+ minutes

### Validation Requirements

#### Manual Testing Steps
After creating any Java project:
1. **ALWAYS** test the complete build process from scratch
2. **ALWAYS** run the test suite to ensure it passes
3. **ALWAYS** execute the application to verify it runs correctly
4. Expected output for default projects: "Hello World!"

#### Complete Maven Validation Workflow
```bash
# 1. Create project (12s)
mvn archetype:generate -DgroupId=com.example.poc -DartifactId=backend-poc -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -B

# 2. Build and test (6s each)
cd backend-poc
mvn clean compile
mvn test
mvn clean package

# 3. Run application
java -cp target/backend-poc-1.0-SNAPSHOT.jar com.example.poc.App
```

#### Complete Gradle Validation Workflow
```bash
# 1. Create project (5+ minutes - NEVER CANCEL)
gradle init --type java-application --dsl groovy --test-framework junit --project-name backend-poc --package com.example.poc --no-split-project --java-version 17

# 2. Build and test (16s first run)
./gradlew build

# 3. Run application (1s)
./gradlew run
```

### Critical Timing and Timeout Guidelines

**NEVER CANCEL any of these operations:**
- Maven archetype generation: 30s timeout minimum
- Maven first build: 300s (5 minutes) timeout minimum
- Gradle init: 600s (10 minutes) timeout minimum
- Gradle first build: 600s (10 minutes) timeout minimum

**Subsequent builds are much faster:**
- Maven builds: 5-10 seconds
- Gradle builds: 1-5 seconds (with daemon running)

### Common Development Tasks

#### Starting a New Java Backend
1. Choose Maven (faster setup) or Gradle (more flexible builds)
2. Use the validated commands above with appropriate timeouts
3. **ALWAYS** run through the complete validation workflow
4. **NEVER** skip the manual execution test

#### Extending to Other Technologies
While this repository is Java-focused, the environment supports:
- **Node.js projects:** Use `npm init` to create package.json
- **Python projects:** Use `python3 -m venv` for virtual environments
- **Docker projects:** Use `docker build` and `docker run` for containerization
- **Multi-language projects:** Can combine Java backend with Node.js frontend

**Note:** When adding non-Java components, test all build and run commands thoroughly and document new timeout requirements.

#### Code Quality
The repository uses Java-focused .gitignore. When adding build tools:
- Maven: Ensure `target/` directory is ignored
- Gradle: Ensure `.gradle/` and `build/` directories are ignored

### Troubleshooting

#### Maven Issues
- **Build hangs:** Wait at least 5 minutes on first run for dependency downloads
- **Tests fail:** Run `mvn clean test` to rebuild and retest
- **Application won't run:** Check the exact JAR filename in `target/` directory

#### Gradle Issues  
- **Init hangs:** Wait at least 10 minutes for daemon startup and downloads
- **Build hangs:** Wait at least 10 minutes on first run
- **Daemon issues:** Use `./gradlew --stop` to stop daemon if needed

### Repository State Validation
Current repository contains only basic files. After adding any code:
- **ALWAYS** test build processes work correctly
- **ALWAYS** verify application runs and produces expected output
- **NEVER** commit broken build configurations

## Key Points for Agent Mode
- This repository is designed for Java backend development
- Both Maven and Gradle are fully supported and validated
- **Critical:** Never cancel long-running builds - they complete successfully
- Always validate end-to-end functionality, not just compilation
- Build times are normal and expected on first runs due to dependency downloads