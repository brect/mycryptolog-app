package com.blimas.mycryptolog.domain.usecase.wallet

import com.blimas.mycryptolog.domain.repository.WalletRepository
import javax.inject.Inject

class CreateWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(name: String) = repository.createWallet(name)
}
