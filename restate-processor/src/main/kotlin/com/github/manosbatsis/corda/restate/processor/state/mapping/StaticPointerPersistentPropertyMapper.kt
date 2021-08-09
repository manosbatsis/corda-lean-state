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


package com.github.manosbatsis.corda.restate.processor.state.mapping

import com.github.manosbatsis.corda.restate.annotation.PropertyMappingMode
import com.github.manosbatsis.corda.restate.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.restate.processor.state.MappedProperty
import net.corda.core.contracts.StaticPointer

class StaticPointerPersistentPropertyMapper(
        delegate: BaseStateMembersStrategy
) : BasePersistentPropertyMapper<StaticPointer<*>>(delegate) {

    override fun supportedTypes() = listOf(StaticPointer::class.java.canonicalName)

    override fun map(original: MappedProperty, modes: List<PropertyMappingMode>): List<MappedProperty> {
        return listOfNotNull(
                if (modes.contains(PropertyMappingMode.NATIVE)) original else null,
                if (modes.contains(PropertyMappingMode.STRINGIFY))
                    original.asChildMappedProperty(
                            subPath = listOf("txhash"),
                            asString = true,
                            childPropertyName = original.propertyName)
                else null,
                if (modes.contains(PropertyMappingMode.STRINGIFY))
                    original.asChildMappedProperty(
                            subPath = listOf("index"),
                            childPropertyName = original.propertyName)
                else null
                /*
                val txhash: SecureHash, val index: Int
                 */
        ) + if (modes.contains(PropertyMappingMode.EXPANDED))
            listOf(
                    original.asChildMappedProperty(subPath = listOf("type")),
                    original.asChildMappedProperty(subPath = listOf("resolved"))
            )
        else emptyList()
    }
}