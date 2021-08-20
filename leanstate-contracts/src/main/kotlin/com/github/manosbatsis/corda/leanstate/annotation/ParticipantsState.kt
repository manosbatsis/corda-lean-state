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


package com.github.manosbatsis.corda.leanstate.annotation

import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.AnonymousParty
import java.security.PublicKey
import kotlin.reflect.full.memberProperties

/** Helpers for (generated) implementations of [ContractState.participants]. */
interface ParticipantsState {
    companion object {

        fun Any.isKotlinType(): Boolean = this::class.java.isKotlinClass()

        fun Class<*>.isKotlinClass(): Boolean =
                declaredAnnotations.any { it.annotationClass.qualifiedName == "kotlin.Metadata" }


        fun Any.getField(fieldName: String): Any? =
                this::class.memberProperties.find { fieldName == it.name }
                        ?.getter?.call(this)
    }

    fun toAbstractParty(entry: Any?): AbstractParty? {
        return when {
            entry == null -> null
            entry is AbstractParty -> entry
            entry is PublicKey -> toAbstractParty(entry)
            entry.isKotlinType() -> toAbstractParty(entry.getField("party"))
            else -> entry.javaClass.declaredMethods
                    .find { "getParty" == it.name && it.isAccessible }
                    ?.let { toAbstractParty(it.invoke(entry)) }
        }
    }

    fun toAbstractParty(publicKey: PublicKey) = AnonymousParty(publicKey)

    fun toAbstractParties(entries: Collection<*>?): List<AbstractParty> =
            entries?.mapNotNull { toAbstractParty(it) }
                    ?: emptyList()

    fun toParticipants(vararg entry: Any?): List<AbstractParty> =
            entry.filterNotNull().partition { it is Collection<*> }.run {
                first.map { toAbstractParties(it as Collection<*>) }.flatten() +
                        second.mapNotNull { toAbstractParty(it) }
            }
}