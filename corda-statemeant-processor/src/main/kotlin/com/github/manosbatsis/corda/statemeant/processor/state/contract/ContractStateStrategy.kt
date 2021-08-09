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
package com.github.manosbatsis.corda.statemeant.processor.state.contract

import com.github.manosbatsis.corda.statemeant.processor.state.BaseStateStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo

/** Default overrides for building a ContractState from a spec interface */
class ContractStateStrategy(
        annotatedElementInfo: AnnotatedElementInfo
) : BaseStateStrategy<ContractStateNameStrategy, ContractStateTypeStrategy, ContractStateMembersStrategy>(
        annotatedElementInfo = annotatedElementInfo,
        dtoNameStrategyConstructor = ::ContractStateNameStrategy,
        dtoTypeStrategyConstructor = ::ContractStateTypeStrategy,
        dtoMembersStrategyConstructor = ::ContractStateMembersStrategy
) {

    companion object {
        const val STRATEGY_KEY = ContractStateNameStrategy.STRATEGY_KEY
    }

    override fun with(annotatedElementInfo: AnnotatedElementInfo): ContractStateStrategy {
        return ContractStateStrategy(annotatedElementInfo)
    }
}