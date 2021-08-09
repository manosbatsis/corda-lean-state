# Corda Statemeant

> __Note__: This repo contains an early release, i.e. experimental code. 

Are you tired of maintaining consistent Corda Contract to Persistent State mappings?
Corda Statemeant will use annotation processing during your cordapp's build 
to (re)generate Corda Contract and Persistent States based on a simplified 
interface like `NewsPaper` bellow:

```kotlin
@StatemeantType
interface NewsPaper: LinearState, QueryableState {
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
[state mapping](./state-mapping.md)(TBA) and [complete example](./complete-example.md) sections 
for more details.
