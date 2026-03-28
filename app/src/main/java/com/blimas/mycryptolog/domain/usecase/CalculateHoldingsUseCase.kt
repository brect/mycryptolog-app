package com.blimas.mycryptolog.domain.usecase

import com.blimas.mycryptolog.domain.model.ProcessedHolding
import com.blimas.mycryptolog.domain.model.Transaction
import javax.inject.Inject

class CalculateHoldingsUseCase @Inject constructor() {

    operator fun invoke(transactions: List<Transaction>): List<ProcessedHolding> {
        val holdingsMap = transactions.groupBy { it.crypto }.mapValues { (_, txs) ->
            txs.sumOf { tx ->
                if (tx.type.equals("BUY", ignoreCase = true)) tx.quantity else -tx.quantity
            }
        }.filterValues { it != 0.0 }

        return holdingsMap.map { (crypto, currentQuantity) ->
            val cryptoTransactions = transactions.filter { it.crypto == crypto }
            val relevantBuys = cryptoTransactions.filter { it.type.equals("BUY", ignoreCase = true) }
            
            val totalCostOfBuys = relevantBuys.sumOf { it.price * it.quantity }
            val totalQuantityBought = relevantBuys.sumOf { it.quantity }
            
            val totalProceedsFromSells = cryptoTransactions.filter { it.type.equals("SELL", ignoreCase = true) }
                .sumOf { it.price * it.quantity }

            ProcessedHolding(
                crypto = crypto,
                currentQuantity = currentQuantity,
                netInvestedValue = totalCostOfBuys - totalProceedsFromSells,
                avgBuyPrice = if (totalQuantityBought > 0) totalCostOfBuys / totalQuantityBought else 0.0
            )
        }
    }
}
