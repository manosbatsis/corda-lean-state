# Full Example

## Model Interface



```kotlin
@LeanStateModel(
        contractClass = NewsPaperContract::class,
        // Optional, default is [PropertyMappingMode.EXPANDED]
        mappingModes = [
            PropertyMappingMode.NATIVE,
            PropertyMappingMode.STRINGIFY,
            PropertyMappingMode.EXPANDED
        ]
)
interface NewsPaper {
    val publisher: Party?
    val author: Party
    val price: BigDecimal
    val editions: Int
    val title: String
    @get:LeanStateProperty(initializer = "Date()")
    val published: Date
    @get:Column(name = "alt_title", length = 500)
    val alternativeTitle: String?
}
```

## Generated ContractState

```kotlin

/**
 * Generated [ContractState] based on [NewsPaperContract.NewsPaper].
 */
@BelongsToContract(value = NewsPaperContract::class)
data class NewsPaperContractState(
  override val linearId: UniqueIdentifier = UniqueIdentifier(),
  override val publisher: Party? = null,
  override val author: Party,
  override val price: BigDecimal,
  override val editions: Int,
  override val title: String,
  override val published: Date = Date(),
  override val alternativeTitle: String? = null
) : NewsPaperContract.NewsPaper, ParticipantsState, LinearState, QueryableState {
    
    override val participants: List<AbstractParty>
        get() = toParticipants(publisher,author)

    override fun generateMappedObject(schema: MappedSchema): NewsPaperPersistentState {
        return when(schema) {
            is SchemaV1 -> {
                NewsPaperPersistentState(
                        linearIdIdString = linearId.id.toString(),
                        linearIdId = linearId.id,
                        linearIdExternalId = linearId.externalId,
                        publisherName = publisher?.name,
                        publisherNameString = publisher?.name?.toString(),
                        publisherNameCommonName = publisher?.name?.commonName,
                        publisherNameOrganisationUnit = publisher?.name?.organisationUnit,
                        publisherNameOrganisation = publisher?.name?.organisation,
                        publisherNameLocality = publisher?.name?.locality,
                        publisherNameState = publisher?.name?.state,
                        publisherNameCountry = publisher?.name?.country,
                        authorName = author.name,
                        authorNameString = author.name.toString(),
                        authorNameCommonName = author.name.commonName,
                        authorNameOrganisationUnit = author.name.organisationUnit,
                        authorNameOrganisation = author.name.organisation,
                        authorNameLocality = author.name.locality,
                        authorNameState = author.name.state,
                        authorNameCountry = author.name.country,
                        price = price,
                        editions = editions,
                        title = title,
                        published = published,
                        alternativeTitle = alternativeTitle
                )
            }
            else ->  {
                throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }
    }
    
    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(SchemaV1)
    object Schema
    
    object SchemaV1 : MappedSchema(Schema::class.java, 1,
      listOf(NewsPaperPersistentState::class.java))
}

```

## Generated PersistentState

```kotlin
/**
 * Custom database schema mapping for [NewsPaperContractState], generated based on
 * [NewsPaperContract.NewsPaper].
 */
@Entity
@Table(name = "news_paper")
class NewsPaperPersistentState(
  /**
   * Enables query criteria for [NewsPaperContractState.linearId].
   */
  @Column(
    name = "linear_id",
    nullable = false
  )
  val linearId: UniqueIdentifier,
  /**
   * Enables query criteria for [NewsPaperContractState.linearId.id] as a [String].
   */
  @Column(
    name = "linear_id_id_string",
    nullable = false
  )
  val linearIdIdString: String,
  /**
   * Enables query criteria for [NewsPaperContractState.linearId.id].
   */
  @Column(
    name = "linear_id_id",
    nullable = false
  )
  val linearIdId: UUID,
  /**
   * Enables query criteria for [NewsPaperContractState.linearId.externalId].
   */
  @Column(name = "linear_id_external_id")
  val linearIdExternalId: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name].
   */
  @Column(name = "publisher_name")
  val publisherName: CordaX500Name?,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name] as a [String].
   */
  @Column(name = "publisher_name_string")
  val publisherNameString: String?,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.commonName].
   */
  @Column(name = "publisher_name_common_name")
  val publisherNameCommonName: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.organisationUnit].
   */
  @Column(name = "publisher_name_organisation_unit")
  val publisherNameOrganisationUnit: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.organisation].
   */
  @Column(name = "publisher_name_organisation")
  val publisherNameOrganisation: String?,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.locality].
   */
  @Column(name = "publisher_name_locality")
  val publisherNameLocality: String?,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.state].
   */
  @Column(name = "publisher_name_state")
  val publisherNameState: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.publisher.name.country].
   */
  @Column(name = "publisher_name_country")
  val publisherNameCountry: String?,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name].
   */
  @Column(
    name = "author_name",
    nullable = false
  )
  val authorName: CordaX500Name,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name] as a [String].
   */
  @Column(
    name = "author_name_string",
    nullable = false
  )
  val authorNameString: String,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.commonName].
   */
  @Column(name = "author_name_common_name")
  val authorNameCommonName: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.organisationUnit].
   */
  @Column(name = "author_name_organisation_unit")
  val authorNameOrganisationUnit: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.organisation].
   */
  @Column(
    name = "author_name_organisation",
    nullable = false
  )
  val authorNameOrganisation: String,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.locality].
   */
  @Column(
    name = "author_name_locality",
    nullable = false
  )
  val authorNameLocality: String,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.state].
   */
  @Column(name = "author_name_state")
  val authorNameState: String? = null,
  /**
   * Enables query criteria for [NewsPaperContractState.author.name.country].
   */
  @Column(
    name = "author_name_country",
    nullable = false
  )
  val authorNameCountry: String,
  /**
   * Enables query criteria for [NewsPaperContractState.price].
   */
  @Column(
    name = "price",
    nullable = false
  )
  val price: BigDecimal,
  /**
   * Enables query criteria for [NewsPaperContractState.editions].
   */
  @Column(
    name = "editions",
    nullable = false
  )
  val editions: Int,
  /**
   * Enables query criteria for [NewsPaperContractState.title].
   */
  @Column(
    name = "title",
    nullable = false
  )
  val title: String,
  /**
   * Enables query criteria for [NewsPaperContractState.published].
   */
  @Column(
    name = "published",
    nullable = false
  )
  val published: Date,
  /**
   * Enables query criteria for [NewsPaperContractState.alternativeTitle].
   */
  @Column(
          name = "description",
          length = 500
  )
  val alternativeTitle: String? = null
) : PersistentState()

```