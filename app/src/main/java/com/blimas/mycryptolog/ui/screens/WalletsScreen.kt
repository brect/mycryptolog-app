package com.blimas.mycryptolog.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blimas.mycryptolog.model.ProcessedHolding
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.blimas.mycryptolog.viewmodel.DatabaseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WalletsScreen(
    wallets: List<Wallet>,
    allTransactions: List<Transaction>,
    databaseViewModel: DatabaseViewModel
) {
    var walletToEdit by remember { mutableStateOf<Wallet?>(null) }
    var walletToDelete by remember { mutableStateOf<Wallet?>(null) }

    val processedWallets = wallets.map { wallet ->
        val holdings = wallet.cryptoHoldings.mapNotNull { (crypto, currentQuantity) ->
            if (currentQuantity > 0) {
                val relevantTransactions =
                    allTransactions.filter { it.walletId == wallet.id && it.crypto == crypto }

                val relevantBuys =
                    relevantTransactions.filter { it.type.equals("BUY", ignoreCase = true) }

                val totalCostOfBuys = relevantBuys.sumOf { it.price * it.quantity }

                val totalQuantityBought = relevantBuys.sumOf { it.quantity }

                val totalProceedsFromSells =
                    relevantTransactions.filter { it.type.equals("SELL", ignoreCase = true) }
                        .sumOf { it.price * it.quantity }

                ProcessedHolding(
                    crypto = crypto,
                    currentQuantity = currentQuantity,
                    netInvestedValue = totalCostOfBuys - totalProceedsFromSells,
                    avgBuyPrice = if (totalQuantityBought > 0) totalCostOfBuys / totalQuantityBought else 0.0
                )
            } else null
        }
        wallet to holdings
    }

    WalletsContent(
        processedWallets = processedWallets,
        onEditWallet = { walletToEdit = it },
        onDeleteWallet = { walletToDelete = it }
    )

    walletToEdit?.let {
        EditWalletDialog(
            wallet = it,
            onDismiss = { walletToEdit = null },
            onConfirm = {
                databaseViewModel.updateWallet(it)
                walletToEdit = null
            }
        )
    }

    walletToDelete?.let {
        DeleteWalletDialog(
            onDismiss = { walletToDelete = null },
            onConfirm = {
                databaseViewModel.deleteWallet(it)
                walletToDelete = null
            }
        )
    }
}

@Composable
private fun WalletsContent(
    processedWallets: List<Pair<Wallet, List<ProcessedHolding>>>,
    onEditWallet: (Wallet) -> Unit,
    onDeleteWallet: (Wallet) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(processedWallets) { (wallet, holdings) ->
            WalletCard(
                wallet = wallet,
                holdings = holdings,
                onEditClick = { onEditWallet(wallet) },
                onDeleteClick = { onDeleteWallet(wallet) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalletCard(
    wallet: Wallet,
    holdings: List<ProcessedHolding>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = wallet.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Edit Name") }, onClick = {
                            onEditClick()
                            isMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Delete Wallet") }, onClick = {
                            onDeleteClick()
                            isMenuExpanded = false
                        })
                    }
                }
            }

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

@Composable
private fun HoldingItem(holding: ProcessedHolding) {
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
            Text(
                currencyFormatter.format(holding.netInvestedValue),
                fontWeight = FontWeight.SemiBold
            )
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

@Composable
private fun DeleteWalletDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Wallet") },
        text = { Text("Are you sure? Deleting a wallet will also delete ALL transactions associated with it. This action cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Delete") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditWalletDialog(wallet: Wallet, onDismiss: () -> Unit, onConfirm: (Wallet) -> Unit) {
    var newName by remember { mutableStateOf(wallet.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Wallet Name") },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("New name") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                if (newName.isNotBlank()) {
                    onConfirm(wallet.copy(name = newName))
                }
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Preview(showBackground = true, name = "Wallets Screen Preview")
@Composable
fun WalletsScreenPreview() {
    val sampleWallets = listOf(
        Wallet(id = "1", name = "Binance") to listOf(
            ProcessedHolding("BTC", 0.5, 30000.0, 60000.0),
            ProcessedHolding("ETH", 10.0, 39000.0, 3900.0)
        ),
        Wallet(id = "2", name = "Cold Wallet", cryptoHoldings = emptyMap()) to emptyList()
    )

    MaterialTheme {
        WalletsContent(
            processedWallets = sampleWallets,
            onEditWallet = {},
            onDeleteWallet = {}
        )
    }
}