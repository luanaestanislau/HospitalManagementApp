package br.com.fiap.hospitalmanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_med_item")
data class MedItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int,
    val minimumQuantity: Int,
    val unit: String,
    val category: String,
    val location: String = "",
    val expirationDate: String? = null,
    val supplier: String = "",
    val isFavorite: Boolean = false
) {
    val isLowStock: Boolean
        get() = quantity <= minimumQuantity

    val stockStatus: String
        get() = when {
            quantity == 0 -> "Esgotado"
            quantity <= minimumQuantity -> "Crítico"
            quantity <= minimumQuantity * 2 -> "Baixo"
            else -> "Normal"
        }
}
