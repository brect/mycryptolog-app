package com.blimas.mycryptolog.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.ProcessedHolding
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.usecase.CalculateHoldingsUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.DeleteTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.GetTransactionsUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.SaveTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.UpdateTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.CreateWalletUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.DeleteWalletUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.GetWalletsUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.UpdateWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val getWalletsUseCase: GetWalletsUseCase,
    private val createWalletUseCase: CreateWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val calculateHoldingsUseCase: CalculateHoldingsUseCase
) : ViewModel() {

    // Obtém as carteiras através do Use Case
    val wallets: LiveData<List<Wallet>> = getWalletsUseCase()

    // Obtém as transações de forma reativa baseada nas carteiras
    val transactions: LiveData<List<Transaction>> = wallets.switchMap { walletList ->
        getTransactionsUseCase(walletList.map { it.id })
    }

    // Processa os dados de saldo usando o Use Case de cálculo
    val processedWallets: LiveData<List<Pair<Wallet, List<ProcessedHolding>>>> = transactions.map { allTxs ->
        val currentWallets = wallets.value ?: emptyList()
        currentWallets.map { wallet ->
            val walletTxs = allTxs.filter { it.walletId == wallet.id }
            wallet to calculateHoldingsUseCase(walletTxs)
        }
    }

    fun createWallet(name: String) {
        viewModelScope.launch {
            createWalletUseCase(name)
        }
    }

    fun updateWallet(wallet: Wallet) {
        viewModelScope.launch {
            updateWalletUseCase(wallet)
        }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch {
            deleteWalletUseCase(wallet)
        }
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            saveTransactionUseCase(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            updateTransactionUseCase(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
        }
    }
}
