package br.com.fiap.hospitalmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.hospitalmanagement.screens.AlertsScreen
import br.com.fiap.hospitalmanagement.screens.HomeScreen
import br.com.fiap.hospitalmanagement.screens.FuncionalDataScreen
import br.com.fiap.hospitalmanagement.screens.LogisticsScreen
import br.com.fiap.hospitalmanagement.screens.LoginScreen
import br.com.fiap.hospitalmanagement.screens.PrevAIScreen
import br.com.fiap.hospitalmanagement.screens.InitialScreen
import br.com.fiap.hospitalmanagement.screens.StockScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.SplashScreen.route
    ) {
        composable(Destination.SplashScreen.route) {
            InitialScreen(navController)
        }
        composable(Destination.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(
            route = Destination.FunctionalDataScreen.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            FuncionalDataScreen(navController, email)
        }
        composable(
            route = Destination.DashboardScreen.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeScreen(navController, email)
        }
        composable(Destination.StockScreen.route) {
            StockScreen(navController)
        }
        composable(Destination.AlertsScreen.route) {
            AlertsScreen(navController)
        }
        composable(Destination.LogisticsScreen.route) {
            LogisticsScreen(navController)
        }
        composable(Destination.PrevAIScreen.route) {
            PrevAIScreen(navController)
        }
    }
}