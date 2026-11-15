# 🎓 Student Grade CLI

A command-line tool that calculates a student's **SGPA** (and soon **CGPA**) from their courses, credits and grades, built with **Java 17** and **Maven**, with unit tests for every class.

> 🚧 **Work in progress**: built step by step as my first real project after learning Maven. See the [roadmap](#-roadmap) below.

---

## ✨ Features (so far)
- Converts grade letters to grade points on a **10-point scale**
- Validates every course: a name is required, credits must be positive, the grade must be valid
- Calculates the **SGPA** as a credit-weighted average, rounded to 2 decimals
- Prints a neat semester report
- Unit tests for every core class (JUnit 5)

---

## 📊 Grade scale

| Grade | O | A+ | A | B+ | B | C | P | F |
|-------|---|----|---|----|---|---|---|---|
| Points | 10 | 9 | 8 | 7 | 6 | 5 | 4 | 0 |

**SGPA formula:**
```
SGPA = Σ (credits × grade points) / Σ credits
```

---

## 🚀 How to run

**Requirements:** Java 17+ and Maven 3.9+

```bash
# run all tests
mvn clean test

# build and run
mvn clean compile
java -cp target/classes com.college.App
```

**Sample output:**
```
===== Semester Report =====
Data Structures      4 credits   O   -> 40 points
DBMS                 3 credits   A+  -> 27 points
Operating Systems    3 credits   A   -> 24 points
Maths                4 credits   B+  -> 28 points
Java Lab             2 credits   O   -> 20 points
---------------------------
SGPA: 8.69
```

---

## 🏗️ Design

Each class has **one job** (Single Responsibility Principle):

| Class | Responsibility |
|-------|----------------|
| `Grade` | Enum mapping grade letters (O, A+, …) to points; rejects invalid grades |
| `Course` | Immutable record (name, credits, grade) that **validates itself** on creation |
| `GpaCalculator` | Calculates the credit-weighted SGPA |
| `App` | Entry point: gets input, calls the calculator, prints the report (kept thin) |

```
App  →  GpaCalculator  →  Course  →  Grade
```

### Design decisions
- **`enum` for grades** → invalid grades like `"A++"` can't exist; the compiler helps catch mistakes
- **`record` with a compact constructor** → course data is immutable and validated in one place
- **Test-Driven Development** → tests were written first as the specification, then the code was written to make them pass
- **Thin `App`** → all logic lives in testable classes; `App` only coordinates

---

## 🧪 Tests

| Test class | What it checks |
|------------|----------------|
| `GradeTest` | All 8 grades map to the right points (parameterized), lowercase/spaces accepted, invalid/null grades rejected |
| `CourseTest` | Grade-point math, and rejection of zero credits, blank names and missing grades |
| `GpaCalculatorTest` | Credit-weighted SGPA, rounding to 2 decimals, empty semester rejected |

```bash
mvn clean test
```

---

## 🗺️ Roadmap
- [x] Grade scale with validation
- [x] Course model with self-validation
- [x] SGPA calculation + unit tests
- [ ] Interactive input: the user types their courses
- [ ] Read courses from a **CSV file**
- [ ] **CGPA** across multiple semesters
- [ ] **Target calculator**: "what SGPA do I need next semester to reach a 9.0 CGPA?"
- [ ] Proper CLI commands (`sgpa`, `cgpa`, `target`) with picocli
- [ ] Executable **fat JAR** (`java -jar grade-cli.jar`)
- [ ] **JaCoCo** coverage gate
- [ ] **GitHub Actions CI** + badge
- [ ] Maven Wrapper (`./mvnw`)

---

## 🛠️ Tech
- Java 17 (records, enums, switch expressions)
- Apache Maven
- JUnit 5 (incl. parameterized tests)

---

## 👩‍💻 Author
**Anushka**, B.Tech CSE, Dayananda Sagar University
Built while learning Maven → see my learning repo: [MAVEN](https://github.com/Anu5156/MAVEN)