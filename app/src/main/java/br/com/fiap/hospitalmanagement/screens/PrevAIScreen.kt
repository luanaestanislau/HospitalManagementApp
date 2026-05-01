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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
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

private data class MonthForecastData(
    val month: String,
    val historicPercent: Float,
    val forecastPercent: Float,
    val isFuture: Boolean = false
)

private val monthChartData = listOf(
    MonthForecastData("Abr", 0.60f, 0.65f),
    MonthForecastData("Mai", 0.70f, 0.72f),
    MonthForecastData("Jun", 0.65f, 0.68f),
    MonthForecastData("Jul", 0.80f, 0.78f),
    MonthForecastData("Ago", 0.75f, 0.80f),
    MonthForecastData("Set", 0.0f, 0.90f, true),
    MonthForecastData("Out", 0.0f, 0.85f, true),
    MonthForecastData("Nov", 0.0f, 0.75f, true)
)

private val productOptions = listOf(
    "Soro Fisiológico 500ml",
    "Luvas P (caixa)",
    "Seringa 10ml",
    "Máscara Cirúrgica",
    "Dipirona Sódica 500mg"
)

private data class IaNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun PrevAIScreen(navController: NavController) {
    val context = LocalContext.current
    val email = remember {
        context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
    }

    val navItems = listOf(
        IaNavItem("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
        IaNavItem("Estoque", Icons.Default.Inventory, Destination.StockScreen.route),
        IaNavItem("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
        IaNavItem("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
        IaNavItem("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route)
    )

    val tabs = listOf("Dashboard", "Estoque", "Previsão IA", "Logística", "Alertas", "Pedido")

    var selectedProduct by remember { mutableStateOf(productOptions[0]) }
    var showDropdown by remember { mutableStateOf(false) }

    Scaffold (
        containerColor = MediBackground,
        bottomBar = {
            NavigationBar(containerColor = MediSurface) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == 2,
                        onClick = {
                            if (index != 2) {
                                navController.navigate(item.route)
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Previsão IA",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MediCardBg)
                            .clickable { showDropdown = !showDropdown }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = selectedProduct, fontSize = 14.sp, color = Color.White)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = MediSubtext)
                    }
                    DropdownMenu (
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.background(MediCardBg)
                    ) {
                        productOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White, fontSize = 14.sp) },
                                onClick = {
                                    selectedProduct = option
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCardBg)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Histórico vs Previsão",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    val maxHeight = 80.dp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight + 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        monthChartData.forEach { data ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    if (!data.isFuture) {
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .height(maxHeight * data.historicPercent)
                                                .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                                .background(MediBlue)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(maxHeight * data.forecastPercent)
                                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                            .background(MediPrimary.copy(alpha = if (data.isFuture) 0.9f else 0.6f))
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = data.month,
                                    fontSize = 9.sp,
                                    color = if (data.isFuture) MediPrimary else MediSubtext
                                )
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IaLegendItem(color = MediBlue, label = "Histórico")
                        IaLegendItem(color = MediPrimary, label = "Previsão IA")
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCardBg)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Recomendações da IA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MediSurface)
                            .padding(12.dp)
                    ) {
                        Text(
                            "Repor 200 un. de Soro Fisiológico até 10/05. Alta prevista de +18% em jun/jul.",
                            fontSize = 13.sp,
                            color = MediSubtext
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MediSurface)
                            .padding(12.dp)
                    ) {
                        Text(
                            "Luvas P com vencimento próximo e consumo lento. Considere redistribuição.",
                            fontSize = 13.sp,
                            color = MediSubtext
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MediPrimary)
                        ) {
                            Text("Gerar pedido", fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("Histórico", fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MediCardBg)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Fatores considerados",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IaFactorChip("Histórico", MediBlue)
                        IaFactorChip("Sazonalidade", MediSuccess)
                        IaFactorChip("Pandemias", MediError)
                        IaFactorChip("SLA", MediWarning)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Preview
@Composable
private fun PrevAIScreenPreview() {
    HospitalManagementTheme {
        PrevAIScreen(rememberNavController())
    }
}

@Composable
private fun IaLegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(text = label, fontSize = 11.sp, color = MediSubtext)
    }
}

@Composable
private fun IaFactorChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium)
    }
}