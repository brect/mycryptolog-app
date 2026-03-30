package com.blimas.mycryptolog.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.usecase.CalculateHoldingsUseCase
import com.blimas.mycryptolog.domain.usecase.transaction.GetTransactionsUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.CreateWalletUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.DeleteWalletUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.GetWalletsUseCase
import com.blimas.mycryptolog.domain.usecase.wallet.UpdateWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletsUseCase: GetWalletsUseCase,
    private val createWalletUseCase: CreateWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val calculateHoldingsUseCase: CalculateHoldingsUseCase
) : ViewModel() {

    val wallets: LiveData<List<Wallet>> = getWalletsUseCase()

    val uiState: LiveData<WalletUiState> = wallets.switchMap { walletList ->
        if (walletList.isEmpty()) {
            MutableLiveData(WalletUiState.Empty)
        } else {
            getTransactionsUseCase(walletList.map { it.id }).map { allTxs ->
                val processed = walletList.map { wallet ->
                    val walletTxs = allTxs.filter { it.walletId == wallet.id }
                    wallet to calculateHoldingsUseCase(walletTxs)
                }
                WalletUiState.Success(processed)
            }
        }
    }

    fun createWallet(name: String) {
        viewModelScope.launch { createWalletUseCase(name) }
    }

    fun updateWallet(wallet: Wallet) {
        viewModelScope.launch { updateWalletUseCase(wallet) }
    }

    fun deleteWallet(wallet: Wallet) {
        viewModelScope.launch { deleteWalletUseCase(wallet) }
    }
}
