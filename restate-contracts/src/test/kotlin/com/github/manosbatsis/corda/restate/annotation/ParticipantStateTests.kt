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
package com.github.manosbatsis.corda.restate.annotation

import com.github.manosbatsis.vaultaire.dto.AccountParty
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.testing.core.TestIdentity
import org.junit.jupiter.api.Test
import java.security.PublicKey
import java.util.*
import kotlin.test.assertEquals

class ParticipantStateTests {

    data class Foo(
            val bar: AccountParty,
            val baz: Party,
            val dah: PublicKey
    ) : ParticipantsState {
        val participants = toParticipants(bar, baz, dah)
    }

    val alice = TestIdentity(CordaX500Name("Alice", "New York", "US"))
    val miniCorp = TestIdentity(CordaX500Name("MiniCorp", "New York", "US"))


    @Test
    fun canHandleAccountParty() {

        val foo = Foo(
                AccountParty(
                        UUID.randomUUID(),
                        "Foo name",
                        alice.party.anonymise()),
                miniCorp.party,
                alice.publicKey
        )

        assertEquals(3, foo.participants.size)
    }
}