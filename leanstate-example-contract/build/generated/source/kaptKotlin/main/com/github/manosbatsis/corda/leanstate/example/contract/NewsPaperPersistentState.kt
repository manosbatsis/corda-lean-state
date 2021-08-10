// ------------------------ DO NOT EDIT -----------------------
//  This file is automatically generated by Corda LeanState,
//  see https://manosbatsis.github.io/corda-lean-state
// -------------------------------------------------------------
// ----------------------------------------------------
// Annotation Processing Info
// ----------------------------------------------------
// Annotation: com.github.manosbatsis.corda.leanstate.annotation.LeanStateModel
// Source Elements
//    Primary:   com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContract.NewsPaper
//    Secondary: none
//    Mixin:     none
// Generator Strategies
//    Main:    PersistentStateStrategy
//    Name:    com.github.manosbatsis.corda.leanstate.processor.state.persistent.PersistentStateNameStrategy
//    Type:    com.github.manosbatsis.corda.leanstate.processor.state.persistent.PersistentStateTypeStrategy
//    Members: com.github.manosbatsis.corda.leanstate.processor.state.persistent.PersistentStateMembersStrategy
// ----------------------------------------------------
//
package com.github.manosbatsis.corda.leanstate.example.contract

import com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContractState
import java.math.BigDecimal
import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import kotlin.Int
import kotlin.String
import net.corda.core.identity.CordaX500Name
import net.corda.core.schemas.PersistentState

/**
 * Custom database schema mapping for [NewsPaperContractState], generated based on
 * [NewsPaperContract.NewsPaper].
 */
@Entity
@Table(name = "news_paper")
class NewsPaperPersistentState(
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
    name = "alt_title",
    length = 500
  )
  val alternativeTitle: String? = null
) : PersistentState()
