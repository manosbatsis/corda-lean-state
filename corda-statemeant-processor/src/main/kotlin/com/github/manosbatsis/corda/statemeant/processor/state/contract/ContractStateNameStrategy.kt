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

import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.SimpleDtoNameStrategy
import com.squareup.kotlinpoet.ClassName

open class ContractStateNameStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : SimpleDtoNameStrategy(rootDtoStrategy) {

    companion object {
        const val STRATEGY_KEY = "ContractState"
    }

    override fun getClassName(): ClassName {
        val mappedPackageName = mapPackageName(annotatedElementInfo.generatedPackageName)
        var baseName = annotatedElementInfo.primaryTargetTypeElementSimpleName
                .removeSuffix("StateSpec")
                .removeSuffix("State")
                .removeSuffix("Spec")
        return ClassName(mappedPackageName, "${primaryTargetTypeElement.simpleName}${getClassNameSuffix()}")
    }

    override fun getClassNameSuffix(): String = STRATEGY_KEY


}
