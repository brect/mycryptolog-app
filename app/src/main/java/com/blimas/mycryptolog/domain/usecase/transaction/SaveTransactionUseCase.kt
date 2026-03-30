package com.blimas.mycryptolog.domain.usecase.transaction

import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.repository.TransactionRepository
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = repository.saveTransaction(transaction)
}
