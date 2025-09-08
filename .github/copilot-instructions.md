# GitHub Copilot Agent Mode Repository Instructions

**Repository**: Spring Boot Mortgage Calculator Backend

## Purpose

This repository implements a backend REST API for mortgage calculations using Spring Boot. It is designed for use with GitHub Copilot Agent Mode and follows best practices for agent-driven development, build, and validation workflows.

---

## 1. Repository Overview

- **Framework**: Spring Boot (Java)
- **API Documentation**: OpenAPI (via Springdoc)
- **Testing**: JUnit, Mockito
- **Build Tool**: Maven (preferred, validated)

**Directory Structure**:
```
src/main/java/com/example/mortgage_calculator/     # Backend source code  
src/main/resources/                                # Application config/resources  
src/test/java/com/example/mortgage_calculator/     # Unit and integration tests  
pom.xml                                            # Maven build file  
mvnw, mvnw.cmd                                     # Maven wrapper scripts  
README.md, LICENSE, .github/                       # Metadata and GitHub config  
```

---

## 2. Agent Mode: Working Effectively

**Environment Setup**
- Java and Maven are pre-configured.
- No additional setup required for standard workflows.

**Build, Test, and Run Workflows (Maven)**
- **Build and Compile**
  ```
  ./mvnw clean compile
  ```
  Timing: ~6s (cached), Timeout: ~5+ min (first run)

- **Run Tests**
  ```
  ./mvnw test
  ```
  Timing: ~7s (cached), Timeout: ~5+ min (first run)

- **Full Build with Packaging**
  ```
  ./mvnw clean package
  ```

- **Run Application**
  ```
  ./mvnw spring-boot:run
  ```

**Manual Execution (Validation Required)**  
After any code change, ALWAYS:
1. Build the project from scratch
2. Run the full test suite
3. Start the application and verify logs

**Complete Maven Validation Workflow**
```
# 1. Build and test
./mvnw clean install
# 2. Run application
./mvnw spring-boot:run

# 3. (Optional) Run tests separately
./mvnw test
```

---

## 3. Development Guidelines

**Goal**  
Implement a REST API endpoint for mortgage calculations:
- Accepts input parameters via POST
- Performs mortgage calculation logic
- Returns results as JSON

### API Payload Specification (MUST MATCH FRONTEND)

**Request Payload (POST /api/mortgage/calculate)**
```json
{
  "loanAmount": number,
  "interestRate": number,
  "loanTermYears": number,
  "livingSituation": "alone" | "together",
  "mainIncome": number,
  "partnerIncome": number,
  "energyLabel": "A" | "B" | "C" | "D" | "E" | "F" | "G",
  "fixedInterestPeriod": number
}
```

**Field Descriptions**:
- `loanAmount`: Principal amount (required, > 0)
- `interestRate`: Annual interest rate (%) (required, >= 0)
- `loanTermYears`: Loan term in years (required, > 0)
- `livingSituation`: "alone" or "together" (required)
- `mainIncome`: Gross annual income (required, > 0)
- `partnerIncome`: Optional, >= 0
- `energyLabel`: Required, one of "A" to "G"
- `fixedInterestPeriod`: Years (e.g., 10, 20, 30) (required, > 0)

**Validation Rules**:
- All fields except `partnerIncome` are required.
- All numeric values must be positive.
- Return HTTP 400 for invalid/missing fields.

**Response Payload**
```json
{
  "monthlyPayment": number,
  "totalInterest": number,
  "maxBorrowing": number,
  "advice": string
}
```

### Implementation Steps

**Project Setup**
- Use Spring Initializr with: Spring Web, Spring Boot DevTools
- Group ID: `com.example`, Artifact ID: `mortgage-calculator`

**API Contract**
- Define OpenAPI YAML: `src/main/resources/openapi.yaml`
- Use OpenAPI Generator Maven plugin if needed

**Business Logic**
- Port calculation logic from frontend
- Implement as a Spring service class

**Mortgage Calculation Formula**
```
M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
```
Where:
- M = Monthly payment
- P = Principal
- r = Monthly interest rate (annual / 12 / 100)
- n = Total payments (years * 12)
- If interest rate is 0: `M = P / n`

**Max Borrowing Calculation**
- Alone: `mainIncome * 4.5`
- Together: `(mainIncome + partnerIncome) * 4.5`
- Energy label adjustment (Dutch 2025 rules):
    - A/B: add €10,000
    - C/D: no adjustment
    - E/F/G: subtract between €5,500 and €30,000 depending on income
    - This function implements **Dutch 2025 rules** for energy label impact on borrowing capacity.

**REST Endpoint**
- `POST /api/mortgage/calculate`
- Use `@RestController`, `@RequestBody`, validation annotations

**Frontend Integration**
- Frontend should POST to backend and display results

**Testing**
- Unit tests: JUnit, Mockito
- Integration tests: Spring Boot Test

**Build and Test Instructions**
```
Build: ./mvnw clean install  
Run: ./mvnw spring-boot:run  
Test: ./mvnw test
```

**Coding Standards**
- Follow Java and Spring Boot best practices
- Use OpenAPI annotations
- Write unit and integration tests
- Document code and APIs clearly

**Validation Steps**
- Test with Postman or cURL
- Ensure frontend integration works
- All tests must pass
- Handle edge cases gracefully
- Use correct HTTP status codes

---

## 4. Agent Mode: Critical Timing and Fallback Logic
**Never cancel the following operations**:
- Maven build/test/package/run: allow up to 5 minutes for first run

**If build/test fails**:
- Re-run with:
  ```
  ./mvnw clean test
  ```
- Check logs and JAR name in `target/`

---

## 5. Repository State Validation

After any code change, ALWAYS:
- Test build and run from scratch
- Verify application starts and outputs correctly
- Never commit broken build configurations
---

## 6. Extending and Troubleshooting

**Adding Docker**
- Use `docker build` and `docker run`
- Document new timeouts

**.gitignore**
- Ensure `target/` is ignored
  **Troubleshooting**:
- If build hangs: wait 5+ minutes
- If tests fail: `./mvnw clean test`
- If app won't run: check JAR name and logs
---

## 7. Key Points for Copilot Agent Mode

- This repository is a Java/Spring Boot backend for mortgage calculations
- Maven is the validated and preferred build tool
- Never cancel long-running builds/tests on first run
- Always validate end-to-end functionality (not just compilation)
- Build times are normal on first run due to dependency downloads
- Follow these instructions first; only fall back to search/context if incomplete
