
# Installation


Step 1: Add Lean State to your Contract Cordapp's Gradle dependencies and apply 
kotlin's kapt and jpa plugins:

```groovy
// apply the kapt plugin
apply plugin: 'kotlin-kapt'
// apply the kapt plugin
apply plugin: 'kotlin-jpa'

dependencies{
    // Lean State dependencies
    cordaCompile "com.github.manosbatsis.corda.leanstate:leanstate-contracts:$leanstate_version"
    kapt "com.github.manosbatsis.corda.leanstate:leanstate-processor:$leanstate_version"

    // Corda dependencies etc.
    // ...

}    
```

Please note: you __don't__ need any Lean State dependencies at runtime. 
No reason whatsoever to use `compile`, add dependencies to `deployNodes` 
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
