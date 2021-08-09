/*
 * Corda Statemeant: Generate Corda Contract and Persistent states
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
/*
 * Corda Statemeant: Generate Corda Contract and Persistent states
 * from a single interface
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
package com.github.manosbatsis.corda.statemeant.processor

import com.github.manosbatsis.corda.statemeant.processor.state.contract.ContractStateStrategy
import com.github.manosbatsis.corda.statemeant.processor.state.persistent.PersistentStateStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.ConstructorRefsCompositeDtoStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AbstractAnnotatedModelInfoProcessor
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotationProcessorBase
import com.squareup.kotlinpoet.asClassName
import net.corda.core.schemas.QueryableState
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

/**
 * Kapt processor for generating (Corda) state-based DTOs.
 */
@SupportedAnnotationTypes(
        "com.github.manosbatsis.corda.statemeant.annotation.StatemeantType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(AnnotationProcessorBase.KAPT_OPTION_NAME_KAPT_KOTLIN_GENERATED)
class StatemeantTypeAnnotationProcessor : AbstractAnnotatedModelInfoProcessor(
        primaryTargetRefAnnotationName = "baseType",
        secondaryTargetRefAnnotationName = ""
) {

    override fun getFieldNameExclusions(): Set<String> = setOf("participants")

    override fun processElementInfos(elementInfos: List<AnnotatedElementInfo>) {
        elementInfos.forEach {
            processElementInfo(it)
        }
    }

    fun processElementInfo(elementInfo: AnnotatedElementInfo) {
        val allStrategies = getDtoStrategies(elementInfo)

        allStrategies.map { (_, strategy) ->
            val dtoStrategyBuilder = strategy.dtoTypeSpecBuilder()
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
                    .addComment("   Secondary: ${annElem.secondaryTargetTypeElement?.asClassName()?.canonicalName?:"none"}\n")
                    .addComment("   Mixin:     ${annElem.mixinTypeElement?.asClassName()?.canonicalName?:"none"}\n")
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

}


