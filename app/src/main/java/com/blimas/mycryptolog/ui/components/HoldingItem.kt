package com.blimas.mycryptolog.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blimas.mycryptolog.model.ProcessedHolding
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HoldingItem(holding: ProcessedHolding) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(holding.crypto, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Quantity", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(String.format(Locale.US, "%.8f", holding.currentQuantity))
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Net Invested", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(currencyFormatter.format(holding.netInvestedValue), fontWeight = FontWeight.SemiBold)
        }
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Avg. Price: ${currencyFormatter.format(holding.avgBuyPrice)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}