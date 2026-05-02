package br.com.fiap.hospitalmanagement.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.R
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.repository.DeliveryRepository
import br.com.fiap.hospitalmanagement.repository.MedItemRepository
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface
import br.com.fiap.hospitalmanagement.ui.theme.MediWarning

private enum class AlertCategory { ESTOQUE, VALIDADE, LOGISTICA }

private data class FullAlert(
    val id: Int,
    val title: String,
    val detail: String,
    val timeAgo: String,
    val category: AlertCategory,
    val actionLabel: String
)

private val allFullAlerts = listOf(
    FullAlert(1, "Estoque crítico — Soro Fisiológico 500ml", "12 un. atual · Mínimo: 50 un.", "há 10 min", AlertCategory.ESTOQUE, "Repor"),
    FullAlert(2, "Estoque crítico — Gaze Esterilizada", "8 un. atual · Mínimo: 30 un.", "há 25 min", AlertCategory.ESTOQUE, "Repor"),
    FullAlert(3, "Vencimento — Luvas Descartáveis P", "Validade: 15/05 · 20 dias", "há 1 h", AlertCategory.VALIDADE, "Ver"),
    FullAlert(4, "Atraso na entrega — Pedido #0041", "MedSupply · SLA +2h", "há 2 h", AlertCategory.LOGISTICA, "Rastrear")
)

@Composable
fun AlertsScreen(navController: NavController) {
    val context = LocalContext.current
    val medItemRepository = remember { MedItemRepository(context) }
    val deliveryRepository = remember { DeliveryRepository() }

    val email = remember {
        context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
    }

    var selectedFilter by remember { mutableStateOf<AlertCategory?>(null) }
    val allAlerts = remember { mutableStateListOf<FullAlert>() }

    LaunchedEffect(Unit) {
        val lowStock = medItemRepository.getLowStockItems()
        val allItems = medItemRepository.getAllItems()
        val deliveries = deliveryRepository.getOngoingDeliveries()

        val alertsList = mutableListOf<FullAlert>()

        lowStock.forEachIndexed { index, item ->
            alertsList.add(
                FullAlert(
                    id = index,
                    title = "Estoque crítico — ${item.name}",
                    detail = "${item.quantity} ${item.unit} atual · Mínimo: ${item.minimumQuantity} ${item.unit}",
                    timeAgo = "há ${10 + index * 5} min",
                    category = AlertCategory.ESTOQUE,
                    actionLabel = "Repor"
                )
            )
        }

        allItems.filter { it.expirationDate != null }.take(1).forEach { item ->
            alertsList.add(
                FullAlert(
                    id = alertsList.size,
                    title = "Vencimento — ${item.name}",
                    detail = "Validade: ${item.expirationDate} · 20 dias",
                    timeAgo = "há 1 h",
                    category = AlertCategory.VALIDADE,
                    actionLabel = "Ver"
                )
            )
        }

        deliveries.take(1).forEach { delivery ->
            alertsList.add(
                FullAlert(
                    id = alertsList.size,
                    title = if (delivery.isDelayed) "Atraso na entrega — Pedido ${delivery.id}" else "Entrega em curso — Pedido ${delivery.id}",
                    detail = "${delivery.supplier} · ${delivery.eta}",
                    timeAgo = "há 2 h",
                    category = AlertCategory.LOGISTICA,
                    actionLabel = "Rastrear"
                )
            )
        }

        allAlerts.clear()
        allAlerts.addAll(alertsList)
    }

    val filteredAlerts = remember(selectedFilter, allAlerts.toList()) {
        if (selectedFilter == null) allAlerts
        else allAlerts.filter { it.category == selectedFilter }
    }

    Scaffold(
        containerColor = MediBackground,
        bottomBar = { AlertsBottomNavigation(navController, email) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { AlertsHeaderCard(count = allAlerts.size) }

            item {
                AlertsSummarySection(
                    criticos = allAlerts.count { it.category == AlertCategory.ESTOQUE },
                    atencao = allAlerts.count { it.category != AlertCategory.ESTOQUE }
                )
            }
            item {
                AlertsFilterSection(
                    selectedFilter = selectedFilter,
                    onFilterSelect = { selectedFilter = if (selectedFilter == it) null else it }
                )
            }
            item { AlertsListCard(alerts = filteredAlerts, navController = navController, email = email) }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun AlertsHeaderCard(count: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MediCardBg,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.alerts), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(1.dp, MediError.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("$count", color = MediError, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AlertsSummarySection(criticos: Int, atencao: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(140.dp)
    ) {
        Column {
            Text(stringResource(R.string.critics), color = MediSubtext, fontSize = 12.sp)
            Text("$criticos", color = MediError, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(stringResource(R.string.attention), color = MediSubtext, fontSize = 12.sp)
            Text("$atencao", color = MediWarning, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AlertsFilterSection(selectedFilter: AlertCategory?, onFilterSelect: (AlertCategory) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AlertFilterChip("Estoque", MediError, selectedFilter == AlertCategory.ESTOQUE) { onFilterSelect(AlertCategory.ESTOQUE) }
        AlertFilterChip("Validade", MediWarning, selectedFilter == AlertCategory.VALIDADE) { onFilterSelect(AlertCategory.VALIDADE) }
        AlertFilterChip("Logística", MediBlue, selectedFilter == AlertCategory.LOGISTICA) { onFilterSelect(AlertCategory.LOGISTICA) }
    }
}

@Composable
private fun AlertFilterChip(label: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) color else color.copy(alpha = 0.15f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = if (isSelected) Color.White else color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AlertsListCard(alerts: List<FullAlert>, navController: NavController, email: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MediCardBg,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            alerts.forEachIndexed { index, alert ->
                AlertItemRow(alert, navController, email)
                if (index < alerts.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.White.copy(alpha = 0.05f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertItemRow(alert: FullAlert, navController: NavController, email: String) {
    val (iconColor, iconText) = when (alert.category) {
        AlertCategory.ESTOQUE -> MediError to "!"
        AlertCategory.VALIDADE -> MediWarning to "!"
        AlertCategory.LOGISTICA -> MediBlue to "i"
    }

    val buttonColor = when (alert.actionLabel) {
        "Repor" -> Color(0xFF7B61FF)
        else -> Color(0xFF2C2C2C)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(iconText, color = iconColor, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(alert.title, color = iconColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(alert.detail, color = Color.White, fontSize = 12.sp)
            Text(alert.timeAgo, color = MediSubtext, fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(buttonColor)
                .then(
                    if (alert.actionLabel != "Repor") Modifier.border(
                        0.5.dp,
                        Color.White.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    ) else Modifier
                )
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clickable {
                    when (alert.actionLabel) {
                        "Repor" -> navController.navigate(Destination.PrevAIScreen.route)
                        "Ver" -> navController.navigate(Destination.StockScreen.createRoute(email))
                        "Rastrear" -> navController.navigate(Destination.LogisticsScreen.route)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(alert.actionLabel, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AlertsBottomNavigation(navController: NavController, email: String) {
    NavigationBar(containerColor = MediSurface) {
        val items = listOf(
            Triple("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
            Triple("Estoque", Icons.Default.Inventory, Destination.StockScreen.createRoute(email)),
            Triple("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
            Triple("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
            Triple("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route)
        )

        items.forEachIndexed { index, item ->
            val isSelected = index == 4
            NavigationBarItem(
                selected = isSelected,
                onClick = { if (!isSelected) navController.navigate(item.third) },
                icon = { Icon(item.second, contentDescription = item.first, modifier = Modifier.size(24.dp)) },
                label = { Text(item.first, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MediPrimary,
                    selectedTextColor = MediPrimary,
                    unselectedIconColor = MediSubtext,
                    unselectedTextColor = MediSubtext,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun AlertsScreenPreview() {
    HospitalManagementTheme { AlertsScreen(rememberNavController()) }
}