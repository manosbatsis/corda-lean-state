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
package com.github.manosbatsis.corda.statemeant.processor.state.persistent

import com.github.manosbatsis.corda.statemeant.processor.state.contract.ContractStateNameStrategy
import com.github.manosbatsis.kotlin.utils.kapt.processor.AnnotatedElementInfo
import com.github.manosbatsis.vaultaire.processor.dto.BaseVaultaireDtoStrategy
import com.squareup.kotlinpoet.FileSpec

/** Default overrides for building a ContractState from a spec interface */
open class PersistentStateStrategy(
        annotatedElementInfo: AnnotatedElementInfo
) : BaseVaultaireDtoStrategy<PersistentStateNameStrategy, PersistentStateTypeStrategy, PersistentStateMembersStrategy>(
        annotatedElementInfo = annotatedElementInfo,
        dtoNameStrategyConstructor = ::PersistentStateNameStrategy,
        dtoTypeStrategyConstructor = ::PersistentStateTypeStrategy,
        dtoMembersStrategyConstructor = ::PersistentStateMembersStrategy
) {

    companion object {
        const val STRATEGY_KEY = PersistentStateNameStrategy.STRATEGY_KEY
    }

    override fun with(annotatedElementInfo: AnnotatedElementInfo): PersistentStateStrategy {
        return PersistentStateStrategy(annotatedElementInfo)
    }

    override fun onBeforeFileWrite(fileSpecBuilder: FileSpec.Builder) {
        val contractState = ContractStateNameStrategy(this).getClassName()
        fileSpecBuilder.addImport(contractState.packageName, contractState.simpleName)
        super.onBeforeFileWrite(fileSpecBuilder)
    }
}