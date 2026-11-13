# Learning Maven 🚀
   [![Maven CI](https://github.com/Anu5156/MAVEN/actions/workflows/maven.yml/badge.svg)](https://github.com/Anu5156/MAVEN/actions/workflows/maven.yml)

My day-by-day journey learning **Apache Maven** from scratch, one concept and one project per day.

## 📅 Progress

| Day | Topic | Folder |
|-----|-------|--------|
| 1 | Setup, first project, standard layout, `mvn package` | [hello-maven](./hello-maven) |
| 2 | `pom.xml` anatomy, GAV, properties, dependencies, BOM | [pom-basics](./pom-basics) |
| 3 | Build lifecycle, JUnit 5 tests, dependency scopes | [lifecycle-demo](./lifecycle-demo) |
| 4 | Transitive dependencies, version conflicts, BOM fix, exclusions | [dependency-demo](./dependency-demo) |
| 5 | Plugins, executions, manifest, fat JAR with maven-shade-plugin | [java-packaging](./java-packaging) |
| 6 | Unit vs integration tests, Surefire, Failsafe, JaCoCo coverage gate | [java-testing](./java-testing) |
| 7 | Multi-module project: parent POM, modules, inheritance, reactor | [multi-module-project](./multi-module-project) |
| 8 | CI with GitHub Actions: workflow, jobs, matrix builds, caching, badge | [.github/workflows](./.github/workflows) |
| 9 | Maven Wrapper (mvnw), profiles, activation, resource filtering | [maven-profiles](./maven-profiles) |
| 10 | Interview prep: rapid-fire quiz, scenario questions, cheat sheet | [INTERVIEW_NOTES.md](./INTERVIEW_NOTES.md) |
| 🧪 | Practice: basic app with JUnit 5 tests (built on my own) | [basic-application](./basic-application) |
| 🧪 | Practice: web application packaged as a WAR (built on my own) | [web-app-creation](./web-app-creation) |

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

## Day 2: Understanding `pom.xml`

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
mvn clean install                              # build + run all tests
mvn test "-Dtest=CalculatorTest"               # run one test class
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

---

## Day 4: Transitive Dependencies & Conflicts

### What I learned
- **Transitive dependencies**: dependencies of my dependencies are downloaded automatically (I declared 2 libraries, and `mvn dependency:tree` showed about 10)
- Only `compile` and `runtime` scoped dependencies are passed on; `test` and `provided` are not
- **Dependency mediation** decides which version wins in a conflict:
  - **Nearest wins**: the version closest to my project in the tree
  - **Tie → first declared wins** in the pom
  - Maven does **NOT** pick the newest version; a wrong version can cause `NoSuchMethodError` at runtime
- Three ways to fix a conflict:
  1. Declare the dependency directly (depth 1 always wins)
  2. Pin it with `<dependencyManagement>` / a BOM ✅ (best practice)
  3. Remove it with `<exclusions>`
- All jars live in `~/.m2/repository/<groupId>/<artifactId>/<version>/`, shared across projects
- `mvn -o` = offline mode, `mvn -U` = force update of SNAPSHOTs

### `pom.xml` rules I learned 📋
- Each section (`<properties>`, `<dependencyManagement>`, `<dependencies>`, `<build>`) appears only **once**
- Each library is declared only **once**
- `...Management` blocks = versions only; plain blocks = actually used
- BOMs have a version (`<type>pom</type>` + `<scope>import</scope>`); libraries managed by a BOM don't
- An `<exclusion>` has only `groupId` + `artifactId`, with no version

### What I built
- Serialized a Java record to JSON using **Jackson**, with an `ImmutableList` from **Google Guava**
- Created a conflict on purpose (`jackson-core 2.15.0` vs `2.18.0`) and saw "nearest wins" in the verbose tree
- Fixed it with `jackson-bom 2.18.0`, so all Jackson modules stay on the same version
- Excluded `j2objc-annotations` (only needed for iOS builds) from Guava

### Key part of the `pom.xml`
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.fasterxml.jackson</groupId>
      <artifactId>jackson-bom</artifactId>
      <version>2.18.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>   <!-- version comes from the BOM -->
  </dependency>
  <dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>33.3.1-jre</version>
    <exclusions>
      <exclusion>
        <groupId>com.google.j2objc</groupId>
        <artifactId>j2objc-annotations</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
</dependencies>
```

### How to run
```bash
cd dependency-demo
mvn clean compile exec:java "-Dexec.mainClass=com.anushka.App"
mvn dependency:tree
mvn dependency:tree "-Dverbose" "-Dincludes=com.fasterxml.jackson.core"
mvn dependency:analyze
```

**Output:**
```
{"name":"Anushka","dreamCompanies":["Google","Amazon","Microsoft"]}
```

---

## 🛠️ Tech
- Java 17+
- Apache Maven 3.9.x
- JUnit 5
- Apache Commons Lang 3
- Jackson 2.18.x
- Google Guava 33.3.1

## Day 5: Plugins & the Fat JAR

### What I learned
- **Plugins do all the work** in Maven; each plugin has **goals** bound to lifecycle **phases**
- `<pluginManagement>` pins versions only; `<build><plugins>` actually uses or configures plugins
  (same idea as `dependencyManagement` vs `dependencies`)
- `<configuration>` = plugin settings; `<executions>` = run a goal automatically during a `<phase>`
- If a plugin's version is already pinned in `<pluginManagement>`, it can be used without `<version>`
- Official plugins use `groupId` `org.apache.maven.plugins` (optional to write); others like `org.codehaus.mojo` need it
- `java -jar` reads `META-INF/MANIFEST.MF` to find the `Main-Class`
- A normal jar has **no dependencies** inside; a **fat (uber) jar** bundles my code **plus all dependencies**
- **Plugin prefixes**: `exec:java` → `exec-maven-plugin`, `dependency:tree` → `maven-dependency-plugin`
- `mvn help:describe "-Dplugin=shade"` lists a plugin's goals

### The two errors I fixed 🐛
| Error | Cause | Fix |
|---|---|---|
| `no main manifest attribute` | Manifest has no `Main-Class` | Set `<mainClass>` (jar plugin / shade transformer) |
| `NoClassDefFoundError: .../ObjectMapper` | Jackson not inside the jar | Build a fat jar with `maven-shade-plugin` |

### Key part of the `pom.xml`
```xml
<build>
  <pluginManagement>
    <!-- versions only -->
  </pluginManagement>

  <plugins>   <!-- plugins actually used: AFTER pluginManagement, not inside it -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.6.0</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals><goal>shade</goal></goals>
          <configuration>
            <createDependencyReducedPom>false</createDependencyReducedPom>
            <transformers>
              <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                <mainClass>com.anushka.App</mainClass>
              </transformer>
            </transformers>
            <filters>
              <filter>
                <artifact>*:*</artifact>
                <excludes>
                  <exclude>module-info.class</exclude>
                  <exclude>META-INF/*.SF</exclude>
                  <exclude>META-INF/*.DSA</exclude>
                  <exclude>META-INF/*.RSA</exclude>
                </excludes>
              </filter>
            </filters>
          </configuration>
        </execution>
      </executions>
    </plugin>

    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.5.0</version>
      <configuration>
        <mainClass>com.anushka.App</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### How to run
```bash
cd java-packaging
mvn clean package
java -jar target/java-packaging-1.0-SNAPSHOT.jar   # no Maven needed to run it!
mvn compile exec:java                              # mainClass configured in the pom
```

**Output:**
```
{"name":"Anushka","dreamCompanies":["Google","Amazon","Microsoft"]}
```

| Jar in `target/` | Size | Contents |
|---|---|---|
| `original-java-packaging-1.0-SNAPSHOT.jar` | KB | only my code |
| `java-packaging-1.0-SNAPSHOT.jar` | MB | my code + Jackson + Guava 🍔 |

### Real-world notes
- `maven-assembly-plugin` can also build fat jars; Shade can additionally **relocate** packages to avoid conflicts
- Spring Boot apps use `spring-boot-maven-plugin` for their executable jars
- Fat jars are for **apps** (CLI tools, microservices), not for **libraries** others depend on

---

## Day 6: Testing Like a Google Engineer 🧪

### What I learned
- **Unit tests** (`*Test.java`) run with **Surefire** in the `test` phase: fast, one class at a time
- **Integration tests** (`*IT.java`) run with **Failsafe** in `integration-test` + `verify`
- Maven picks the plugin **by file name**: a test named `CalculatorCheck.java` would never run!
- Failsafe records failures in `integration-test` and fails the build only in `verify`, so `post-integration-test` cleanup always runs
- **JaCoCo** measures code coverage: 🟢 covered, 🔴 missed, 🟡 partially covered branches
- `prepare-agent` attaches the JaCoCo agent; `report` writes HTML; `check` enforces a minimum (a **quality gate**)
- Multiple executions of one plugin each need a unique `<id>`
- `src/test/resources` files are copied to `target/test-classes` and loaded with `getResourceAsStream`
- 100% coverage ≠ bug-free: it shows which lines ran, not whether they were checked correctly

### JUnit 5 features I used
| Feature | Purpose |
|---|---|
| `@ParameterizedTest` + `@CsvSource` | One test method, many input rows |
| `@DisplayName` | Readable test names |
| `assertThrows` | Verify exceptions (and their messages) |
| `assertEquals(expected, actual, 0.001)` | Compare doubles with a tolerance |
| Boundary values (`8.5`, `8.49`) | Test right at the edge of a condition |

### What I built
- `GradeCalculator`: grade → points (O=10 … F=0), credit-weighted **CGPA**, distinction check
- `GradeCalculatorTest`: unit tests including parameterized and exception tests
- `GradeCalculatorIT`: integration test reading `semester1.csv` from test resources (CGPA 8.69)
- A JaCoCo rule requiring **≥ 90% line coverage**: the build failed until I tested every branch

### Key part of the `pom.xml`
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-failsafe-plugin</artifactId>
  <version>3.5.2</version>
  <executions>
    <execution>
      <goals><goal>integration-test</goal><goal>verify</goal></goals>
    </execution>
  </executions>
</plugin>

<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.15</version>
  <executions>
    <execution><id>prepare-agent</id><goals><goal>prepare-agent</goal></goals></execution>
    <execution><id>report</id><phase>test</phase><goals><goal>report</goal></goals></execution>
    <execution>
      <id>check</id>
      <goals><goal>check</goal></goals>
      <configuration>
        <rules>
          <rule>
            <element>BUNDLE</element>
            <limits>
              <limit><counter>LINE</counter><value>COVEREDRATIO</value><minimum>0.90</minimum></limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### How to run
```bash
cd java-testing
mvn clean test                     # unit tests + coverage report
mvn clean verify                   # unit + integration tests + coverage gate
start target\site\jacoco\index.html   # open coverage report (Windows)
mvn verify "-DskipITs"             # skip integration tests
```
**Output:** `BUILD SUCCESS` with ≥ 90% line coverage; the app prints `CGPA: 9.1 (Distinction)`

---

## Day 7: Multi-Module Projects 🏗️

### What I learned
- A **parent POM** has `<packaging>pom</packaging>`: it holds shared rules and has no code
- **`<modules>`** (in the parent) lists the children by folder name, so one command builds them all
- **`<parent>`** (in the child) inherits groupId, version, properties and dependencies
- Each child needs only its own **`artifactId`**
- JUnit added **once** in the parent was available in every child
- A module can **depend on another module** (`greeting-app` → `greeting-core`)
- The **reactor** builds in dependency order: core first, even though I listed the app first
- If a module fails, modules that depend on it are **SKIPPED**
- `mvn test -pl greeting-core` builds only one module

### Structure
```
multi-module-project/     (parent, packaging: pom)
├── greeting-core/        (Greeter + tests)
└── greeting-app/         (App, depends on greeting-core)
```

### How to run
```bash
cd multi-module-project
mvn clean install
java -cp "greeting-app/target/classes;greeting-core/target/classes" com.anushka.app.App
```
**Output:**
```
Hello, Anushka!
Hello, stranger!
```

---

## Day 8: CI with GitHub Actions 🤖

### What I learned
- **CI (Continuous Integration)** builds and tests every push on a **fresh machine**, so it catches "works on my machine" bugs
- A **workflow** lives in `.github/workflows/*.yml`
- Vocabulary: **trigger** (`on:`), **job**, **runner** (`ubuntu-latest`), **step**, **action** (`uses:`)
- A **matrix** runs the same job for many values: here, **9 projects in parallel**
- `fail-fast: false` keeps the other jobs running when one fails
- `actions/setup-java` with `cache: maven` caches `~/.m2`, so builds get faster
- `mvn -B -ntp verify`: batch mode, no download noise, full lifecycle up to `verify`
- **Branch → Pull Request → CI check → merge**: broken code is blocked before it reaches `main`
- YAML: indentation is structure, spaces only (no tabs)

### The workflow
```yaml
name: Maven CI
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    name: Build ${{ matrix.project }}
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      matrix:
        project: [hello-maven, pom-basics, lifecycle-demo, dependency-demo,
                  java-packaging, java-testing, multi-module-project,
                  basic-application, web-app-creation]
    defaults:
      run:
        working-directory: ${{ matrix.project }}
    steps:
      - uses: actions/checkout@v7
      - uses: actions/setup-java@v6
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
      - run: mvn -B -ntp verify
```

### What I tried
- Pushed → watched 9 jobs run in parallel in the **Actions** tab
- Opened a PR with a deliberately broken test → CI turned ❌ and blocked it → closed the PR without merging

---

## Day 9: Maven Wrapper & Profiles 🎛️

### Maven Wrapper
- `mvnw` / `mvnw.cmd` + `.mvn/wrapper/maven-wrapper.properties` pin the **Maven version per project**
- New developers and CI don't need Maven installed: `./mvnw clean verify`
- Generated with `mvn wrapper:wrapper "-Dmaven=3.9.11"` (added to `multi-module-project`)
- On Windows, mark it executable for Linux CI: `git update-index --chmod=+x mvnw`

### Resource filtering
- Without filtering, Maven copies resources as-is, so the app printed `${app.env}` literally 🪤
- `<filtering>true</filtering>` replaces `${...}` while copying `src/main/resources` → `target/classes`
- The source file keeps the placeholders; only the built copy gets real values

### Profiles
- A **profile** = a named set of overrides inside one pom (properties, dependencies, plugins)
- Default values live in `<properties>`; a profile replaces them while it is ON
- Activate by hand: `-Pdev`, `-Pprod`
- Activate automatically with `<activation>`: my `ci` profile turns on when the env variable `CI` exists (GitHub Actions sets `CI=true`)
- `mvn help:active-profiles` shows which profiles are on
- `-q` (quiet) hides Maven's `[INFO]` logs

### How to run
```bash
cd maven-profiles
mvn -q clean compile exec:java "-Dexec.mainClass=com.anushka.App"          # Environment : local
mvn -q clean compile exec:java "-Dexec.mainClass=com.anushka.App" -Pdev    # Environment : dev
mvn -q clean compile exec:java "-Dexec.mainClass=com.anushka.App" -Pprod   # Environment : prod
```
Simulating CI on Windows:
```powershell
$env:CI = "true"
mvn help:active-profiles        # - ci
Remove-Item Env:CI
```

---
## Day 10: Interview Prep 🎤

### What I did
- Took a **10-question rapid-fire quiz** covering Days 1–9 (lifecycle, mediation, scopes, test naming, manifests, reactor, wrapper, filtering)
- Practised **Google-style scenario questions**: debugging a `NoSuchMethodError`, designing the Maven build for a distributed system, and explaining `mvn clean install` end to end
- Wrote a revision cheat sheet: **[INTERVIEW_NOTES.md](./INTERVIEW_NOTES.md)**

---

## 🎓 Course Complete: What I Can Do Now
- Create, build, test and package Java projects with **Maven**
- Read and write a `pom.xml`: GAV, properties, dependencies, scopes, BOMs, plugins
- Debug dependency conflicts with `dependency:tree` and fix them with BOMs and exclusions
- Ship runnable **fat JARs** with `maven-shade-plugin`
- Separate **unit and integration tests** (Surefire / Failsafe) and enforce coverage with **JaCoCo**
- Structure **multi-module** projects with a parent POM and the reactor
- Run **CI on every push** with GitHub Actions (matrix of 10 projects)
- Pin Maven with the **wrapper** and switch environments with **profiles**

## 🛠️ Tech
- Java 17+
- Apache Maven 3.9.x
- JUnit 5
- Apache Commons Lang 3
- Jackson 2.18.x
- Google Guava 33.3.1
- maven-shade-plugin, exec-maven-plugin
- Maven Surefire & Failsafe, JaCoCo
- GitHub Actions, Maven Wrapper

## 🎯 Coming next
Building real projects with these skills, starting with **grade-cli**: a CGPA command-line tool with tests, coverage, a fat JAR and CI.
