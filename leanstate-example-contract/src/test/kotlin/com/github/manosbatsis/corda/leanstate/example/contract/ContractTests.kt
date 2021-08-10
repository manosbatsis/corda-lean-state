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


package com.github.manosbatsis.corda.leanstate.example.contract

import com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContract.Commands
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.DummyCommandData
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*


class ContractTests {

    val cordappPackages = listOf(NEWSPAPER_CONTRACT_PACKAGE)

    private val alice = TestIdentity(CordaX500Name("Alice", "New York", "US"))
    private val miniCorp = TestIdentity(CordaX500Name("MiniCorp", "New York", "US"))
    private val ledgerServices = MockServices(cordappPackages)


    @Test
    fun bookTransactionMustBeWellFormed() {
        val state = NewsPaperContractState(
                author = alice.party,
                price = BigDecimal.TEN,
                editions = 2,
                title = "Foobar Daily",
                published = Date())

        // Tests.
        ledgerServices.ledger {
            // Input state present.
            transaction {
                input(NEWSPAPER_CONTRACT_ID, state)
                command(alice.publicKey, Commands.Create())
                output(NEWSPAPER_CONTRACT_ID, state)
                this.failsWith("There can be no inputs when creating publications.")
            }
            // Wrong command.
            transaction {
                output(NEWSPAPER_CONTRACT_ID, state)
                command(alice.publicKey, DummyCommandData)
                this.failsWith("")
            }
            // Command signed by wrong key.
            transaction {
                output(NEWSPAPER_CONTRACT_ID, state)
                command(miniCorp.publicKey, Commands.Create())
                this.failsWith("The publication must be signed by the publisher.")
            }
            // Sending to yourself is not allowed.
            transaction {
                output(NEWSPAPER_CONTRACT_ID, state.copy(publisher = alice.party))
                command(alice.publicKey, Commands.Create())
                this.failsWith("Cannot publish your own publication!")
            }
            transaction {
                output(NEWSPAPER_CONTRACT_ID, state.copy(publisher = miniCorp.party))
                command(listOf(alice.publicKey, miniCorp.publicKey), Commands.Create())
                this.verifies()
            }
        }
    }
}
