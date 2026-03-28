package com.blimas.mycryptolog.domain.model

import com.google.firebase.database.Exclude

data class Transaction(
    @get:Exclude
    var id: String = "",
    var walletId: String = "",
    val type: String = "",
    val crypto: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val date: Long = 0L
)