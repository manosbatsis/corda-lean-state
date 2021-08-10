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


package com.github.manosbatsis.corda.leanstate.processor.state.mapping

import com.github.manosbatsis.corda.leanstate.annotation.PropertyMappingMode
import com.github.manosbatsis.corda.leanstate.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.leanstate.processor.state.MappedProperty
import net.corda.core.identity.Party

class PartyPersistentPropertyMapper(
        delegate: BaseStateMembersStrategy
) : BasePersistentPropertyMapper<Party>(delegate) {

    override fun supportedTypes() = listOf(Party::class.java.canonicalName)

    override fun map(original: MappedProperty, modes: List<PropertyMappingMode>): List<MappedProperty> {
        return listOfNotNull(
                if (modes.contains(PropertyMappingMode.NATIVE))
                    original.asChildMappedProperty(subPath = listOf("name"))
                else null,
                if (modes.contains(PropertyMappingMode.STRINGIFY))
                    original.asChildMappedProperty(subPath = listOf("name"), asString = true)
                else null
        ) + if (modes.contains(PropertyMappingMode.EXPANDED))
            listOf(
                    original.asChildMappedProperty(subPath = listOf("name", "commonName")),
                    original.asChildMappedProperty(subPath = listOf("name", "organisationUnit")),
                    original.asChildMappedProperty(subPath = listOf("name", "organisation")),
                    original.asChildMappedProperty(subPath = listOf("name", "locality")),
                    original.asChildMappedProperty(subPath = listOf("name", "state")),
                    original.asChildMappedProperty(subPath = listOf("name", "country"))
            )
        else emptyList()
    }

}