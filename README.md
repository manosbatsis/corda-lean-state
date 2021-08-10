# Corda LeanState ![Maven Central](https://img.shields.io/maven-central/v/com.github.manosbatsis.corda.leanstate/leanstate-contracts.svg)

Are you tired of maintaining consistent Corda Contract to Persistent State mappings?
Corda LeanState will use annotation processing during your cordapp's build 
to (re)generate Corda Contract and Persistent States based on a simplified 
interface like `NewsPaper` bellow:

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

Explicit/custom implementations of `linearId`, `participants`, 
`generateMappedObject()`, `supportedSchemas()` etc. in the above 
interface are optional but rarely needed. Check out the [installation](https://manosbatsis.github.io/corda-leanstate/installation), 
[state model](https://manosbatsis.github.io/corda-leanstate/state-model) and [full example](https://manosbatsis.github.io/corda-leanstate/full-example) sections 
for more details.
