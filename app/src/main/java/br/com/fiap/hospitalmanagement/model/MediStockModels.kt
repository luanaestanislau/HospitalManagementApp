package br.com.fiap.hospitalmanagement.model

enum class AlertType { LOW_STOCK, EXPIRING, DELAY, INFO }

data class StockAlert(
    val id: Int,
    val title: String,
    val timeAgo: String,
    val type: AlertType,
    val isRead: Boolean = false
)

data class Delivery(
    val id: String,
    val supplier: String,
    val eta: String,
    val status: String,
    val isDelayed: Boolean = false
)

data class DemandForecast(
    val dayLabel: String,
    val demandPercent: Float,
    val isHighDemand: Boolean = false
)

data class FunctionalData(
    val matricula: String,
    val department: String,
    val role: String,
    val professionalRegistry: String
)