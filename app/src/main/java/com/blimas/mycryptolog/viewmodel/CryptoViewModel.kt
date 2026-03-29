package com.blimas.mycryptolog.viewmodel

import androidx.lifecycle.ViewModel
import com.blimas.mycryptolog.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor() : ViewModel() {

    private val _coins = MutableStateFlow<List<Coin>>(emptyList())
    val coins: StateFlow<List<Coin>> = _coins

    init {
        // Simulate fetching data from a live API
        _coins.value = listOf(
            Coin("BTC", "Bitcoin"),
            Coin("ETH", "Ethereum"),
            Coin("USDT", "Tether"),
            Coin("BNB", "BNB"),
            Coin("SOL", "Solana"),
            Coin("XRP", "XRP"),
            Coin("USDC", "USD Coin"),
            Coin("DOGE", "Dogecoin"),
            Coin("ADA", "Cardano"),
            Coin("AVAX", "Avalanche"),
            Coin("SHIB", "Shiba Inu"),
            Coin("DOT", "Polkadot"),
            Coin("LINK", "Chainlink"),
            Coin("TRX", "TRON"),
            Coin("BCH", "Bitcoin Cash"),
            Coin("ICP", "Internet Computer"),
            Coin("LTC", "Litecoin"),
            Coin("NEAR", "NEAR Protocol"),
            Coin("LEO", "UNUS SED LEO"),
            Coin("UNI", "Uniswap")
        )
    }
}