package com.blimas.mycryptolog.domain.usecase.wallet

import androidx.lifecycle.LiveData
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.repository.WalletRepository
import javax.inject.Inject

class GetWalletsUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    operator fun invoke(): LiveData<List<Wallet>> = repository.getWallets()
}
