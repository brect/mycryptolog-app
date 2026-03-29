package com.blimas.mycryptolog.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blimas.mycryptolog.model.ProcessedHolding
import com.blimas.mycryptolog.model.Wallet

@Composable
fun WalletCard(wallet: Wallet, holdings: List<ProcessedHolding>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(wallet.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (holdings.isEmpty()) {
                Text("No holdings yet.")
            } else {
                holdings.forEach { holding ->
                    HoldingItem(holding = holding)
                }
            }
        }
    }
}
