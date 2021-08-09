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
 * from a simplified model interface
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
package com.github.manosbatsis.corda.statemeant.annotation

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.Contract
import net.corda.core.contracts.ContractState
import net.corda.core.schemas.PersistentState
import kotlin.reflect.KClass

/**
 * Generate [ContractState], [PersistentState] types
 * based on the annotated interface.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class StatemeantType(
        /**
         * Configures the [BelongsToContract] annotation added to the generated [ContractState].
         * Takes precedence over [contractClassName],using one of the two is required,
         * unless the annotated interface is contained in a [Contract] type.
         */
        val contractClass: KClass<out Contract> = Contract::class,

        /**
         * Configures the [BelongsToContract] annotation added to the generated [ContractState]..
         * Required unless [contractClass] is used instead or the annotated interface is contained
         * in a [Contract] type, otherwise convenient when [contractClass] cannot be used at build time.
         */
        val contractClassName: String = "",

        /**
         * The type-level mode(s) for mapping [ContractState] to [PersistentState] properties.
         * The default is [StatemeantPropertyMappingMode.EXPANDED]. Can be overridden per property via
         * [StatemeantProperty] annotations.
         */
        val persistentMappingModes: Array<StatemeantPropertyMappingMode> = [StatemeantPropertyMappingMode.EXPANDED],
        val ignoreProperties: Array<String> = [],
        val copyAnnotationPackages: Array<String> = [],
        val contractStateSuperInterface: KClass<out ContractState> = ContractState::class
)

/**
 * Generate [ContractState], [PersistentState] types
 * based on the annotated interface.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class StatemeantProperty(
        /**
         * Apply a custom implementation of
         * `com.github.manosbatsis.vaultaire.processor.state.mapping.PersistentPropertyMapper`
         * to this property.
         */
        val persistentMapperClassName: String = "",
        /**
         * The type-level mode(s) for mapping [ContractState] to [PersistentState] properties.
         * The default is [StatemeantPropertyMappingMode.EXPANDED].
         */
        val persistentMappingModes: Array<StatemeantPropertyMappingMode>
)
