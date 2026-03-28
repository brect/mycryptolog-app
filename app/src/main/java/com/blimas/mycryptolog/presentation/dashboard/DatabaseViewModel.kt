package com.blimas.mycryptolog.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.ProcessedHolding
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.repository.WalletRepository
import com.blimas.mycryptolog.domain.usecase.CalculateHoldingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val calculateHoldingsUseCase: CalculateHoldingsUseCase
) : ViewModel() {

    val wallets: LiveData<List<Wallet>> = repository.getWallets()

    val transactions: LiveData<List<Transaction>> = wallets.switchMap { walletList ->
        repository.getTransactions(walletList.map { it.id })
    }

    val processedWallets: LiveData<List<Pair<Wallet, List<ProcessedHolding>>>> =
        transactions.map { allTxs ->
            val currentWallets = wallets.value ?: emptyList()
            currentWallets.map { wallet ->
                val walletTxs = allTxs.filter { it.walletId == wallet.id }
                wallet to calculateHoldingsUseCase(walletTxs)
            }
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
