
# Installation


Step 1: Add to your Cordapp's Gradle dependencies:

```groovy
// apply the kapt plugin
apply plugin: 'kotlin-kapt'

dependencies{
    // Core dependency
    cordaCompile "com.github.manosbatsis.corda.restate:restate-contracts:$restate_version"
    // Annotation processing
    kapt "com.github.manosbatsis.corda.restate:restate-processor:$restate_version"

    // Corda dependencies etc.
    // ...

}    
```

Alternatively, you might want to add Restate in the Cordapp's fat JAR, 
in which case use `compile` instead of `cordacompile` and skip step 2 bellow.

Step 2: Add Restate as a Cordapp to your `deployNodes`/`dockerNodes` task:

```groovy

// Core dependency
cordapp "com.github.manosbatsis.corda.restate:restate-contracts:$restate_version"
```

Step 3: You may also want to add the generated sources to your cordapp's 
Gradle `sourceSets`:

```groovy

// Define an extra sources variable
def generatedSourcesDir = project.file("build/generated/source/kaptKotlin/main")
// Tell Gradle about the extra source set
sourceSets {
    main {
        kotlin.srcDirs += generatedSourcesDir
    }
}

// Optional: Tell IntelliJ about the extra source set
idea {
    module {
        sourceDirs += generatedSourcesDir
        generatedSourceDirs += generatedSourcesDir
    }
}
```
