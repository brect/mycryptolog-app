package com.blimas.mycryptolog.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.presentation.ui.components.TransactionCard
import com.blimas.mycryptolog.presentation.viewmodel.TransactionViewModel
import com.blimas.mycryptolog.presentation.viewmodel.WalletViewModel

@Composable
fun TransactionsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    walletViewModel: WalletViewModel
) {
    val transactions by transactionViewModel.transactions.observeAsState(emptyList())
    val wallets by walletViewModel.wallets.observeAsState(emptyList())
    
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
                transactionViewModel.deleteTransaction(it)
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
