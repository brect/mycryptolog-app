package com.blimas.mycryptolog.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.usecase.transaction.DeleteTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.GetTransactionsUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.SaveTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.UpdateTransactionUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.GetWalletsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getWalletsUseCase: GetWalletsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    // As transações dependem das IDs das carteiras existentes
    val transactions: LiveData<List<Transaction>> = getWalletsUseCase().switchMap { walletList ->
        getTransactionsUseCase(walletList.map { it.id })
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
