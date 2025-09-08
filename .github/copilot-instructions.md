
# GitHub Copilot Agent Mode Repository Instructions

**Repository:** Spring Boot Mortgage Calculator Backend

**Purpose:**
This repository implements a backend REST API for mortgage calculations using Spring Boot. It is designed for use with GitHub Copilot Agent Mode and follows best practices for agent-driven development, build, and validation workflows.

---

## 1. Repository Overview

- **Framework:** Spring Boot (Java)
- **API Documentation:** OpenAPI (via Springdoc)
- **Testing:** JUnit, Mockito
- **Build Tool:** Maven (preferred, validated)
- **Directory Structure:**
  ```
  src/main/java/com/example/mortgage_calculator/         # Backend source code
  src/main/resources/                                    # Application config/resources
  src/test/java/com/example/mortgage_calculator/         # Unit and integration tests
  pom.xml                                                # Maven build file
  mvnw, mvnw.cmd                                         # Maven wrapper scripts
  README.md, LICENSE, .github/
  ```

---

## 2. Agent Mode: Working Effectively

**ALWAYS follow these instructions first.** Only fall back to additional search/context gathering if the information here is incomplete or in error.

### Environment Setup
- Java and Maven are pre-configured in the environment.
- No additional setup is required for standard build/test/run workflows.

### Build, Test, and Run Workflows (Maven)

#### Build and Compile
- Clean and compile: `./mvnw clean compile`
  - **Timing:** ~6 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run (dependency downloads)

#### Run Tests
- Run all tests: `./mvnw test`
  - **Timing:** ~7 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run

#### Full Build with Packaging
- Build JAR: `./mvnw clean package`
  - **Timing:** ~5 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run

#### Run Application
- Start app: `./mvnw spring-boot:run`
  - **Timeout:** 5+ minutes for first run

#### Manual Execution (Validation Required)
After any code change, **ALWAYS**:
1. Build the project from scratch
2. Run the full test suite
3. Start the application and verify it runs (check logs for startup success)

#### Complete Maven Validation Workflow
```bash
# 1. Build and test
./mvnw clean install

# 2. Run application
./mvnw spring-boot:run

# 3. (Optional) Run tests separately
./mvnw test
```

---

## 3. Development Guidelines

### Goal
Implement a REST API endpoint for mortgage calculations:
- Accepts input parameters (principal, interest rate, duration) via POST
- Performs mortgage calculation logic
- Returns results (monthly payment, total interest) as JSON

### Implementation Steps
1. **Project Setup:**
   - Use Spring Initializr with dependencies: Spring Web, Spring Boot DevTools
   - Group ID: `com.example`, Artifact ID: `mortgage-calculator`
2. **API Contract:**
   - Define OpenAPI YAML (`src/main/resources/openapi.yaml`)
   - Use OpenAPI Generator Maven plugin for controllers/models if needed
3. **Business Logic:**
   - Port calculation logic from frontend (see below)
   - Implement as a Spring service class
   - **Mortgage Calculation Formula:**
     - M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
     - Where:
       - M = Monthly payment
       - P = Principal (loan amount)
       - r = Monthly interest rate (annual rate / 12 / 100)
       - n = Total payments (years * 12)
     - If interest rate is 0, M = P / n
     - Validate all inputs are positive
4. **REST Endpoint:**
   - POST `/api/mortgage/calculate`
   - Use `@RestController`, `@RequestBody`, and validation annotations
5. **Frontend Integration:**
   - Frontend should POST to backend and display results
6. **Testing:**
   - Unit tests (JUnit, Mockito) for business logic
   - Integration tests (Spring Boot Test) for API endpoint

### Build and Test Instructions
- **Build:** `./mvnw clean install`
- **Run:** `./mvnw spring-boot:run`
- **Test:** `./mvnw test`

### Coding Standards
- Follow Java and Spring Boot best practices
- Use OpenAPI annotations for all public APIs
- Write unit tests for all business logic and integration tests for endpoints
- Document code and APIs clearly

### Validation Steps
- Test backend API with Postman or cURL
- Ensure frontend integration works (end-to-end test)
- All tests must pass; no lint/type errors
- Handle edge cases (invalid input, large numbers) gracefully
- Use correct HTTP status codes (400 for validation errors, 200 for success)

---

## 4. Agent Mode: Critical Timing and Fallback Logic

**Never cancel** the following operations (let them complete):
- Maven build/test/package/run: allow up to 5 minutes for first run (dependency downloads)
- If a build/test appears to hang, wait at least 5 minutes before reporting an error

**Subsequent builds are much faster** (5-10 seconds)

**If a build or test fails:**
- Re-run with `./mvnw clean test` to rebuild and retest
- If the application does not start, check the logs and ensure the correct JAR is used

---

## 5. Repository State Validation

After any code change, **ALWAYS**:
- Test build and run processes from scratch
- Verify the application starts and produces expected output
- Never commit broken build configurations

---

## 6. Extending and Troubleshooting

- **Adding Docker:** Use `docker build` and `docker run` for containerization. Test all build/run commands and document new timeouts.
- **.gitignore:** Ensure `target/` is ignored for Maven builds
- **Troubleshooting:**
  - If build hangs, wait 5+ minutes for dependencies
  - If tests fail, run `./mvnw clean test`
  - If app won't run, check JAR name in `target/` and logs

---

## 7. Key Points for Copilot Agent Mode

- This repository is a Java/Spring Boot backend for mortgage calculations
- Maven is the validated and preferred build tool
- **Never cancel** long-running builds/tests on first run
- **Always** validate end-to-end functionality (not just compilation)
- **Build times are normal** on first run due to dependency downloads
- **Follow these instructions first**; only fall back to search/context if incomplete


# GitHub Copilot Agent Mode Repository Instructions

**Repository:** Spring Boot Mortgage Calculator Backend

**Purpose:**
This repository implements a backend REST API for mortgage calculations using Spring Boot. It is designed for use with GitHub Copilot Agent Mode and follows best practices for agent-driven development, build, and validation workflows.

---

## 1. Repository Overview

- **Framework:** Spring Boot (Java)
- **API Documentation:** OpenAPI (via Springdoc)
- **Testing:** JUnit, Mockito
- **Build Tool:** Maven (preferred, validated)
- **Directory Structure:**
  ```
  src/main/java/com/example/mortgage_calculator/         # Backend source code
  src/main/resources/                                    # Application config/resources
  src/test/java/com/example/mortgage_calculator/         # Unit and integration tests
  pom.xml                                                # Maven build file
  mvnw, mvnw.cmd                                         # Maven wrapper scripts
  README.md, LICENSE, .github/
  ```

---

## 2. Agent Mode: Working Effectively

**ALWAYS follow these instructions first.** Only fall back to additional search/context gathering if the information here is incomplete or in error.

### Environment Setup
- Java and Maven are pre-configured in the environment.
- No additional setup is required for standard build/test/run workflows.

### Build, Test, and Run Workflows (Maven)

#### Build and Compile
- Clean and compile: `./mvnw clean compile`
  - **Timing:** ~6 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run (dependency downloads)

#### Run Tests
- Run all tests: `./mvnw test`
  - **Timing:** ~7 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run

#### Full Build with Packaging
- Build JAR: `./mvnw clean package`
  - **Timing:** ~5 seconds (after dependencies cached)
  - **Timeout:** 5+ minutes for first run

#### Run Application
- Start app: `./mvnw spring-boot:run`
  - **Timeout:** 5+ minutes for first run

#### Manual Execution (Validation Required)
After any code change, **ALWAYS**:
1. Build the project from scratch
2. Run the full test suite
3. Start the application and verify it runs (check logs for startup success)

#### Complete Maven Validation Workflow
```bash
# 1. Build and test
./mvnw clean install

# 2. Run application
./mvnw spring-boot:run

# 3. (Optional) Run tests separately
./mvnw test
```

---

## 3. Development Guidelines

### Goal
Implement a REST API endpoint for mortgage calculations:
- Accepts input parameters (principal, interest rate, duration) via POST
- Performs mortgage calculation logic
- Returns results (monthly payment, total interest) as JSON

### Implementation Steps
1. **Project Setup:**
   - Use Spring Initializr with dependencies: Spring Web, Spring Boot DevTools
   - Group ID: `com.example`, Artifact ID: `mortgage-calculator`
2. **API Contract:**
   - Define OpenAPI YAML (`src/main/resources/openapi.yaml`)
   - Use OpenAPI Generator Maven plugin for controllers/models if needed
3. **Business Logic:**
   - Port calculation logic from frontend (see below)
   - Implement as a Spring service class
   - **Mortgage Calculation Formula:**
     - M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
     - Where:
       - M = Monthly payment
       - P = Principal (loan amount)
       - r = Monthly interest rate (annual rate / 12 / 100)
       - n = Total payments (years * 12)
     - If interest rate is 0, M = P / n
     - Validate all inputs are positive
4. **REST Endpoint:**
   - POST `/api/mortgage/calculate`
   - Use `@RestController`, `@RequestBody`, and validation annotations
5. **Frontend Integration:**
   - Frontend should POST to backend and display results
6. **Testing:**
   - Unit tests (JUnit, Mockito) for business logic
   - Integration tests (Spring Boot Test) for API endpoint

### Build and Test Instructions
- **Build:** `./mvnw clean install`
- **Run:** `./mvnw spring-boot:run`
- **Test:** `./mvnw test`

### Coding Standards
- Follow Java and Spring Boot best practices
- Use OpenAPI annotations for all public APIs
- Write unit tests for all business logic and integration tests for endpoints
- Document code and APIs clearly

### Validation Steps
- Test backend API with Postman or cURL
- Ensure frontend integration works (end-to-end test)
- All tests must pass; no lint/type errors
- Handle edge cases (invalid input, large numbers) gracefully
- Use correct HTTP status codes (400 for validation errors, 200 for success)

---

## 4. Agent Mode: Critical Timing and Fallback Logic

**Never cancel** the following operations (let them complete):
- Maven build/test/package/run: allow up to 5 minutes for first run (dependency downloads)
- If a build/test appears to hang, wait at least 5 minutes before reporting an error

**Subsequent builds are much faster** (5-10 seconds)

**If a build or test fails:**
- Re-run with `./mvnw clean test` to rebuild and retest
- If the application does not start, check the logs and ensure the correct JAR is used

---

## 5. Repository State Validation

After any code change, **ALWAYS**:
- Test build and run processes from scratch
- Verify the application starts and produces expected output
- Never commit broken build configurations

---

## 6. Extending and Troubleshooting

- **Adding Docker:** Use `docker build` and `docker run` for containerization. Test all build/run commands and document new timeouts.
- **.gitignore:** Ensure `target/` is ignored for Maven builds
- **Troubleshooting:**
  - If build hangs, wait 5+ minutes for dependencies
  - If tests fail, run `./mvnw clean test`
  - If app won't run, check JAR name in `target/` and logs

---

## 7. Key Points for Copilot Agent Mode

- This repository is a Java/Spring Boot backend for mortgage calculations
- Maven is the validated and preferred build tool
- **Never cancel** long-running builds/tests on first run
- **Always** validate end-to-end functionality (not just compilation)
- **Build times are normal** on first run due to dependency downloads
- **Follow these instructions first**; only fall back to search/context if incomplete