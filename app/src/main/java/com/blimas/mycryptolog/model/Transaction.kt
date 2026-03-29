package com.blimas.mycryptolog.model

import com.google.firebase.database.Exclude

data class Transaction(
    @get:Exclude // Exclude from being saved in the database again as it's the key
    var id: String = "",
    var walletId: String = "", // ID of the wallet this transaction belongs to
    val type: String = "", // "BUY" or "SELL"
    val crypto: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val date: Long = 0L
)