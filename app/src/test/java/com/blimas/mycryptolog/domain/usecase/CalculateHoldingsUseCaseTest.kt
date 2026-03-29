package com.blimas.mycryptolog.domain.usecase

import com.blimas.mycryptolog.domain.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateHoldingsUseCaseTest {

    private val useCase = CalculateHoldingsUseCase()

    @Test
    fun `should sum quantities correctly when multiple buys occur`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 0.5, type = "BUY"),
            Transaction(crypto = "BTC", quantity = 0.2, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(0.7, result.first { it.crypto == "BTC" }.currentQuantity, 0.001)
    }

    @Test
    fun `should subtract quantity correctly when a sell occurs`() {
        val transactions = listOf(
            Transaction(crypto = "ETH", quantity = 1.0, type = "BUY"),
            Transaction(crypto = "ETH", quantity = 0.4, type = "SELL")
        )
        val result = useCase(transactions)
        assertEquals(0.6, result.first { it.crypto == "ETH" }.currentQuantity, 0.001)
    }

    @Test
    fun `should keep separate balances for different assets`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 0.1, type = "BUY"),
            Transaction(crypto = "ETH", quantity = 2.0, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(2, result.size)
    }

    @Test
    fun `should not include asset in the list if the final quantity is zero`() {
        val transactions = listOf(
            Transaction(crypto = "SOL", quantity = 10.0, type = "BUY"),
            Transaction(crypto = "SOL", quantity = 10.0, type = "SELL")
        )
        val result = useCase(transactions)
        assertEquals(0, result.size)
    }

    @Test
    fun `should calculate the average buy price correctly`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 1.0, price = 50000.0, type = "BUY"),
            Transaction(crypto = "BTC", quantity = 1.0, price = 60000.0, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(55000.0, result.first { it.crypto == "BTC" }.avgBuyPrice, 0.001)
    }
}
