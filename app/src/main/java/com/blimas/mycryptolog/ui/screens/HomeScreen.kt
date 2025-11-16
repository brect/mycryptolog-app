package com.blimas.mycryptolog.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.blimas.mycryptolog.viewmodel.AuthViewModel
import com.blimas.mycryptolog.viewmodel.DatabaseViewModel
import kotlinx.coroutines.launch

// --- STATEFUL COMPOSABLE ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    databaseViewModel: DatabaseViewModel = viewModel(),
) {
    val wallets by databaseViewModel.wallets.observeAsState(emptyList())
    val transactions by databaseViewModel.transactions.observeAsState(emptyList())

    HomeContent(
        wallets = wallets,
        transactions = transactions,
        onSignOutClick = { authViewModel.signOut() },
        onAddTransactionClick = { navController.navigate("add_transaction") },
        navController = navController,
        databaseViewModel = databaseViewModel,
    )
}

// --- STATELESS COMPOSABLE ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    wallets: List<Wallet>,
    transactions: List<Transaction>,
    onSignOutClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    navController: NavController,
    databaseViewModel: DatabaseViewModel,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val tabTitles = listOf("Wallets", "Transactions")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Crypto Log") },
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> WalletsScreen(wallets, transactions, databaseViewModel)
                    1 -> TransactionsScreen(transactions, wallets, navController, databaseViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    val sampleWallets = listOf(
        Wallet(id = "1", name = "Binance", cryptoHoldings = mapOf("BTC" to 0.5, "ETH" to 10.0)),
        Wallet(id = "2", name = "Cold Wallet", cryptoHoldings = mapOf("ADA" to 5000.0))
    )
    val sampleTransactions = listOf(
        Transaction("t1", "1", "BUY", "BTC", 0.5, 60000.0, System.currentTimeMillis()),
        Transaction("t2", "1", "SELL", "ETH", 2.0, 4100.0, System.currentTimeMillis() - 100000),
        Transaction("t3", "2", "BUY", "ADA", 5000.0, 0.45, System.currentTimeMillis() - 200000)
    )

    // Dummy NavController and ViewModels for preview
    val dummyNavController = NavController(LocalContext.current)
    val dummyViewModel: DatabaseViewModel = viewModel()

    MaterialTheme {
        HomeContent(
            wallets = sampleWallets,
            transactions = sampleTransactions,
            onSignOutClick = {},
            onAddTransactionClick = {},
            navController = dummyNavController,
            databaseViewModel = dummyViewModel,
        )
    }
}