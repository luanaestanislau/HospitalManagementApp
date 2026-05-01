package br.com.fiap.hospitalmanagement.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import br.com.fiap.hospitalmanagement.model.AlertType
import br.com.fiap.hospitalmanagement.model.Delivery
import br.com.fiap.hospitalmanagement.model.DemandForecast
import br.com.fiap.hospitalmanagement.model.StockAlert
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.repository.DeliveryRepository
import br.com.fiap.hospitalmanagement.repository.MedItemRepository
import br.com.fiap.hospitalmanagement.repository.UserRepository
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface
import br.com.fiap.hospitalmanagement.ui.theme.MediWarning

private data class NavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun HomeScreen(navController: NavController, email: String) {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val medItemRepository = remember { MedItemRepository(context) }
    val deliveryRepository = remember { DeliveryRepository() }

    var userName by remember { mutableStateOf("") }
    var lowStockCount by remember { mutableIntStateOf(0) }
    var todayDeliveriesCount by remember { mutableIntStateOf(0) }
    var ongoingDeliveries by remember { mutableStateOf(emptyList<Delivery>()) }

    LaunchedEffect(email) {
        val user = userRepository.getUserByEmail(email)
        userName = user?.name ?: ""
        lowStockCount = medItemRepository.getLowStockCount()
        todayDeliveriesCount = deliveryRepository.getTodayDeliveriesCount()
        ongoingDeliveries = deliveryRepository.getOngoingDeliveries()
    }

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
        NavItem("Estoque", Icons.Default.Inventory, Destination.StockScreen.createRoute(email)),
        NavItem("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
        NavItem("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
        NavItem("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route)
    )
    var selectedNav by remember { mutableIntStateOf(0) }

    val mockAlerts = listOf(
        StockAlert(1, "Insulina Glargina: Estoque Crítico", "12 min atrás", AlertType.LOW_STOCK),
        StockAlert(2, "Lote #882: Vencimento em 5 dias", "45 min atrás", AlertType.EXPIRING),
        StockAlert(3, "Pedido #392: Atraso na entrega", "2h atrás", AlertType.DELAY)
    )

    val mockForecast = listOf(
        DemandForecast("Seg", 0.4f),
        DemandForecast("Ter", 0.6f),
        DemandForecast("Qua", 0.9f, true),
        DemandForecast("Qui", 0.7f),
        DemandForecast("Sex", 0.8f),
        DemandForecast("Sab", 0.3f),
        DemandForecast("Dom", 0.2f)
    )

    Scaffold(
        containerColor = MediBackground,
        bottomBar = {
            HomeBottomBar(navItems, selectedNav, email) { index, route ->
                selectedNav = index
                if (route != Destination.HomeScreen.createRoute(email)) {
                    navController.navigate(route)
                }
            }
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
                HomeHeader(
                    userName = userName,
                    onProfileClick = { /* Fazer tela perfil */ }
                )
            }
            item {
                HomeStats(
                    lowStockCount = lowStockCount,
                    todayDeliveriesCount = todayDeliveriesCount
                )
            }
            item {
                HomeRecentAlerts(mockAlerts)
            }

            item {
                HomeDemandForecastSection(mockForecast)
            }

            item {
                HomeOngoingDeliveries(ongoingDeliveries)
            }

            item {
                HomeCreditsLink(onClick = { navController.navigate(Destination.CreditsScreen.route) })
            }
        }
    }
}

@Composable
fun HomeHeader(userName: String, onProfileClick: () -> Unit) {
    val initials = remember(userName) {
        if (userName.isBlank()) "US"
        else {
            val parts = userName.trim().split(" ")
            if (parts.size >= 2) {
                "${parts[0].firstOrNull() ?: ""}${parts[parts.size - 1].firstOrNull() ?: ""}".uppercase()
            } else {
                "${parts[0].firstOrNull() ?: ""}".uppercase()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MediCardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MediStock",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MediError.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("3", fontSize = 12.sp, color = MediError, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MediPrimary)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HomeStats(lowStockCount: Int, todayDeliveriesCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Espaçamento entre as duas colunas
    ) {
        // Coluna 1: Estoque Crítico
        StatColumn(
            title = "Estoque crítico",
            value = lowStockCount.toString(),
            valueColor = MediError,
            badgeText = "Crítico",
            modifier = Modifier.weight(1f)
        )

        // Coluna 2: Entregas Hoje
        StatColumn(
            title = "Entregas hoje",
            value = todayDeliveriesCount.toString(),
            valueColor = Color(0, 107, 176), // Um tom de azul (MediBlue)
            badgeText = "Em rota",
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start // Alinhado à esquerda como na foto
        )
    }
}

@Composable
private fun StatColumn(
    title: String,
    value: String,
    valueColor: Color,
    badgeText: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = horizontalAlignment
    ) {
        // Título Cinza Superior
        Text(
            text = title,
            fontSize = 13.sp,
            color = MediSubtext,
            fontWeight = FontWeight.Medium
        )

        // Número Grande e Colorido
        Text(
            text = value,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )

        // Badge (Pílula) branca com texto colorido
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = badgeText,
                fontSize = 11.sp,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HomeRecentAlerts(alerts: List<StockAlert>) {
    SectionCard(title = "Alertas recentes", modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            alerts.forEach { alert ->
                AlertRow(alert = alert)
            }
        }
    }
}

@Composable
fun HomeDemandForecastSection(forecast: List<DemandForecast>) {
    SectionCard(
        title = "Previsão de demanda — 7 dias",
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        DemandChart(forecast = forecast)
    }
}

@Composable
fun HomeOngoingDeliveries(deliveries: List<Delivery>) {
    SectionCard(
        title = "Entregas em andamento",
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            deliveries.forEach { delivery ->
                DeliveryRow(delivery = delivery)
            }
        }
    }
}

@Composable
fun HomeCreditsLink(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MediCardBg)
            .clickable { onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ℹ️  Sobre o MediStock / Créditos",
            fontSize = 13.sp,
            color = MediSubtext
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun HomeBottomBar(
    navItems: List<NavItem>,
    selectedNav: Int,
    email: String,
    onNavItemClick: (Int, String) -> Unit
) {
    NavigationBar(containerColor = MediSurface) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNav == index,
                onClick = { onNavItemClick(index, item.route) },
                icon = {
                    if (index == 4) {
                        BadgedBox(badge = { Badge { Text("3") } }) {
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
private fun StatCard(
    title: String,
    value: String,
    badgeText: String,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MediCardBg)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, fontSize = 12.sp, color = MediSubtext)
        Text(text = value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeColor.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(text = badgeText, fontSize = 11.sp, color = badgeColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SectionCard(title: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MediCardBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        content()
    }
}

@Composable
private fun AlertRow(alert: StockAlert) {
    val alertColor = when (alert.type) {
        AlertType.LOW_STOCK -> MediError
        AlertType.EXPIRING -> MediWarning
        AlertType.DELAY -> MediBlue
        AlertType.INFO -> MediSubtext
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(alertColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (alert.type) {
                        AlertType.LOW_STOCK -> "!"
                        AlertType.EXPIRING -> "!"
                        AlertType.DELAY -> "i"
                        AlertType.INFO -> "i"
                    },
                    color = alertColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Column {
                Text(text = alert.title, fontSize = 13.sp, color = alertColor)
                Text(text = alert.timeAgo, fontSize = 11.sp, color = MediSubtext)
            }
        }
    }
}

@Composable
private fun DemandChart(forecast: List<DemandForecast>) {
    val maxHeight = 80.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxHeight + 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        forecast.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .height(maxHeight * day.demandPercent)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (day.isHighDemand) MediError
                            else MediPrimary.copy(alpha = 0.6f + day.demandPercent * 0.4f)
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = day.dayLabel, fontSize = 10.sp, color = MediSubtext)
            }
        }
    }
}

@Composable
private fun DeliveryRow(delivery: Delivery) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${delivery.id} — ${delivery.supplier}",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Text(text = delivery.eta, fontSize = 11.sp, color = MediSubtext)
        }
        val statusColor = if (delivery.isDelayed) MediError else MediBlue
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(statusColor.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = delivery.status,
                fontSize = 11.sp,
                color = statusColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HospitalManagementTheme() {
        HomeScreen(rememberNavController(), email = "")
    }
}
