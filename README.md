# Corda LeanState [![Maven Central](https://img.shields.io/maven-central/v/com.github.manosbatsis.corda.leanstate/leanstate-contracts.svg)](https://repo1.maven.org/maven2/com/github/manosbatsis/corda/leanstate/)

Tired of manual maintenance of Corda `ContractState` - `PersistentState` mappings?
LeanState's annotation processing will (re)generate perfectly synced and consistent  
state classes, using a simplified interface like `NewsPaper` bellow as input:

```kotlin
@LeanStateModel
interface NewsPaper {
   val publisher: Party?
   //...
}
```

You can (optionally!) extend Corda's standard interfaces and, 
if you really need to, add custom overrides (e.g.  for `supportedSchemas()`). 
The processor will behave accordingly and refrain from generating its own  if you do.  
The need to should be rare as generation of state sources is configurable in a few ways,
checkout the [state model](https://manosbatsis.github.io/corda-lean-state/state-model) 
and a [full example](https://manosbatsis.github.io/corda-lean-state/full-example) 
for details.

Here's what `NewsPaper` above gets us with default settings (edited for brevity). 
Contract state:

```kotlin
@BelongsToContract(value = NewsPaperContract::class)
data class NewsPaperContractState(
        override val linearId: UniqueIdentifier = UniqueIdentifier(),
        override val publisher: Party? = null,
        //...
) : NewsPaperContract.NewsPaper, ParticipantsState, LinearState, QueryableState {
    override val participants: List<AbstractParty> = //...
    override fun generateMappedObject(schema: MappedSchema): NewsPaperPersistentState = //...
    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(SchemaV1)
    object Schema
    object SchemaV1 : MappedSchema(Schema::class.java, 1,
            listOf(NewsPaperPersistentState::class.java))
}
```

Persistent state:

```kotlin
@Entity @Table(name = "news_paper")
class NewsPaperPersistentState(
  @Column(name = "linear_id_id",nullable = false) val linearIdId: UUID,
  @Column(name = "linear_id_external_id") val linearIdExternalId: String? = null,
  @Column(name = "publisher_name_common_name") val publisherNameCommonName: String? = null,
  @Column(name = "publisher_name_organisation_unit") val publisherNameOrganisationUnit: String? = null,
  @Column(name = "publisher_name_organisation") val publisherNameOrganisation: String?,
  @Column(name = "publisher_name_locality") val publisherNameLocality: String?,
  @Column(name = "publisher_name_state") val publisherNameState: String? = null,
  @Column(name = "publisher_name_country") val publisherNameCountry: String?,
) : PersistentState()

```


