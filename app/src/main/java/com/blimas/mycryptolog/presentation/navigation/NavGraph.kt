package com.blimas.mycryptolog.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.blimas.mycryptolog.domain.model.AuthState
import com.blimas.mycryptolog.presentation.viewmodel.AuthViewModel
import com.blimas.mycryptolog.presentation.ui.screens.AddTransactionScreen
import com.blimas.mycryptolog.presentation.ui.screens.HomeScreen
import com.blimas.mycryptolog.presentation.ui.screens.auth.LoginScreen
import com.blimas.mycryptolog.presentation.ui.screens.auth.SignUpScreen

@Composable
fun NavGraph(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.AUTHENTICATED -> {
                navController.navigate(Graph.APP) { popUpTo(0) }
            }
            AuthState.UNAUTHENTICATED -> {
                navController.navigate(Graph.AUTH) { popUpTo(0) }
            }
            AuthState.LOADING -> { /* Do nothing */ }
        }
    }

    NavHost(navController = navController, startDestination = Graph.SPLASH) {
        composable(Graph.SPLASH) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        authGraph(navController = navController)
        appGraph(navController = navController)
    }
}

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(startDestination = "login", route = Graph.AUTH) {
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = {}
            )
        }
        composable("signup") {
            SignUpScreen(onLoginClick = { navController.popBackStack() })
        }
    }
}

fun NavGraphBuilder.appGraph(navController: NavController) {
    navigation(startDestination = "home", route = Graph.APP) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "add_transaction?transactionId={transactionId}&walletId={walletId}",
            arguments = listOf(
                navArgument("transactionId") { nullable = true },
                navArgument("walletId") { nullable = true }
            )
        ) { backStackEntry ->
            AddTransactionScreen(
                navController = navController,
                transactionId = backStackEntry.arguments?.getString("transactionId"),
                walletId = backStackEntry.arguments?.getString("walletId")
            )
        }
    }
}