package com.blimas.mycryptolog.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blimas.mycryptolog.domain.model.ProcessedHolding
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.presentation.ui.components.WalletCard
import com.blimas.mycryptolog.presentation.viewmodel.WalletViewModel

@Composable
fun WalletsScreen(
    walletViewModel: WalletViewModel
) {
    var walletToEdit by remember { mutableStateOf<Wallet?>(null) }
    var walletToDelete by remember { mutableStateOf<Wallet?>(null) }

    val processedWallets by walletViewModel.processedWallets.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        WalletsContent(
            processedWallets = processedWallets,
            onEditWallet = { walletToEdit = it },
            onDeleteWallet = { walletToDelete = it }
        )
    }

    walletToEdit?.let {
        EditWalletDialog(
            wallet = it,
            onDismiss = { walletToEdit = null },
            onConfirm = {
                walletViewModel.updateWallet(it)
                walletToEdit = null
            }
        )
    }

    walletToDelete?.let {
        DeleteWalletDialog(
            onDismiss = { walletToDelete = null },
            onConfirm = {
                walletViewModel.deleteWallet(it)
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
