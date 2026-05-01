package br.com.fiap.hospitalmanagement.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.R
import br.com.fiap.hospitalmanagement.model.MedItem
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.repository.MedItemRepository
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface
import br.com.fiap.hospitalmanagement.ui.theme.MediWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(navController: NavController, email: String = "") {
    val context = LocalContext.current
    val repository = remember { MedItemRepository(context) }

    var searchQuery by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(emptyList<MedItem>()) }
    var selectedFilter by remember { mutableStateOf("Todos") }

    LaunchedEffect(Unit) {
        try {
            repository.seedInitialData()
            items = repository.getAllItems()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val filteredItems = remember(searchQuery, items, selectedFilter) {
        items.filter { item ->
            val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Crítico" -> item.isLowStock
                "Vencendo" -> item.expirationDate != null
                "Normal" -> !item.isLowStock
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MediBackground,
        bottomBar = { MedBottomBar(navController, email) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            StockHeader(onAddClick = { /* Fazer tela de pedido */ })

            StockSummary(
                totalCount = items.size,
                criticalCount = items.count { it.isLowStock }
            )

            Spacer(modifier = Modifier.height(20.dp))
            StockSearchBar(searchQuery) { searchQuery = it }
            Spacer(modifier = Modifier.height(16.dp))
            StockFilters(selectedFilter) { selectedFilter = it }
            Spacer(modifier = Modifier.height(20.dp))
            
            StockDataTable(
                filteredItems = filteredItems,
                totalCount = items.size,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StockHeader(onAddClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = MediCardBg,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.stock),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(MediIconBg)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = MediPrimary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun StockHeaderPreview() {
    HospitalManagementTheme() {
        StockHeader {  }
    }
}
@Composable
fun StockSummary(totalCount: Int, criticalCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(R.string.total_items),
                color = MediSubtext,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$totalCount",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = stringResource(R.string.critics),
                color = MediSubtext,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$criticalCount",
                color = MediError,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 1.dp)
            ) {
                Text(
                    text = stringResource(R.string.attention),
                    color = MediError, 
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
private fun StockSummaryPreview() {
    HospitalManagementTheme() {
        StockSummary(totalCount = 84, criticalCount = 8)
    }
}

@Composable
fun StockSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(
            stringResource(R.string.search_input),
            color = MediSubtext,
            fontSize = 14.sp
        ) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MediSubtext.copy(alpha = 0.5f),
            unfocusedBorderColor = MediSubtext.copy(alpha = 0.3f),
            focusedContainerColor = MediCardBg,
            unfocusedContainerColor = MediCardBg,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Preview
@Composable
private fun StockSearchBarPreview() {
    HospitalManagementTheme() {
        StockSearchBar(query = String()) { }
    }
}
@Composable
private fun StockFilters(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Crítico", "Vencendo", "Normal", "Todos")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilter == filter

            val (bgColor, contentColor) = when (filter) {
                "Crítico" -> Pair(MediError.copy(alpha = 0.15f), MediError)
                "Vencendo" -> Pair(MediWarning.copy(alpha = 0.15f), MediWarning)
                "Normal" -> Pair(MediSuccess.copy(alpha = 0.15f), MediSuccess)
                else -> Pair(Color.Transparent, MediSubtext)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) bgColor else Color.Transparent)
                    .clickable { onFilterSelected(filter) }
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else Color.Transparent,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = filter,
                    color = if (isSelected) contentColor else MediSubtext,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
private fun StockFiltersPreview() {
    HospitalManagementTheme() {
        StockFilters(selectedFilter = "") { }
    }
}

@Composable
private fun StockDataTable(
    filteredItems: List<MedItem>,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MediCardBg,
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.input), color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(2f))
                Text(text = stringResource(R.string.qtd), color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = stringResource(R.string.status), color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.1f))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    val statusColor = when (item.stockStatus) {
                        "Esgotado", "Crítico" -> MediError
                        "Baixo" -> MediWarning
                        else -> MediSuccess
                    }

                    val displayStatus = when (item.stockStatus) {
                        "Esgotado", "Crítico" -> "Crítico"
                        "Baixo" -> "Venc."
                        else -> "Normal"
                    }

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(2f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${item.quantity}",
                                color = if (item.isLowStock) MediError else Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentWidth(Alignment.End)
                                    .clip(CircleShape)
                                    .background(statusColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = displayStatus,
                                    color = statusColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.05f))
                    }
                }
            }

            Text(
                text = "${filteredItems.size} de $totalCount itens",
                color = MediSubtext,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun MedBottomBar(navController: NavController, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediSurface)
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem("Home", Icons.Default.Home, false) {
            navController.navigate(Destination.HomeScreen.createRoute(email))
        }
        BottomNavItem("Estoque", Icons.Default.Inventory, true) {
            // Já está na tela de estoque
        }
        BottomNavItem("IA", Icons.Default.Psychology, false) {
            navController.navigate(Destination.PrevAIScreen.route)
        }
        BottomNavItem("Logística", Icons.Default.LocalShipping, false) {
            navController.navigate(Destination.LogisticsScreen.route)
        }
        BottomNavItem("Alertas", Icons.Default.Notifications, false) {
            navController.navigate(Destination.AlertsScreen.route)
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String, 
    icon: ImageVector, 
    isSelected: Boolean, 
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) MediPrimary.copy(alpha = 0.2f) else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MediPrimary else MediSubtext,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = if (isSelected) MediPrimary else MediSubtext,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Preview
@Composable
private fun StockScreenPreview() {
    HospitalManagementTheme {
        StockScreen(rememberNavController())
    }
}