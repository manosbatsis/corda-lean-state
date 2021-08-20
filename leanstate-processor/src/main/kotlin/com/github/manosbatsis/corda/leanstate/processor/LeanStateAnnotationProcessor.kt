/*
 * Corda LeanState: Generate Corda Contract and Persistent states
 * from a simplified model interface.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */


package com.github.manosbatsis.corda.leanstate.processor

import com.github.manosbatsis.corda.leanstate.processor.state.contract.ContractStateStrategy
import com.github.manosbatsis.corda.leanstate.processor.state.persistent.PersistentStateStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.ConstructorRefsCompositeDtoStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AbstractAnnotatedModelInfoProcessor
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotationProcessorBase
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotationProcessorBase.Companion.TYPE_PARAMETER_STAR
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.AnonymousParty
import java.security.PublicKey
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

/**
 * Kapt processor for generating (Corda) state-based DTOs.
 */
@SupportedAnnotationTypes(
        "com.github.manosbatsis.corda.leanstate.annotation.LeanStateModel")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnnotationProcessorBase.KAPT_OPTION_NAME_KAPT_KOTLIN_GENERATED)
class LeanStateModelAnnotationProcessor : AbstractAnnotatedModelInfoProcessor(
        primaryTargetRefAnnotationName = "baseType",
        secondaryTargetRefAnnotationName = ""
) {


    override fun getFieldNameExclusions(): Set<String> = setOf("participants")

    override fun processElementInfos(elementInfos: List<AnnotatedElementInfo>) {
        if(participantsStateClassName == null){
            val leanstatePackage = elementInfos.map { it.generatedPackageName }
                    .reduce { acc, string -> acc.commonPrefixWith(string) }
                    .let {
                        val base = if (it.endsWith(".")) it else it.substringBeforeLast(".")
                        "$base.leanstate"
                    }
            val participantsStateSpec: TypeSpec = getParticipantsStateSpec(leanstatePackage)
            getFileSpecBuilder(leanstatePackage, participantsStateSpec.name.toString())
                    .addComment("\n")
                    .addImport("kotlin.reflect.full", "memberProperties")
                    .addType(participantsStateSpec)
                    .build()
                    .writeTo(sourceRootFile)
        }

        elementInfos.forEach {
            processElementInfo(it)
        }
    }

    fun processElementInfo(elementInfo: AnnotatedElementInfo) {
        val allStrategies = getDtoStrategies(elementInfo)

        allStrategies.map { (_, strategy) ->
            val dtoStrategyBuilder = strategy.dtoTypeSpecBuilder()
                    .also { if(strategy is ContractStateStrategy) it.addSuperinterface(participantsStateClassName!!) }
            val dto = dtoStrategyBuilder.build()
            val dtoClassName = strategy.getClassName()
            val fileName = dtoClassName.simpleName
            val packageName = dtoClassName.packageName
            val annElem = strategy.annotatedElementInfo
            val fileBuilder = getFileSpecBuilder(packageName, fileName)
            strategy.onBeforeFileWrite(fileBuilder)
            fileBuilder.addComment("\n")
                    .addComment("----------------------------------------------------\n")
                    .addComment("Annotation Processing Info\n")
                    .addComment("----------------------------------------------------\n")
                    .addComment("Annotation: ${annElem.annotation.annotationType}\n")
                    .addComment("Source Elements\n")
                    .addComment("   Primary:   ${annElem.primaryTargetTypeElement.asClassName().canonicalName}\n")
                    .addComment("   Secondary: ${annElem.secondaryTargetTypeElement?.asClassName()?.canonicalName ?: "none"}\n")
                    .addComment("   Mixin:     ${annElem.mixinTypeElement?.asClassName()?.canonicalName ?: "none"}\n")
                    .addComment("Generator Strategies\n")
                    .addComment("   Main:    ${strategy.javaClass.simpleName}\n")
                    .addComment("   Name:    ${strategy.dtoNameStrategy.javaClass.canonicalName}\n")
                    .addComment("   Type:    ${strategy.dtoTypeStrategy.javaClass.canonicalName}\n")
                    .addComment("   Members: ${strategy.dtoMembersStrategy.javaClass.canonicalName}\n")
                    .addComment("----------------------------------------------------\n")
                    .addType(dto)
                    .build()
                    .writeTo(sourceRootFile)
        }
    }

    /** Get a list of DTO strategies to apply per annotated element */
    private fun getDtoStrategies(annotatedElementInfo: AnnotatedElementInfo): Map<String, ConstructorRefsCompositeDtoStrategy<*, *, *>> {
        return listOfNotNull(
                ContractStateStrategy.STRATEGY_KEY to ContractStateStrategy(annotatedElementInfo),
                PersistentStateStrategy.STRATEGY_KEY to PersistentStateStrategy(annotatedElementInfo)
        ).toMap()
    }

    companion object {
        var participantsStateClassName: ClassName? = null
        private fun getParticipantsStateSpec(leanstatePackage: String): TypeSpec {
            participantsStateClassName = ClassName(leanstatePackage, "ParticipantsState")
            return TypeSpec.interfaceBuilder(participantsStateClassName!!.simpleName)
                    .addKdoc("Helpers for (generated) implementations of [ContractState.participants].")
                    .addType(TypeSpec.companionObjectBuilder()

                            .addFunction(FunSpec.builder("isKotlinClass")
                                    .receiver(Class::class.asClassName().parameterizedBy(TYPE_PARAMETER_STAR))
                                    .returns(Boolean::class)
                                    .addCode("return declaredAnnotations.any { it.annotationClass.qualifiedName == \"kotlin.Metadata\" }")
                                    .build())
                            .addFunction(FunSpec.builder("isKotlinType")
                                    .receiver(Any::class)
                                    .returns(Boolean::class)
                                    .addCode("return this::class.java.isKotlinClass()")
                                    .build())
                            .addFunction(FunSpec.builder("getField")
                                    .addParameter("fieldName", String::class)
                                    .receiver(Any::class)
                                    .returns(Any::class.asClassName().copy(nullable = true))
                                    .addCode("return  this::class.memberProperties.find { fieldName == it.name } ?.getter?.call(this)")
                                    .build())
                            .build())

                    .addFunction(FunSpec.builder("toAbstractParty")
                            .addParameter("publicKey", PublicKey::class)
                            .returns(AbstractParty::class)
                            .addCode("return %T(publicKey)", AnonymousParty::class)
                            .build())
                    .addFunction(FunSpec.builder("toAbstractParties")
                            .addParameter("entries", Collection::class.asClassName()
                                    .parameterizedBy(TYPE_PARAMETER_STAR).copy(nullable = true))
                            .returns(List::class.asClassName().parameterizedBy(AbstractParty::class.asClassName()))
                            .addCode("return entries?.mapNotNull { toAbstractParty(it) }  ?: emptyList()")
                            .build())
                    .addFunction(FunSpec.builder("toAbstractParty")
                            .addParameter("entry", Any::class.asClassName().copy(nullable = true))
                            .returns(AbstractParty::class.asClassName().copy(nullable = true))
                            .addCode("""
                                |return when {
                                |    entry == null -> null
                                |    entry is AbstractParty -> entry
                                |    entry is PublicKey -> toAbstractParty(entry)
                                |    entry.isKotlinType() -> toAbstractParty(entry.getField("party"))
                                |    else -> entry.javaClass.declaredMethods
                                |            .find { "getParty" == it.name && it.isAccessible }
                                |            ?.let { toAbstractParty(it.invoke(entry)) }
                                |}
                                |""".trimMargin())
                            .build())
                    .addFunction(FunSpec.builder("toParticipants")
                            .addParameter("entry", Any::class.asClassName().copy(nullable = true), KModifier.VARARG)
                            .returns(List::class.asClassName().parameterizedBy(AbstractParty::class.asClassName()))
                            .addCode("""
                            |return entry.filterNotNull()
                            |   .partition { it is Collection<*> }
                            |   .run {
                            |       first.map { toAbstractParties(it as Collection<*>) }
                            |           .flatten() + second.mapNotNull { toAbstractParty(it) }
                            |   }
                            |""".trimMargin())
                            .build())
                    .build()
        }
    }
}


