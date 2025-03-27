# Support for external Android module to Android project dependency substitution

## Repro

1. ` ./gradlew app:build --dry-run`

## Expected

Success

## Actual

Failure

```
* What went wrong:
Could not determine the dependencies of task ':app:compileDebugJavaWithJavac'.
> Could not resolve all dependencies for configuration ':app:debugCompileClasspath'.
   > Could not select a variant of project :libA that matches the consumer attributes.
      > Could not find libA.aar (project :libA).
```

## Context

AndroidX has a build where we substitute all binary dependencies with project dependencies to make
sure that build and tests pass with the latest version of the library. 

We have external (non-androidx) libraries that depend on androidx and we use these external
dependencies. Some of these external libraries only publish POM files (no Gradle metadata) and
specify `<type>aar</type>` in the POM. Usually, these libraries do not use Gradle to create the POM.

In this example project, we have `com.external:libB:1.0` (external library) that depends on
`com.example:libA:1.0` (our library). We then pull this dependency in `:app` project and try to do
a substitution to `project(":libA")`.

In theory, this should just work, but sadly, it seems that classifier type `aar` is kept and when
project is used, Gradle fails to select the variant.
