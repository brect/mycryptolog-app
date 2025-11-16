package com.blimas.mycryptolog.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.blimas.mycryptolog.model.Coin
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.blimas.mycryptolog.util.CurrencyVisualTransformation
import com.blimas.mycryptolog.viewmodel.CryptoViewModel
import com.blimas.mycryptolog.viewmodel.DatabaseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    transactionId: String?,
    walletId: String?,
    databaseViewModel: DatabaseViewModel = viewModel(),
    cryptoViewModel: CryptoViewModel = viewModel()
) {
    val wallets by databaseViewModel.wallets.observeAsState(emptyList())

    val allTransactions by databaseViewModel.transactions.observeAsState(emptyList())

    val allCoins by cryptoViewModel.coins.collectAsState()

    val isEditMode = transactionId != null

    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }
    var selectedType by remember { mutableStateOf("BUY") }
    var cryptoSearchText by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    var showCreateWalletDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = transactionId, key2 = wallets, key3 = allTransactions) {
        if (isEditMode && wallets.isNotEmpty() && allTransactions.isNotEmpty()) {
            val transactionToEdit =
                allTransactions.find { it.id == transactionId && it.walletId == walletId }
            transactionToEdit?.let {
                selectedWallet = wallets.find { w -> w.id == it.walletId }
                selectedType = it.type
                cryptoSearchText = it.crypto
                quantity = it.quantity.toString().replace(".", ",")
                price = (it.price * 100).toLong().toString()
                selectedDateMillis = it.date
            }
        }
    }

    AddTransactionContent(
        isEditMode = isEditMode,
        wallets = wallets,
        allCoins = allCoins,
        selectedWallet = selectedWallet,
        onWalletSelected = { selectedWallet = it },
        selectedType = selectedType,
        onTypeSelected = { selectedType = it },
        cryptoSearchText = cryptoSearchText,
        onCryptoSearchTextChange = { cryptoSearchText = it },
        quantity = quantity,
        onQuantityChange = { quantity = it },
        price = price,
        onPriceChange = { price = it },
        selectedDateMillis = selectedDateMillis,
        onDateClicked = { showDatePicker = true },
        onSaveClicked = {
            val cleanQuantity = quantity.replace(",", ".")
            val quantityDouble = cleanQuantity.toDoubleOrNull()
            val priceAsDouble = price.toLongOrNull()?.div(100.0)

            if (selectedWallet == null || cryptoSearchText.isBlank() || quantityDouble == null || priceAsDouble == null) {
                Toast.makeText(context, "Please fill all fields correctly.", Toast.LENGTH_SHORT)
                    .show()
                return@AddTransactionContent
            }

            val transaction = Transaction(
                id = transactionId ?: "",
                walletId = selectedWallet!!.id,
                type = selectedType,
                crypto = cryptoSearchText,
                quantity = quantityDouble,
                price = priceAsDouble,
                date = selectedDateMillis
            )

            if (isEditMode) {
                databaseViewModel.updateTransaction(transaction)
            } else {
                databaseViewModel.saveTransaction(transaction)
            }
            navController.popBackStack()
        },
        onBackClicked = { navController.popBackStack() },
        onCreateWalletClicked = { showCreateWalletDialog = true }
    )

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDateMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showCreateWalletDialog) {
        CreateWalletDialog(
            onDismiss = { showCreateWalletDialog = false },
            onConfirm = { databaseViewModel.createWallet(it); showCreateWalletDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionContent(
    isEditMode: Boolean,
    wallets: List<Wallet>,
    allCoins: List<Coin>,
    selectedWallet: Wallet?,
    onWalletSelected: (Wallet) -> Unit,
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    cryptoSearchText: String,
    onCryptoSearchTextChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    selectedDateMillis: Long,
    onDateClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCreateWalletClicked: () -> Unit
) {
    var isWalletsExpanded by remember { mutableStateOf(false) }
    var isTypesExpanded by remember { mutableStateOf(false) }
    var isCryptoExpanded by remember { mutableStateOf(false) }
    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    val filteredCoins = allCoins.filter {
        it.symbol.contains(
            cryptoSearchText,
            ignoreCase = true
        ) || it.name.contains(cryptoSearchText, ignoreCase = true)
    }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(if (isEditMode) "Edit Transaction" else "Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    ExposedDropdownMenuBox(
                        expanded = isWalletsExpanded,
                        onExpandedChange = { isWalletsExpanded = it }) {
                        OutlinedTextField(
                            value = selectedWallet?.name ?: "Select a Wallet",
                            onValueChange = {}, readOnly = true, label = { Text("Wallet") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWalletsExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = isWalletsExpanded,
                            onDismissRequest = { isWalletsExpanded = false }) {
                            wallets.forEach { wallet ->
                                DropdownMenuItem(
                                    text = { Text(wallet.name) },
                                    onClick = {
                                        onWalletSelected(wallet); isWalletsExpanded = false
                                    })
                            }
                        }
                    }
                }
                IconButton(onClick = onCreateWalletClicked) {
                    Icon(
                        Icons.Default.Add,
                        "Create Wallet"
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = isTypesExpanded,
                onExpandedChange = { isTypesExpanded = it }) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypesExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isTypesExpanded,
                    onDismissRequest = { isTypesExpanded = false }) {
                    listOf("BUY", "SELL").forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = { onTypeSelected(type); isTypesExpanded = false })
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isCryptoExpanded,
                onExpandedChange = { isCryptoExpanded = it }) {
                OutlinedTextField(
                    value = cryptoSearchText,
                    onValueChange = onCryptoSearchTextChange,
                    label = { Text("Crypto (e.g., BTC, ETH)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCryptoExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                if (filteredCoins.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = isCryptoExpanded,
                        onDismissRequest = { isCryptoExpanded = false }) {
                        filteredCoins.forEach { coin ->
                            DropdownMenuItem(
                                text = { Text("${coin.name} (${coin.symbol})") },
                                onClick = {
                                    onCryptoSearchTextChange(coin.symbol); isCryptoExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.clickable(onClick = onDateClicked)) {
                OutlinedTextField(
                    value = dateFormatter.format(Date(selectedDateMillis)),
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Date") },
                    trailingIcon = { Icon(Icons.Default.DateRange, "Select date") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
            }

            OutlinedTextField(
                value = quantity,
                onValueChange = { onQuantityChange(it.filter { char -> char.isDigit() || char == '.' || char == ',' }) },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { onPriceChange(it.filter { char -> char.isDigit() }) },
                label = { Text("Price per coin") },
                visualTransformation = CurrencyVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSaveClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(if (isEditMode) "Update Transaction" else "Save Transaction")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, name = "Add Transaction Mode")
@Composable
fun AddTransactionScreenPreview() {
    MaterialTheme {
        AddTransactionContent(
            isEditMode = false,
            wallets = listOf(Wallet(id = "1", name = "Binance")),
            allCoins = listOf(Coin("BTC", "Bitcoin")),
            selectedWallet = null,
            onWalletSelected = {},
            selectedType = "BUY",
            onTypeSelected = {},
            cryptoSearchText = "",
            onCryptoSearchTextChange = {},
            quantity = "",
            onQuantityChange = {},
            price = "",
            onPriceChange = {},
            selectedDateMillis = System.currentTimeMillis(),
            onDateClicked = {},
            onSaveClicked = {},
            onBackClicked = {},
            onCreateWalletClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Transaction Mode")
@Composable
fun EditTransactionScreenPreview() {
    val sampleWallet = Wallet(id = "1", name = "Binance")
    MaterialTheme {
        AddTransactionContent(
            isEditMode = true,
            wallets = listOf(sampleWallet),
            allCoins = listOf(Coin("BTC", "Bitcoin")),
            selectedWallet = sampleWallet,
            onWalletSelected = {},
            selectedType = "SELL",
            onTypeSelected = {},
            cryptoSearchText = "BTC",
            onCryptoSearchTextChange = {},
            quantity = "0.123",
            onPriceChange = {},
            price = "6543210", // R$ 65.432,10
            selectedDateMillis = System.currentTimeMillis(),
            onDateClicked = {},
            onSaveClicked = {},
            onBackClicked = {},
            onCreateWalletClicked = {},
            onQuantityChange = {},
        )
    }
}