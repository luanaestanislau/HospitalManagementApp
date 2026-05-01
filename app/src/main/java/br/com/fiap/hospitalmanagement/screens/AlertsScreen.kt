package br.com.fiap.hospitalmanagement.screens

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

    val navItems = listOf(
        AlertNavItem("Home", Icons.Default.Home, Destination.DashboardScreen.createRoute(email)),
        AlertNavItem("Estoque", Icons.Default.Inventory, Destination.StockScreen.route),
        AlertNavItem("IA", Icons.Default.Psychology, Destination.PrevAIScreen.route),
        AlertNavItem("Logística", Icons.Default.LocalShipping, Destination.LogisticsScreen.route),
        AlertNavItem("Alertas", Icons.Default.Notifications, Destination.AlertsScreen.route)
    )

    val tabs = listOf("Dashboard", "Estoque", "Previsão IA", "Logística", "Alertas", "Pedido")

    var selectedFilter by remember { mutableStateOf<AlertCategory?>(null) }

    val filteredAlerts = remember(selectedFilter) {
        if (selectedFilter == null) allFullAlerts
        else allFullAlerts.filter { it.category == selectedFilter }
    }

    Scaffold(
        containerColor = MediBackground,
        bottomBar = {
            NavigationBar(containerColor = MediSurface) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == 4,
                        onClick = {
                            if (index != 4) {
                                navController.navigate(item.route)
                            }
                        },
                        icon = {
                            if (index == 4) {
                                androidx.compose.material3.BadgedBox(
                                    badge = { androidx.compose.material3.Badge { Text("${allFullAlerts.size}") } }
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = index == 4
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) MediPrimary else MediCardBg)
                                .clickable {
                                    when (index) {
                                        0 -> navController.navigate(Destination.DashboardScreen.createRoute(email))
                                        1 -> navController.navigate(Destination.StockScreen.route)
                                        2 -> navController.navigate(Destination.PrevAIScreen.route)
                                        3 -> navController.navigate(Destination.LogisticsScreen.route)
                                        5 -> navController.navigate(Destination.OrderScreen.route)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else MediSubtext,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
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
                        .clip(RoundedCornerShape(16.dp))
                        .background(MediCardBg)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
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
                            Text("${allFullAlerts.size}", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Counters
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Column {
                            Text("Críticos", fontSize = 12.sp, color = MediSubtext)
                            Text(
                                "${allFullAlerts.count { it.category == AlertCategory.ESTOQUE }}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MediError
                            )
                        }
                        Column {
                            Text("Atenção", fontSize = 12.sp, color = MediSubtext)
                            Text(
                                "${allFullAlerts.count { it.category == AlertCategory.VALIDADE || it.category == AlertCategory.LOGISTICA }}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MediWarning
                            )
                        }
                    }

                    // Filter chips
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AlertFilterChip(
                            label = "Estoque",
                            color = MediError,
                            selected = selectedFilter == AlertCategory.ESTOQUE
                        ) {
                            selectedFilter = if (selectedFilter == AlertCategory.ESTOQUE) null else AlertCategory.ESTOQUE
                        }
                        AlertFilterChip(
                            label = "Validade",
                            color = MediSuccess,
                            selected = selectedFilter == AlertCategory.VALIDADE
                        ) {
                            selectedFilter = if (selectedFilter == AlertCategory.VALIDADE) null else AlertCategory.VALIDADE
                        }
                        AlertFilterChip(
                            label = "Logística",
                            color = MediBlue,
                            selected = selectedFilter == AlertCategory.LOGISTICA
                        ) {
                            selectedFilter = if (selectedFilter == AlertCategory.LOGISTICA) null else AlertCategory.LOGISTICA
                        }
                    }

                    // Alert rows
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredAlerts.forEach { alert ->
                            FullAlertRow(alert = alert)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
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
            Text(
                text = "i",
                color = alertColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
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