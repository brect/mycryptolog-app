package com.blimas.mycryptolog.domain.usecase.transaction

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(walletIds: List<String>): LiveData<List<Transaction>> = repository.getTransactions(walletIds)
}
