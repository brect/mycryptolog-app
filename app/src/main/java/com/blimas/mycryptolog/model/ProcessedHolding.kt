package com.blimas.mycryptolog.model

data class ProcessedHolding(
    val crypto: String,
    val currentQuantity: Double,
    val netInvestedValue: Double,
    val avgBuyPrice: Double
)