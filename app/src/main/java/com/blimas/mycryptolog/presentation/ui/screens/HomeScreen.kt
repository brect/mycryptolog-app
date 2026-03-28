package com.blimas.mycryptolog.presentation.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blimas.mycryptolog.domain.model.Transaction
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.presentation.auth.AuthViewModel
import com.blimas.mycryptolog.presentation.dashboard.DatabaseViewModel
import kotlinx.coroutines.launch

// --- STATEFUL COMPOSABLE ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    databaseViewModel: DatabaseViewModel = hiltViewModel(),
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
                    0 -> WalletsScreen(databaseViewModel)
                    1 -> TransactionsScreen(transactions, wallets, navController, databaseViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    val dummyNavController = NavController(LocalContext.current)

    MaterialTheme {
    }
}
