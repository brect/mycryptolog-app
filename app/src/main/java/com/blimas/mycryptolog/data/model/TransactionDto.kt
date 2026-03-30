package com.blimas.mycryptolog.data.model

data class TransactionDto(
    var walletId: String = "",
    val type: String = "",
    val crypto: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val date: Long = 0L
)
