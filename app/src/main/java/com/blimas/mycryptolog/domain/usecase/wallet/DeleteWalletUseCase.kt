package com.blimas.mycryptolog.domain.usecase.wallet

import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.repository.WalletRepository
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(wallet: Wallet) = repository.deleteWallet(wallet)
}
