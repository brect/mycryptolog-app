package com.blimas.mycryptolog.repository

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet

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