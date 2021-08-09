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
package com.github.manosbatsis.corda.statemeant.processor.state.mapping

import com.github.manosbatsis.corda.statemeant.annotation.StatemeantPropertyMappingMode
import com.github.manosbatsis.corda.statemeant.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.statemeant.processor.state.MappedProperty
import net.corda.core.contracts.LinearPointer

class LinearPointerPersistentPropertyMapper(
        delegate: BaseStateMembersStrategy
) : BasePersistentPropertyMapper<LinearPointer<*>>(delegate) {

    override fun supportedTypes() = listOf(LinearPointer::class.java.canonicalName)

    override fun map(original: MappedProperty, modes: List<StatemeantPropertyMappingMode>): List<MappedProperty> {
        return listOfNotNull(
                original.asChildMappedProperty(
                        subPath = listOf("pointer", "id"),
                        childPropertyName = original.propertyName
                )
        ) + if (modes.contains(StatemeantPropertyMappingMode.EXPANDED))
            listOf(
                    original.asChildMappedProperty(subPath = listOf("type")),
                    original.asChildMappedProperty(subPath = listOf("resolved"))
            )
        else emptyList()
    }
}