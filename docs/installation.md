
# Installation


Step 1: Add to your Cordapp's Gradle dependencies:

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

Using the above may require you to manually add the LeanState contracts JAR 
to Corda transactions as an attachment as needed. You can easily do that using  
the `TransactionBuilder.attachLeanStateJar` extension function.

Alternatively, you might want to add LeanState in the Cordapp's fat JAR, 
in which case use `compile` instead of `cordacompile` and skip step 2 bellow.

Step 2: Add LeanState as a Cordapp to your `deployNodes`/`dockerNodes` task:

```groovy

// Core dependency
cordapp "com.github.manosbatsis.corda.leanstate:leanstate-contracts:$leanstate_version"
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
