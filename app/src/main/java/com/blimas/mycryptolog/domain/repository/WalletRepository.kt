package com.blimas.mycryptolog.domain.repository

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.domain.model.Wallet

interface WalletRepository {
    fun getWallets(): LiveData<List<Wallet>>
    suspend fun createWallet(name: String)
    suspend fun updateWallet(wallet: Wallet)
    suspend fun deleteWallet(wallet: Wallet)
}
