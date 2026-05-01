package br.com.fiap.hospitalmanagement.screens

private val logisticsDeliveries = listOf(
    Delivery("#0039", "ForneceMed", "ETA: 14:30 — hoje", "Em rota", false),
    Delivery("#0041", "MedSupply", "Previsto: 10:00 — atrasado 4h", "Atrasado", true),
    Delivery("#0042", "PharmaBR", "ETA: amanhã 09:00", "Aguardando", false),
    Delivery("#0043", "ChemLab", "ETA: amanhã 14:00", "Aprovado", false),
    Delivery("#0038", "MedTech", "Entregue ontem", "Entregue", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogisticsScreen(navController: NavController) {
    Scaffold(
        containerColor = MediBackground,
        topBar = {
            TopAppBar(
                title = { Text("Logística", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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

@Composable
private fun LogisticsCard(delivery: Delivery) {
    val statusColor = when (delivery.status) {
        "Em rota" -> MediBlue
        "Atrasado" -> MediError
        "Entregue" -> MediSuccess
        "Aprovado" -> MediSuccess
        else -> MediSubtext
    }
    Column(
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