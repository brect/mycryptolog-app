package com.blimas.mycryptolog.domain.model

data class ProcessedHolding(
    val crypto: String,
    val currentQuantity: Double,
    val netInvestedValue: Double,
    val avgBuyPrice: Double
)
