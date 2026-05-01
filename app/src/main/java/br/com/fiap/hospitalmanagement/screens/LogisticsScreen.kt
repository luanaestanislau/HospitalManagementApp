package br.com.fiap.hospitalmanagement.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.R
import br.com.fiap.hospitalmanagement.model.Delivery
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.repository.DeliveryRepository
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface

@Composable
fun LogisticsScreen(navController: NavController) {
    val context = LocalContext.current
    val deliveryRepository = remember { DeliveryRepository() }
    val email = remember {
        context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
    }

    val allDeliveries = remember { deliveryRepository.getAllDeliveries() }
    val ongoingCount = allDeliveries.count { it.status == "Em rota" }
    val delayedCount = allDeliveries.count { it.status == "Atrasado" }

    Scaffold(
        containerColor = MediBackground,
        bottomBar = { LogisticsBottomNavigation(navController, email) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { LogisticsHeaderCard(ongoingCount, delayedCount) }

            item { LogisticsMapCard() }

            item {
                LogisticsListCard(allDeliveries)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun LogisticsHeaderCard(ongoing: Int, delayed: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.logistics),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(stringResource(R.string.route), color = MediSubtext, fontSize = 12.sp)
                Text("$ongoing", color = MediBlue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text(stringResource(R.string.delays), color = MediSubtext, fontSize = 12.sp)
                Text("$delayed", color = MediError, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LogisticsMapCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        color = MediCardBg,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridStep = 40f
                for (x in 0..(size.width / gridStep).toInt()) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(x * gridStep, 0f),
                        end = Offset(x * gridStep, size.height),
                        strokeWidth = 1f
                    )
                }
                for (y in 0..(size.height / gridStep).toInt()) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(0f, y * gridStep),
                        end = Offset(size.width, y * gridStep),
                        strokeWidth = 1f
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                MapDot(Modifier
                    .align(Alignment.Center)
                    .padding(end = 60.dp, bottom = 20.dp), MediPrimary)
                MapDot(Modifier
                    .align(Alignment.Center)
                    .padding(top = 40.dp, start = 10.dp), MediBlue)
                MapDot(Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 80.dp), MediError)
            }

            Text(
                stringResource(R.string.real_time_map),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MapDot(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .size(10.dp)
            .border(1.5.dp, Color.White, CircleShape)
            .padding(2.dp)
            .background(color, CircleShape)
    )
}

@Composable
private fun LogisticsListCard(deliveries: List<Delivery>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MediCardBg,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.order_status),
                color = MediSubtext,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            deliveries.forEachIndexed { index, delivery ->
                DeliveryItemRow(delivery)
                if (index < deliveries.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = Color.White.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DeliveryItemRow(delivery: Delivery) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${delivery.id} — ${delivery.supplier}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            val subText = if (delivery.isDelayed) stringResource(R.string.excedid_sla) else delivery.eta.take(9)
            Text(
                text = subText,
                color = if (delivery.isDelayed) MediError else MediSubtext,
                fontSize = 11.sp
            )
        }

        val (statusLabel, statusColor) = when (delivery.status) {
            "Em rota" -> "Em rota" to MediBlue
            "Atrasado" -> "Atrasado" to MediError
            "Entregue" -> "Entregue" to MediSuccess
            else -> delivery.status to MediSubtext
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(statusColor.copy(alpha = 0.15f))
                .border(0.5.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = statusLabel,
                color = if (statusLabel == "Entregue") MediSuccess else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LogisticsBottomNavigation(navController: NavController, email: String) {
    NavigationBar(containerColor = MediSurface) {
        val items = listOf(
            Triple("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
            Triple("Estoque", Icons.Default.Inventory, Destination.StockScreen.createRoute(email)),
            Triple("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
            Triple("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
            Triple("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route),
        )

        items.forEachIndexed { index, item ->
            val isSelected = index == 3
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
private fun LogisticsScreenPreview() {
    HospitalManagementTheme { LogisticsScreen(rememberNavController()) }
}
