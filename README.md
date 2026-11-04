# Learning Maven 🚀

My day-by-day journey learning **Apache Maven** from scratch, one concept and one project per day.

## 📅 Progress

| Day | Topic | Folder |
|-----|-------|--------|
| 1 | Setup, first project, standard layout, `mvn package` | [hello-maven](./hello-maven) |

---

## Day 1: Hello Maven

### What is Maven?
Maven is a **build automation and dependency management tool** for Java. It's based on **convention over configuration** and driven by a **Project Object Model (POM)**, the `pom.xml` file.

### What I learned
- Installed the JDK and Maven, and checked both with `java -version` and `mvn -v`
- Generated a project using the `maven-archetype-quickstart` archetype
- **GAV coordinates** identify every project:
  - `groupId`: the organisation (`com.anushka`)
  - `artifactId`: the project name (`hello-maven`)
  - `version`: the release version (`1.0-SNAPSHOT`)
- The **standard directory layout**:
  ```
  hello-maven/
  ├── pom.xml
  └── src/
      ├── main/java/   → application code
      └── test/java/   → test code
  ```
- `mvn package` compiles the code, runs the tests, and builds a JAR in `target/`
- `target/` holds build output, so it's kept out of Git with `.gitignore`

### How to run
```bash
cd hello-maven
mvn package
java -cp target/hello-maven-1.0-SNAPSHOT.jar com.anushka.App
```

**Output:**
```
Hello World!
```

---

## 🛠️ Tech
- Java 17+
- Apache Maven 3.9.x
- JUnit 5

## Day 2: Understanding pom.xml

  | 2 | pom.xml anatomy, GAV, properties, dependencies, BOM | [pom-basics](./pom-basics) |

### What I learned
- **GAV** (`groupId:artifactId:version`) uniquely identifies every artifact in the Maven world
- **SNAPSHOT** = in development and can change; release versions (like `1.0`) are immutable
- **`<properties>`** act as variables: `<commons.lang.version>3.17.0</commons.lang.version>` is used as `${commons.lang.version}`
- **`<dependencyManagement>`** only declares versions; **`<dependencies>`** actually adds libraries
- A **BOM** (Bill of Materials), like `junit-bom`, keeps a family of libraries on compatible versions
- **`<pluginManagement>`** pins plugin versions so builds are reproducible
- `mvn dependency:tree` shows every dependency; `mvn help:effective-pom` shows the full resolved POM

### The dependency I added
```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>${commons.lang.version}</version>
</dependency>
```

### The trap 🪤
```bash
java -cp target/pom-basics-1.0-SNAPSHOT.jar com.anushka.App
```
❌ `NoClassDefFoundError: org/apache/commons/lang3/StringUtils`, because a normal JAR contains **only my code, not its dependencies**.

### How to run
```bash
cd pom-basics
mvn compile
mvn exec:java "-Dexec.mainClass=com.anushka.App"
```
**Output:**
```
Capitalized: Google
Reversed:    elgoog
Is blank?    true
```
> 💡 In PowerShell, wrap `-D` arguments in quotes, because PowerShell splits them at the dot.

---

## 🛠️ Tech
- Java 17+
- Apache Maven 3.9.x
- JUnit 5
- Apache Commons Lang 3

## 🎯 Coming next
Day 3: the Maven build lifecycle (`clean`, `validate`, `compile`, `test`, `package`, `verify`, `install`)


## Day 3: The Build Lifecycle

### What I learned
- Maven has **3 lifecycles**: `clean` (deletes `target/`), `default` (builds the project), and `site` (generates docs)
- Default lifecycle phases, in order:
  `validate → compile → test → package → verify → install → deploy`
- **Running a phase runs every phase before it**: `mvn package` also validates, compiles and tests
- **Phases vs goals**: phases are steps; plugin goals do the actual work and are bound to phases
  (e.g. `compiler:compile` → `compile`, `surefire:test` → `test`, `jar:jar` → `package`)
- `mvn install` copies the jar into the **local repository** (`~/.m2/repository`), so other projects on my machine can use it as a dependency
- A **failing test stops the build**, so no jar is produced and broken code never gets packaged
- `-DskipTests` compiles tests but doesn't run them; `-Dmaven.test.skip=true` doesn't even compile them
- `mvn clean install` is the standard everyday build command

### What I built
- `Calculator.java`: add, subtract, multiply, divide (throws `ArithmeticException` on divide by zero)
- `CalculatorTest.java`: 5 JUnit 5 tests, including `assertThrows` for divide by zero

### How to run
```bash
cd lifecycle-demo
mvn clean install                          # build + run all tests
mvn test "-Dtest=CalculatorTest"           # run one test class
mvn test "-Dtest=CalculatorTest#addsNumbers"   # run one test method
java -cp target/lifecycle-demo-1.0-SNAPSHOT.jar com.anushka.App
```
**Output:** `Tests run: 6, Failures: 0` → `BUILD SUCCESS`

### Bonus: dependency scopes
Scope decides which classpath a dependency is on:

| Scope | Main code | Tests | Runtime / shipped | Example |
|---|:-:|:-:|:-:|---|
| `compile` (default) | ✅ | ✅ | ✅ | commons-lang3, Gson |
| `provided` | ✅ | ✅ | ❌ | Servlet API, Lombok |
| `runtime` | ❌ | ✅ | ✅ | MySQL JDBC driver |
| `test` | ❌ | ✅ | ❌ | JUnit, Mockito |
| `import` | BOM versions only (in `dependencyManagement`) | | | junit-bom |

🧪 Experiment: I gave commons-lang3 `test` scope, and `App.java` failed with
`package org.apache.commons.lang3 does not exist`, because test-scoped libraries are invisible to main code.

### Lessons from debugging 🐛
- `File.java:[line,column]` in an error points to the exact spot in the code
- `<identifier> expected` is a **Java syntax error**, not a Maven problem
- `.gitignore` doesn't untrack files that are already committed → `git rm -r --cached <folder>`

## 🎯 Coming next
Day 4: transitive dependencies, dependency conflicts, and exclusions

