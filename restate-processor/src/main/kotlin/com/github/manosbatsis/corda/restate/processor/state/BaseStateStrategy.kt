/*
 * Corda Restate: Generate Corda Contract and Persistent states
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

package com.github.manosbatsis.corda.restate.processor.state

import com.github.manosbatsis.kotlin.utils.kapt.dto.DtoInputContext
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoMembersStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoTypeStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo
import com.github.manosbatsis.vaultaire.processor.dto.BaseVaultaireDtoStrategy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import net.corda.core.contracts.LinearState
import javax.lang.model.element.VariableElement
import kotlin.reflect.KFunction1

abstract class BaseStateStrategy<N: DtoNameStrategy, T: DtoTypeStrategy, M: DtoMembersStrategy>(
        annotatedElementInfo: AnnotatedElementInfo,
        dtoNameStrategyConstructor: KFunction1<DtoStrategyLesserComposition, N>,
        dtoTypeStrategyConstructor: KFunction1<DtoStrategyLesserComposition, T>,
        dtoMembersStrategyConstructor: KFunction1<DtoStrategyLesserComposition, M>
) : BaseVaultaireDtoStrategy<N, T, M>(
        annotatedElementInfo = annotatedElementInfo,
        dtoNameStrategyConstructor = dtoNameStrategyConstructor,
        dtoTypeStrategyConstructor = dtoTypeStrategyConstructor,
        dtoMembersStrategyConstructor = dtoMembersStrategyConstructor
){
    override fun dtoSpec(dtoInputContext: DtoInputContext): TypeSpec {
        checkValidAnnotatedelement(annotatedElementInfo)
        return super.dtoSpec(dtoInputContext)
    }


    override fun getFieldsToProcess(): List<VariableElement> {
        val fields = annotatedElementInfo.primaryTargetTypeElementFields.filtered()
        // add linearId if missing
        return if(annotatedElementInfo.primaryTargetTypeElement.isAssignableTo(LinearState::class.java)
                || fields.find { it.simpleName.toString() == "linearId" } != null) fields
        else processingEnvironment.elementUtils.getTypeElement(LinearState::class.java.canonicalName)
                   .accessibleConstructorParameterFields(adaptInterfaceGetters = true)
                   .filter { it.simpleName.toString() == "linearId" } + fields
    }

    fun checkValidAnnotatedelement(annotatedElementInfo: AnnotatedElementInfo) {
        val annotationName = annotatedElementInfo.annotation.annotationType.asTypeElement().qualifiedName
        val annotatedElementName = annotatedElementInfo.primaryTargetTypeElement.qualifiedName.toString()
        check(annotatedElementInfo.primaryTargetTypeElement.isInterface()) {
            "${annotationName} is only allowed on interfaces but $annotatedElementName is a class"
        }
    }
}