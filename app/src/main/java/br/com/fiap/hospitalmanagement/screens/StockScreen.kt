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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.model.MedItem
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
fun StockScreen(navController: NavController) {
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
        containerColor = MediBackground,
        bottomBar = { MediBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            StockHeader(onAddClick = { /* Implementar navegação para tela de adição */ })
            
            StockSummary(
                totalCount = items.size,
                criticalCount = items.count { it.isLowStock }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            StockSearchBar(searchQuery) { searchQuery = it }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            StockFilters(selectedFilter) { selectedFilter = it }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            StockListHeader()
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    StockItemRow(item)
                }
            }
            
            StockFooter(filteredItems.size, items.size)
        }
    }
}

// Partes separadas conforme solicitado

@Composable
private fun StockHeader(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Estoque",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(32.dp)
                .background(MediIconBg, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun StockSummary(totalCount: Int, criticalCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Itens totais", color = MediSubtext, fontSize = 14.sp)
            Text(
                text = "$totalCount",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "Críticos", color = MediSubtext, fontSize = 14.sp)
            Text(
                text = "$criticalCount",
                color = MediError,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MediError.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Atenção",
                    color = MediError,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StockSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text("Buscar insumo...", color = MediSubtext, fontSize = 14.sp) },
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

@Composable
private fun StockFilters(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("Crítico", "Vencendo", "Normal", "Todos")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilter == filter
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) MediPrimary else MediCardBg)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = filter,
                    color = if (isSelected) Color.White else MediSubtext,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun StockListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Insumo", color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(2f))
        Text(text = "Qtd", color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = "Status", color = MediSubtext, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}

@Composable
private fun StockItemRow(item: MedItem) {
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MediCardBg)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            color = Color.White,
            fontSize = 14.sp,
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
                .clip(RoundedCornerShape(12.dp))
                .background(statusColor.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 4.dp),
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
}

@Composable
private fun StockFooter(currentCount: Int, totalCount: Int) {
    Text(
        text = "$currentCount de $totalCount itens",
        color = MediSubtext,
        fontSize = 12.sp,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
private fun MediBottomBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MediSurface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem("Home", Icons.Default.GridView, false)
        BottomNavItem("Estoque", Icons.Default.Inventory, true)
        BottomNavItem("IA", Icons.Default.Timeline, false)
        BottomNavItem("Logística", Icons.Default.Schedule, false)
        BottomNavItem("Alertas", Icons.Default.Notifications, false)
    }
}

@Composable
private fun BottomNavItem(label: String, icon: ImageVector, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
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
