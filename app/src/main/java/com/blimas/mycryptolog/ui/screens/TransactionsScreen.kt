package com.blimas.mycryptolog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.blimas.mycryptolog.ui.components.TransactionCard
import com.blimas.mycryptolog.viewmodel.DatabaseViewModel

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    wallets: List<Wallet>,
    navController: NavController,
    databaseViewModel: DatabaseViewModel
) {
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    TransactionsContent(
        transactions = transactions,
        wallets = wallets,
        onEditClick = {
            navController.navigate("add_transaction?transactionId=${it.id}&walletId=${it.walletId}")
        },
        onDeleteClick = { transactionToDelete = it }
    )

    transactionToDelete?.let {
        DeleteConfirmationDialog(
            onDismiss = { transactionToDelete = null },
            onConfirm = {
                databaseViewModel.deleteTransaction(it)
                transactionToDelete = null
            }
        )
    }
}

@Composable
private fun TransactionsContent(
    transactions: List<Transaction>,
    wallets: List<Wallet>,
    onEditClick: (Transaction) -> Unit,
    onDeleteClick: (Transaction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(transactions) { transaction ->
            val walletName = wallets.find { it.id == transaction.walletId }?.name ?: "Unknown Wallet"
            TransactionCard(
                transaction = transaction,
                walletName = walletName,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Preview(showBackground = true, name = "Transactions Screen Preview")
@Composable
fun TransactionsScreenPreview() {
    val sampleWallets = listOf(
        Wallet(id = "1", name = "Binance"),
        Wallet(id = "2", name = "Cold Wallet")
    )
    val sampleTransactions = listOf(
        Transaction("t1", "1", "BUY", "BTC", 0.5, 60000.0, System.currentTimeMillis()),
        Transaction("t2", "1", "SELL", "ETH", 10.0, 4000.0, System.currentTimeMillis() - 100000),
        Transaction("t3", "2", "BUY", "ADA", 100.0, 0.45, System.currentTimeMillis() - 200000)
    )

    MaterialTheme {
        TransactionsContent(
            transactions = sampleTransactions,
            wallets = sampleWallets,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
