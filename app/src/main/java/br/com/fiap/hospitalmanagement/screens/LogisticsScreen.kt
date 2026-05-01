package br.com.fiap.hospitalmanagement.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.model.Delivery
import br.com.fiap.hospitalmanagement.repository.DeliveryRepository
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess
import br.com.fiap.hospitalmanagement.ui.theme.MediSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogisticsScreen(navController: NavController) {
    val deliveryRepository = DeliveryRepository()
    val logisticsDeliveries = deliveryRepository.getAllDeliveries()

    Scaffold (
        containerColor = MediBackground,
        topBar = {
            TopAppBar(
                title = { Text("Logística", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton (onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MediSurface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(logisticsDeliveries) { delivery ->
                LogisticsCard(delivery)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Preview
@Composable
private fun LogisticsScreenPreview() {
    HospitalManagementTheme {
        LogisticsScreen(rememberNavController())
    }
}

@Composable
private fun LogisticsCard(delivery: Delivery) {
    val statusColor = when (delivery.status) {
        "Em rota" -> MediBlue
        "Atrasado" -> MediError
        "Entregue" -> MediSuccess
        "Aprovado" -> MediSuccess
        else -> MediSubtext
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MediCardBg)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${delivery.id} — ${delivery.supplier}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = delivery.status, fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Bold)
            }
        }
        Text(text = delivery.eta, fontSize = 12.sp, color = MediSubtext)
    }
}
