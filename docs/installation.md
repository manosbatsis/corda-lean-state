
# Installation


Step 1: Add Lean State to your Contract Cordapp's Gradle build-time dependencies:

```groovy
// apply the kapt plugin
apply plugin: 'kotlin-kapt'

dependencies{
    // Core dependency
    cordaCompile "com.github.manosbatsis.corda.leanstate:leanstate-contracts:$leanstate_version"
    // Annotation processing
    kapt "com.github.manosbatsis.corda.leanstate:leanstate-processor:$leanstate_version"

    // Corda dependencies etc.
    // ...

}    
```

> You __don't need any Lean State dependencies at runtime__. 

Again, no reason whatsoever to use `compile`, add dependencies to `deployNodes` 
or copy Lean State JARs in your Node's cordapps folder. 

Step 2: You may also want to add the generated sources to your cordapp's 
Gradle `sourceSets` e.g.:

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
