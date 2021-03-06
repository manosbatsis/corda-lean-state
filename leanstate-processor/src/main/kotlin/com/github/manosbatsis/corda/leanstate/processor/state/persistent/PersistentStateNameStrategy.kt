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


package com.github.manosbatsis.corda.leanstate.processor.state.persistent

import com.github.manosbatsis.corda.leanstate.annotation.LeanStateModel
import com.github.manosbatsis.corda.leanstate.processor.state.BaseStateNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.dto.strategy.composition.DtoStrategyLesserComposition
import com.squareup.kotlinpoet.ClassName



open class PersistentStateNameStrategy(
        rootDtoStrategy: DtoStrategyLesserComposition
) : BaseStateNameStrategy(rootDtoStrategy) {

    companion object {
        const val STRATEGY_KEY = "PersistentState"
    }

    override fun getClassName(): ClassName = annotatedElementInfo.primaryTargetTypeElement
            .getAnnotation(LeanStateModel::class.java)
            .persistentStateName
            .let {
                if (it.isNotBlank())
                    ClassName(mapPackageName(annotatedElementInfo.generatedPackageName), it)
                else getClassNameFallback()
            }



    override fun getClassNameSuffix(): String = STRATEGY_KEY


}
