package com.blimas.mycryptolog.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    val wallets: LiveData<List<Wallet>> = repository.getWallets()

    val transactions: LiveData<List<Transaction>> = wallets.switchMap { walletList ->
        repository.getTransactions(walletList.map { it.id })
    }

    fun createWallet(name: String) {
        viewModelScope.launch {
            repository.createWallet(name)
        }
    }

    fun updateWallet(wallet: Wallet) {
        viewModelScope.launch {
            repository.updateWallet(wallet)
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch {
            repository.deleteWallet(wallet)
        }
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.saveTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
}