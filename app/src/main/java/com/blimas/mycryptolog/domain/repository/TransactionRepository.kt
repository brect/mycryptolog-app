package com.blimas.mycryptolog.domain.repository

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.domain.model.Transaction

interface TransactionRepository {
    fun getTransactions(walletIds: List<String>): LiveData<List<Transaction>>
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}
