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


package com.github.manosbatsis.corda.statemeant.processor.state.mapping

import com.github.manosbatsis.corda.statemeant.annotation.StatemeantPropertyMappingMode
import com.github.manosbatsis.corda.statemeant.processor.state.BaseStateMembersStrategy
import com.github.manosbatsis.corda.statemeant.processor.state.MappedProperty
import com.squareup.kotlinpoet.asTypeName
import net.corda.core.identity.Party

class IdentityPersistentPropertyMapper(
        delegate: BaseStateMembersStrategy
) : BasePersistentPropertyMapper<Party>(delegate) {

    override fun supportedTypes() = listOf(
            Int::class.java.canonicalName,
            java.lang.Integer::class.java.canonicalName,
            Long::class.java.canonicalName,
            java.lang.Long::class.java.canonicalName,
            Float::class.java.canonicalName,
            java.lang.Float::class.java.canonicalName,
            Short::class.java.canonicalName,
            java.lang.Short::class.java.canonicalName,
            Double::class.java.canonicalName,
            java.lang.Double::class.java.canonicalName,
            java.math.BigDecimal::class.java.canonicalName,
            String::class.java.canonicalName,
            java.lang.String::class.java.canonicalName,
            Boolean::class.java.canonicalName,
            java.lang.Boolean::class.java.canonicalName,
            Byte::class.java.canonicalName,
            java.lang.Byte::class.java.canonicalName,
            java.util.Date::class.java.canonicalName,
            java.sql.Date::class.java.canonicalName,
            java.util.Calendar::class.java.canonicalName,
            Array<Byte>::class.java.asTypeName().toString(),
            java.sql.Clob::class.java.canonicalName,
            java.sql.Blob::class.java.canonicalName,
            java.util.TimeZone::class.java.canonicalName,
            java.util.Currency::class.java.canonicalName,
            java.lang.Class::class.java.canonicalName,
            java.time.LocalTime::class.java.canonicalName,
            java.time.LocalDate::class.java.canonicalName,
            java.time.LocalDateTime::class.java.canonicalName,
            java.time.Instant::class.java.canonicalName,
            java.time.ZonedDateTime::class.java.canonicalName
    )

    override fun map(partyProp: MappedProperty, modes: List<StatemeantPropertyMappingMode>): List<MappedProperty> =
            listOf(partyProp)
}