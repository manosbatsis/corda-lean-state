# Corda Restate ![Maven Central](https://img.shields.io/maven-central/v/com.github.manosbatsis.corda.restate/restate-contracts.svg)

Are you tired of maintaining consistent Corda Contract to Persistent State mappings?
Corda Restate will use annotation processing during your cordapp's build 
to (re)generate Corda Contract and Persistent States based on a simplified 
interface like `NewsPaper` bellow:

```kotlin
@RestateModel
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
interface are optional but rarely needed. Check out the [installation](./installation.md), 
[state model](https://manosbatsis.github.io/corda-restate/state-model)and [full example](https://manosbatsis.github.io/corda-restate/full-example) sections 
for more details.
