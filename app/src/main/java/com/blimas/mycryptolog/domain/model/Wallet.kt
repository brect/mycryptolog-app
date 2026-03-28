package com.blimas.mycryptolog.domain.model

import com.google.firebase.database.Exclude

data class Wallet(
    @get:Exclude
    var id: String = "",
    var name: String = "",
    val cryptoHoldings: Map<String, Double> = emptyMap()
)