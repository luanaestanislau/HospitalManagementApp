package br.com.fiap.hospitalmanagement.repository

import br.com.fiap.hospitalmanagement.model.Delivery

class DeliveryRepository {
    private val deliveries = listOf(
        Delivery("#0039", "ForneceMed", "ETA: 14:30", "Em rota", false),
        Delivery("#0041", "MedSupply", "SLA excedido", "Atrasado", true),
        Delivery("#0038", "HospPro", "ETA: 16:00", "Em rota", false),
        Delivery("#0036", "ForneceMed", "11:45", "Entregue", false),
        Delivery("#0042", "PharmaBR", "ETA: 18:00", "Em rota", false)
    )

    fun getAllDeliveries(): List<Delivery> = deliveries

    fun getOngoingDeliveries(): List<Delivery> = deliveries.filter { 
        it.status != "Entregue" 
    }

    fun getTodayDeliveriesCount(): Int = deliveries.count { 
        it.status == "Atrasado" || it.status == "Em rota"
    }
}
