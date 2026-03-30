package com.blimas.mycryptolog.data.mapper

import com.blimas.mycryptolog.data.model.TransactionDto
import com.blimas.mycryptolog.data.model.WalletDto
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet

fun WalletDto.toDomain(id: String): Wallet = Wallet(
    id = id,
    name = name,
    cryptoHoldings = cryptoHoldings
)

fun Wallet.toDto(): WalletDto = WalletDto(
    name = name,
    cryptoHoldings = cryptoHoldings
)

fun TransactionDto.toDomain(id: String): Transaction = Transaction(
    id = id,
    walletId = walletId,
    type = type,
    crypto = crypto,
    quantity = quantity,
    price = price,
    date = date
)

fun Transaction.toDto(): TransactionDto = TransactionDto(
    walletId = walletId,
    type = type,
    crypto = crypto,
    quantity = quantity,
    price = price,
    date = date
)
