package br.com.fiap.hospitalmanagement.screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { MedItemRepository(context) }

    // Seed initial data if empty
    remember { repository.seedInitialData(); true }

    var searchQuery by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(repository.getAllItems()) }

    val filteredItems = remember(searchQuery, items) {
        if (searchQuery.isBlank()) items
        else items.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = MediBackground,
        topBar = {
            TopAppBar(
                title = { Text("Estoque", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MediSurface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Busca
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Buscar insumo ou categoria...", color = MediSubtext) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MediSubtext) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MediPrimary,
                    unfocusedBorderColor = MediSubtext,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = MediCardBg,
                    unfocusedContainerColor = MediCardBg
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Resumo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryChip("Total: ${items.size}", MediPrimary, Modifier.weight(1f))
                SummaryChip(
                    "Críticos: ${items.count { it.isLowStock }}",
                    MediError,
                    Modifier.weight(1f)
                )
                SummaryChip(
                    "Normais: ${items.count { !it.isLowStock }}",
                    MediSuccess,
                    Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum insumo encontrado.", color = MediSubtext, fontSize = 14.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filteredItems, key = { it.id }) { item ->
                        MedItemCard(
                            item = item,
                            onToggleFavorite = {
                                repository.setFavorite(item.id, !item.isFavorite)
                                items = repository.getAllItems()
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SummaryChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 12.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MedItemCard(item: MedItem, onToggleFavorite: () -> Unit) {
    val statusColor = when (item.stockStatus) {
        "Esgotado" -> MediError
        "Crítico" -> MediError
        "Baixo" -> MediWarning
        else -> MediSuccess
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MediCardBg)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone de quantidade
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(statusColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${item.quantity}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }

        // Informações
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = item.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MediPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = item.category, fontSize = 10.sp, color = MediPrimary)
                }
            }
            Text(text = "${item.quantity} ${item.unit} · Mín: ${item.minimumQuantity}", fontSize = 11.sp, color = MediSubtext)
            if (item.location.isNotBlank()) {
                Text(text = item.location, fontSize = 11.sp, color = MediSubtext)
            }
        }

        // Status e favorito
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = item.stockStatus, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.Bold)
            }
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritar",
                    tint = if (item.isFavorite) MediError else MediSubtext,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun StockScreenPreview() {
    AppFiapGETheme() {
        StockScreen(rememberNavController())
    }
}
