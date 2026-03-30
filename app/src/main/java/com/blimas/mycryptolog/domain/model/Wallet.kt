package com.blimas.mycryptolog.domain.model

data class Wallet(
    var id: String = "",
    var name: String = "",
    val cryptoHoldings: Map<String, Double> = emptyMap()
)