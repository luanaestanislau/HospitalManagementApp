package br.com.fiap.hospitalmanagement.repository

import br.com.fiap.hospitalmanagement.model.Delivery

class DeliveryRepository {
    private val deliveries = listOf(
        Delivery("#0039", "ForneceMed", "ETA: 14:30 — hoje", "Em rota", false),
        Delivery("#0041", "MedSupply", "Previsto: 10:00 — atrasado 4h", "Atrasado", true),
        Delivery("#0042", "PharmaBR", "ETA: amanhã 09:00", "Aguardando", false),
        Delivery("#0043", "ChemLab", "ETA: amanhã 14:00", "Aprovado", false),
        Delivery("#0038", "MedTech", "Entregue ontem", "Entregue", false)
    )

    fun getAllDeliveries(): List<Delivery> = deliveries

    fun getOngoingDeliveries(): List<Delivery> = deliveries.filter { 
        it.status != "Entregue" 
    }

    fun getTodayDeliveriesCount(): Int = deliveries.count { 
        it.eta.contains("hoje") || it.status == "Atrasado" || it.status == "Em rota"
    }
}
