package br.com.fiap.hospitalmanagement.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface
import br.com.fiap.hospitalmanagement.ui.theme.MediWarning

// --- Modelos e Dados ---

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
    FullAlert(3, "Vencimento — Luvas Descartáveis P", "Validade: 15/05 · 20 dias", "há 1h", AlertCategory.VALIDADE, "Ver"),
    FullAlert(4, "Atraso na entrega — Pedido #0041", "MedSupply · SLA +2h", "há 2h", AlertCategory.LOGISTICA, "Rastrear")
)

private data class AlertNavItem(val label: String, val icon: ImageVector, val route: String)


@Composable
fun AlertsScreen(navController: NavController) {
    val context = LocalContext.current
    val email = remember {
        context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
    }

    val tabs = listOf("Dashboard", "Estoque", "Previsão IA", "Logística", "Alertas", "Pedido")
    var selectedFilter by remember { mutableStateOf<AlertCategory?>(null) }

    val filteredAlerts = remember(selectedFilter) {
        if (selectedFilter == null) allFullAlerts
        else allFullAlerts.filter { it.category == selectedFilter }
    }

    Scaffold(
        containerColor = MediBackground,
        bottomBar = {
            AlertsBottomNavigation(navController, email)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            item {
                AlertsDashboard(
                    allAlerts = allFullAlerts,
                    filteredAlerts = filteredAlerts,
                    selectedFilter = selectedFilter,
                    onFilterToggle = { category ->
                        selectedFilter = if (selectedFilter == category) null else category
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Preview
@Composable
private fun AlertsScreenPreview() {
    HospitalManagementTheme() {
        AlertsScreen(rememberNavController())
    }
}
// --- Componentes Particionados ---

@Composable
private fun AlertsBottomNavigation(navController: NavController, email: String) {
    val navItems = listOf(
        AlertNavItem("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
        AlertNavItem("Estoque", Icons.Default.Inventory, Destination.StockScreen.route),
        AlertNavItem("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
        AlertNavItem("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
        AlertNavItem("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route)
    )

    NavigationBar(containerColor = MediSurface) {
        navItems.forEachIndexed { index, item ->
            val isSelected = index == 4
            NavigationBarItem(
                selected = isSelected,
                onClick = { if (!isSelected) navController.navigate(item.route) },
                icon = {
                    if (isSelected) {
                        BadgedBox(
                            badge = { Badge { Text("${allFullAlerts.size}") } }
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MediPrimary,
                    selectedTextColor = MediPrimary,
                    unselectedIconColor = MediSubtext,
                    unselectedTextColor = MediSubtext,
                    indicatorColor = MediPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}



@Composable
private fun AlertsDashboard(
    allAlerts: List<FullAlert>,
    filteredAlerts: List<FullAlert>,
    selectedFilter: AlertCategory?,
    onFilterToggle: (AlertCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MediCardBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header do Card
        AlertsCardHeader(count = allAlerts.size)

        // Contadores
        AlertsCounters(allAlerts = allAlerts)

        // Chips de Filtro
        AlertsFilterRow(selectedFilter = selectedFilter, onFilterToggle = onFilterToggle)

        // Lista de Alertas
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            filteredAlerts.forEach { alert ->
                FullAlertRow(alert = alert)
            }
        }
    }
}

@Composable
private fun AlertsCardHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Alertas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MediError),
            contentAlignment = Alignment.Center
        ) {
            Text("$count", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AlertsCounters(allAlerts: List<FullAlert>) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        Column {
            Text("Críticos", fontSize = 12.sp, color = MediSubtext)
            Text(
                "${allAlerts.count { it.category == AlertCategory.ESTOQUE }}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MediError
            )
        }
        Column {
            Text("Atenção", fontSize = 12.sp, color = MediSubtext)
            Text(
                "${allAlerts.count { it.category != AlertCategory.ESTOQUE }}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MediWarning
            )
        }
    }
}

@Composable
private fun AlertsFilterRow(
    selectedFilter: AlertCategory?,
    onFilterToggle: (AlertCategory) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AlertFilterChip(
            label = "Estoque",
            color = MediError,
            selected = selectedFilter == AlertCategory.ESTOQUE,
            onClick = { onFilterToggle(AlertCategory.ESTOQUE) }
        )
        AlertFilterChip(
            label = "Validade",
            color = MediSuccess,
            selected = selectedFilter == AlertCategory.VALIDADE,
            onClick = { onFilterToggle(AlertCategory.VALIDADE) }
        )
        AlertFilterChip(
            label = "Logística",
            color = MediBlue,
            selected = selectedFilter == AlertCategory.LOGISTICA,
            onClick = { onFilterToggle(AlertCategory.LOGISTICA) }
        )
    }
}

@Composable
private fun AlertFilterChip(label: String, color: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) color else color.copy(alpha = 0.2f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) Color.White else color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FullAlertRow(alert: FullAlert) {
    val alertColor = when (alert.category) {
        AlertCategory.ESTOQUE -> MediError
        AlertCategory.VALIDADE -> MediWarning
        AlertCategory.LOGISTICA -> MediBlue
    }
    val actionColor = when (alert.actionLabel) {
        "Repor" -> MediWarning
        "Ver" -> MediSuccess
        "Rastrear" -> MediBlue
        else -> MediPrimary
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(alertColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "i", color = alertColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = alert.title, fontSize = 13.sp, color = alertColor, fontWeight = FontWeight.Medium)
            Text(text = alert.detail, fontSize = 11.sp, color = MediSubtext)
            Text(text = alert.timeAgo, fontSize = 11.sp, color = MediSubtext)
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = actionColor),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(text = alert.actionLabel, fontSize = 12.sp, color = Color.White)
        }
    }
}