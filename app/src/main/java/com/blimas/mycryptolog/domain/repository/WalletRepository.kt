package com.blimas.mycryptolog.domain.repository

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet

interface WalletRepository {
    fun getWallets(): LiveData<List<Wallet>>
    fun getTransactions(walletIds: List<String>): LiveData<List<Transaction>>
    suspend fun createWallet(name: String)
    suspend fun updateWallet(wallet: Wallet)
    suspend fun deleteWallet(wallet: Wallet)
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}