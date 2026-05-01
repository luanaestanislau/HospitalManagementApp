package br.com.fiap.hospitalmanagement.navigation

sealed class Destination(val route: String) {
    object InitialScreen : Destination("initial")
    object LoginScreen : Destination("login")
    object FuncionalDataScreen : Destination("funcionalData/{email}") {
        fun createRoute(email: String) = "funcionalData/$email"
    }
    object HomeScreen : Destination("home/{email}") {
        fun createRoute(email: String) = "home/$email"
    }
    object StockScreen : Destination("stock")
    object AlertsScreen : Destination("alerts")
    object LogisticsScreen : Destination("logistics")
    object PrevAIScreen : Destination("prevIA")
}