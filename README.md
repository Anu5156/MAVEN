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

## 🎯 Coming next
Day 2: understanding `pom.xml` line by line