package br.com.fiap.hospitalmanagement.screens

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface

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

@Composable
fun PrevAIScreen(navController: NavController) {
    val context = LocalContext.current
    val email = remember {
        context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
    }

    var selectedProduct by remember { mutableStateOf(productOptions[0]) }

    Scaffold(
        containerColor = MediBackground,
        bottomBar = { IaBottomNavigation(navController, email) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { IaHeaderCard() }

            item { IaProductSelector(selectedProduct) { selectedProduct = it } }

            item { IaForecastChartCard() }

            item { IaRecommendationsCard() }

            item { IaFactorsCard() }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun IaHeaderCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp, horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.prev_ai),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun IaProductSelector(selectedProduct: String, onProductSelected: (String) -> Unit) {
    var showDropdown by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MediCardBg)
                .clickable { showDropdown = !showDropdown }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedProduct, fontSize = 14.sp, color = Color.White)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = MediSubtext)
        }
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.background(MediCardBg)
        ) {
            productOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White, fontSize = 14.sp) },
                    onClick = {
                        onProductSelected(option)
                        showDropdown = false
                    }
                )
            }
        }
    }
}

@Composable
private fun IaForecastChartCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MediCardBg)
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(R.string.prev_vs_history),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        val maxHeight = 100.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight + 30.dp),
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
                                    .width(8.dp)
                                    .height(maxHeight * data.historicPercent)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(MediBlue)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .height(maxHeight * data.forecastPercent)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(MediPrimary.copy(alpha = if (data.isFuture) 1f else 0.4f))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.month,
                        fontSize = 10.sp,
                        color = if (data.isFuture) MediPrimary else MediSubtext
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            IaLegendItem(color = MediBlue, label = "Histórico")
            IaLegendItem(color = MediPrimary, label = "Previsão IA")
        }
    }
}

@Composable
private fun IaRecommendationsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MediCardBg)
            .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            stringResource(R.string.recommendation_ai),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MediSurface)
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.text_example),
                fontSize = 13.sp,
                color = MediSubtext,
                lineHeight = 18.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MediSurface)
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.text_example2),
                fontSize = 13.sp,
                color = MediSubtext,
                lineHeight = 18.sp
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {},
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MediPrimary)
            ) {
                Text(stringResource(R.string.create_order), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(stringResource(R.string.historic), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun IaFactorsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MediCardBg,
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                stringResource(R.string.factors_considered),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MediSubtext
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IaFactorChip("Histórico", Color(0xFFE8EAF6), Color(0xFF3F51B5))
                IaFactorChip("Sazonalidade", Color(0xFFE8EAF6), Color(0xFF3F51B5))
                IaFactorChip("Pandemias", Color(0xFFFFEBEE), Color(0xFFD32F2F))
                IaFactorChip("SLA", Color(0xFFE8F5E9), Color(0xFF388E3C))
            }
        }
    }
}

@Composable
private fun IaBottomNavigation(navController: NavController, email: String) {
    NavigationBar(containerColor = MediSurface) {
        val items = listOf(
            Triple("Home", Icons.Default.Home, Destination.HomeScreen.createRoute(email)),
            Triple("Estoque", Icons.Default.Inventory, Destination.StockScreen.createRoute(email)),
            Triple("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
            Triple("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
            Triple("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route),
        )

        items.forEachIndexed { index, item ->
            val isSelected = index == 2
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
private fun IaFactorChip(label: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}