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


package com.github.manosbatsis.corda.statemeant.processor.state.contract


import com.github.manosbatsis.corda.statemeant.annotation.ParticipantsState
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.SimpleDtoTypeStrategy
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Builder
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.Contract
import net.corda.core.contracts.LinearState
import net.corda.core.schemas.QueryableState
import javax.lang.model.element.TypeElement

open class ContractStateTypeStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : SimpleDtoTypeStrategy(rootDtoStrategy) {

    override fun addKdoc(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.addKdoc("Generated [ContractState] based on [%T].",
                rootDtoStrategy!!.annotatedElementInfo.primaryTargetTypeElement)
    }

    private fun Builder.addMissingSuperInterfaces(
            targetInterfaces: List<Class<*>>
    ): Builder {
        val annotatedTypeElement = annotatedElementInfo.primaryTargetTypeElement
        targetInterfaces.forEach{
            if(!annotatedTypeElement.isAssignableTo(it))
                addSuperinterface(it)
        }
        return this
    }

    override fun addSuperTypes(typeSpecBuilder: TypeSpec.Builder) {
        typeSpecBuilder.addSuperinterface(annotatedElementInfo.primaryTargetTypeElement.asKotlinClassName())
                .addMissingSuperInterfaces(listOf(
                        ParticipantsState::class.java,
                        LinearState::class.java,
                        QueryableState::class.java
                ))
    }

    override fun addAnnotations(typeSpecBuilder: Builder) {
        val contractType = with(annotatedElementInfo.annotation){
                    findValueAsTypeElement("contractClass")
                    ?.let { if(it.qualifiedName.toString() == Contract::class.qualifiedName) null else it }
                    ?:findAnnotationValueString("contractClassName")
                            ?.let { if(it.isNotBlank()) processingEnvironment.elementUtils.getTypeElement(it) else null }
                    ?: annotatedElementInfo.primaryTargetTypeElement.enclosingElement?.let {
                        if(it is TypeElement &&  it.isAssignableTo(Contract::class.java)) it else null
                    }
                    ?: error("Could not find a target contract using contractClass or contractClassName of $this")
        }
        typeSpecBuilder.addAnnotation(AnnotationSpec.builder(BelongsToContract::class)
                .addMember("value = %T::class", contractType)
                .build())
        super.addAnnotations(typeSpecBuilder)
    }
}
