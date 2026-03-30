package com.blimas.mycryptolog.presentation.viewmodel

import com.blimas.mycryptolog.domain.model.ProcessedHolding
import com.blimas.mycryptolog.domain.model.Wallet

sealed class WalletUiState {
    object Loading : WalletUiState()
    data class Success(val wallets: List<Pair<Wallet, List<ProcessedHolding>>>) : WalletUiState()
    data class Error(val message: String) : WalletUiState()
    object Empty : WalletUiState()
}
