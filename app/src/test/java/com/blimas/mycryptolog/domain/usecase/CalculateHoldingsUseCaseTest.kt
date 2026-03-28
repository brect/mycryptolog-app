package com.blimas.mycryptolog.domain.usecase

import com.blimas.mycryptolog.domain.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateHoldingsUseCaseTest {

    private val useCase = CalculateHoldingsUseCase()

    @Test
    fun `quando houver compras, o saldo deve ser a soma das quantidades`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 0.5, type = "BUY"),
            Transaction(crypto = "BTC", quantity = 0.2, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(0.7, result.first { it.crypto == "BTC" }.currentQuantity, 0.001)
    }

    @Test
    fun `quando houver venda, o saldo deve subtrair a quantidade vendida`() {
        val transactions = listOf(
            Transaction(crypto = "ETH", quantity = 1.0, type = "BUY"),
            Transaction(crypto = "ETH", quantity = 0.4, type = "SELL")
        )
        val result = useCase(transactions)
        assertEquals(0.6, result.first { it.crypto == "ETH" }.currentQuantity, 0.001)
    }

    @Test
    fun `moedas diferentes devem ter saldos separados`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 0.1, type = "BUY"),
            Transaction(crypto = "ETH", quantity = 2.0, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(2, result.size)
    }

    @Test
    fun `se a quantidade final for zero, a moeda nao deve aparecer na lista`() {
        val transactions = listOf(
            Transaction(crypto = "SOL", quantity = 10.0, type = "BUY"),
            Transaction(crypto = "SOL", quantity = 10.0, type = "SELL")
        )
        val result = useCase(transactions)
        assertEquals(0, result.size)
    }

    @Test
    fun `o preco medio de compra deve ser calculado corretamente`() {
        val transactions = listOf(
            Transaction(crypto = "BTC", quantity = 1.0, price = 50000.0, type = "BUY"),
            Transaction(crypto = "BTC", quantity = 1.0, price = 60000.0, type = "BUY")
        )
        val result = useCase(transactions)
        assertEquals(55000.0, result.first { it.crypto == "BTC" }.avgBuyPrice, 0.001)
    }
}
