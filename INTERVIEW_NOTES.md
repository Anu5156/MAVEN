# Maven Interview Notes 🎤

My revision sheet from the Learning Maven course (Days 1–10). Read this before any Java/backend interview.

---

## ⚡ One-line definitions

| Term | One line |
|---|---|
| **Maven** | Build automation + dependency management tool for Java, based on convention over configuration and driven by a POM |
| **POM** | `pom.xml`: the Project Object Model that describes coordinates, dependencies and build configuration |
| **GAV** | `groupId:artifactId:version`, the unique address of every artifact |
| **SNAPSHOT** | In-development version that can change; release versions are immutable |
| **Lifecycle** | Ordered phases; running a phase runs every phase before it |
| **Phase vs goal** | Phases are steps; plugin goals do the work and are bound to phases |
| **Scope** | Decides which classpath a dependency is on (compile, provided, runtime, test, import) |
| **Transitive dependency** | A dependency of my dependency, downloaded automatically |
| **Mediation** | Conflict rule: nearest wins; on a tie, first declared wins (NOT newest) |
| **BOM** | A pom of aligned versions, imported into `dependencyManagement` with `type pom` + `scope import` |
| **Fat / uber JAR** | My code + all dependencies in one runnable jar (`maven-shade-plugin`) |
| **Reactor** | Builds modules of a multi-module project in dependency order |
| **Maven Wrapper** | `mvnw` pins the Maven version per project and downloads it automatically |
| **Profile** | A named set of overrides, activated with `-P` or automatically (property, env var, JDK, OS) |
| **Resource filtering** | Replaces `${...}` in resource files while copying them into `target/classes` |

---

## 🔁 The default lifecycle

```
validate → compile → test → package → verify → install → deploy
```

| Phase | Default plugin goal |
|---|---|
| compile | `compiler:compile` |
| test | `surefire:test` |
| package | `jar:jar` |
| integration-test / verify | `failsafe:integration-test` / `failsafe:verify` (when configured) |
| install | `install:install` → copies the jar to `~/.m2/repository` |
| deploy | `deploy:deploy` → uploads to a remote repository |

Other lifecycles: **clean** (deletes `target/`) and **site** (generates docs).

---

## 📦 Dependency scopes

| Scope | Main code compiles | Tests | Shipped / runtime | Example |
|---|:-:|:-:|:-:|---|
| `compile` (default) | ✅ | ✅ | ✅ | Jackson, Guava |
| `provided` | ✅ | ✅ | ❌ | Servlet API, Lombok |
| `runtime` | ❌ | ✅ | ✅ | JDBC driver, Logback |
| `test` | ❌ | ✅ | ❌ | JUnit, Mockito |
| `import` | BOM versions only | | | junit-bom, jackson-bom |

Only `compile` and `runtime` dependencies are passed on transitively.

---

## 🧩 "Management" vs "actually used"

| Versions only | Actually used |
|---|---|
| `<dependencyManagement>` | `<dependencies>` |
| `<pluginManagement>` | `<build><plugins>` |

---

## 🧪 Testing

| | Unit tests | Integration tests |
|---|---|---|
| File names | `*Test`, `Test*`, `*Tests`, `*TestCase` | `*IT`, `IT*`, `*ITCase` |
| Plugin | Surefire | Failsafe |
| Phase | `test` | `integration-test` + `verify` |

- Failsafe records failures in `integration-test` and fails only in `verify`, so `post-integration-test` cleanup always runs
- JaCoCo: `prepare-agent` → `report` → `check` (quality gate, e.g. ≥ 90% line coverage)
- 100% coverage ≠ bug-free: it shows which lines ran, not that they were checked
- `-DskipTests` compiles tests but doesn't run them; `-Dmaven.test.skip=true` doesn't compile them either

---

## 🛠️ Commands I actually use

| Command | Use |
|---|---|
| `mvn clean install` | Everyday full build |
| `mvn dependency:tree "-Dverbose"` | Find version conflicts |
| `mvn dependency:analyze` | Used-but-undeclared / declared-but-unused dependencies |
| `mvn help:effective-pom` | See the fully resolved POM |
| `mvn help:active-profiles` | Which profiles are on |
| `mvn test "-Dtest=MyTest#method"` | Run one test |
| `mvn package -pl module -am` | Build one module + what it needs |
| `mvn -o` / `mvn -U` | Offline / force-update SNAPSHOTs |
| `./mvnw clean verify` | Build with the project's pinned Maven version |

---

## 🐛 Errors I have debugged myself

| Error | Cause | Fix |
|---|---|---|
| `NoClassDefFoundError` with `java -cp app.jar` | Normal jar has no dependencies inside | Run via `mvn exec:java` or build a fat jar |
| `no main manifest attribute` | No `Main-Class` in `MANIFEST.MF` | Set `<mainClass>` (jar plugin / shade transformer) |
| `package X does not exist` in main code | Dependency has `test` scope | Use `compile` scope |
| `Duplicated tag: 'plugins'` | Pasted `<plugins>` inside `<pluginManagement>` | Put `<plugins>` after `</pluginManagement>` |
| `package main.java.com... does not exist` | IDE auto-import with wrong package | Delete the import; package is `com.anushka` |
| `Unknown lifecycle phase ".mainClass=..."` | PowerShell splits `-D` args at the dot | Quote it: `"-Dexec.mainClass=..."` |
| `target/` still on GitHub after adding `.gitignore` | `.gitignore` doesn't untrack committed files | `git rm -r --cached target` |
| `Permission denied: ./mvnw` on Linux CI | Windows doesn't set the executable bit | `git update-index --chmod=+x mvnw` |

---

## 🎤 Scenario questions (write your own answers!)

### Q1. Debugging
*"It builds on a teammate's laptop, but on CI the app crashes with `java.lang.NoSuchMethodError: com.fasterxml.jackson.core.JsonParser...`. How do you investigate and fix it?"*

**My answer:**

> _(write here)_

### Q2. Design
*"You're starting Vecta, a distributed vector database in Java, with a core indexing engine, a storage layer, a network server and a Java client library other teams will use. How would you set up the Maven build?"*

**My answer:**

> _(write here)_

### Q3. Explain simply
*"Explain to a fresher what happens when you type `mvn clean install` and press Enter."*

**My answer:**

> _(write here)_
