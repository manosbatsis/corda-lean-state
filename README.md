# Corda LeanState [![Maven Central](https://img.shields.io/maven-central/v/com.github.manosbatsis.corda.leanstate/leanstate-contracts.svg)](https://repo1.maven.org/maven2/com/github/manosbatsis/corda/leanstate/)

Are you tired of manually maintaining consistent Corda Contract and Persistent State mappings?
Corda LeanState applies annotation processing to your cordapp's build 
and (re)generates state types based on a simplified interface like the `NewsPaper` 
you can see bellow:

```kotlin
@LeanStateModel
interface NewsPaper {
   val publisher: Party?
   val author: Party
   val price: BigDecimal
   val editions: Int
   val title: String
   val published: Date
   val alternativeTitle: String?
}
```

Naturally you can (optionally!) have your interface extend `LinearState` and/or `QueryableState` 
if you require custom implementations of `linearId`, `participants`, 
`generateMappedObject()`, `supportedSchemas()` etc. and the annotation will 
refrain from generating it's own if you do. This option however is rarely 
needed as the default generated overrides are configurable 
in a few ways. 

Check out the [installation](https://manosbatsis.github.io/corda-lean-state/installation), 
[state model](https://manosbatsis.github.io/corda-lean-state/state-model) 
and [full example](https://manosbatsis.github.io/corda-lean-state/full-example) sections 
for more details.
