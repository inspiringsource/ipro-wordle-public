

# Helper.java – Run Instructions

This class belongs to the package:

```java
package data;
```

Because it is inside a package, the class must be compiled and executed using its **fully qualified name**.

## Compile (from the project root directory)

```bash
javac -d . data/Helper.java
```

## Run

```bash
java data.Helper
```

The `-d .` option ensures that the compiled class is placed into the correct package folder structure (`data/Helper.class`).

Do **not** run using:

```bash
java Helper
java Helper.class
```

because the JVM must reference the packaged class name `data.Helper`.