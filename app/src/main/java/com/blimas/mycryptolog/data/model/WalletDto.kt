package com.blimas.mycryptolog.data.model

data class WalletDto(
    var name: String = "",
    val cryptoHoldings: Map<String, Double> = emptyMap()
)
