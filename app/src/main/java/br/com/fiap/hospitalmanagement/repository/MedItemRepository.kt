package br.com.fiap.hospitalmanagement.repository

import android.content.Context
import br.com.fiap.hospitalmanagement.dao.MediStockDatabase
import br.com.fiap.hospitalmanagement.model.MedItem

class MedItemRepository(context: Context) {

    private val dao = MediStockDatabase.getDatabase(context).medItemDao()

    fun saveItem(item: MedItem) = dao.save(item)

    fun updateItem(item: MedItem) = dao.update(item)

    fun deleteItem(item: MedItem) = dao.delete(item)

    fun getAllItems(): List<MedItem> = dao.getAllItems()

    fun getItemById(id: Int): MedItem? = dao.getItemById(id)

    fun getLowStockItems(): List<MedItem> = dao.getLowStockItems()

    fun getItemsByCategory(category: String): List<MedItem> = dao.getItemsByCategory(category)

    fun getFavoriteItems(): List<MedItem> = dao.getFavoriteItems()

    fun getLowStockCount(): Int = dao.getLowStockCount()

    fun setFavorite(id: Int, isFavorite: Boolean) = dao.setFavorite(id, isFavorite)

    fun seedInitialData() {
        if (getAllItems().isEmpty()) {
            val items = listOf(
                MedItem(name = "Soro Fisiológico 500ml", quantity = 12, minimumQuantity = 50, unit = "unidades", category = "Soluções", location = "Almoxarifado A", supplier = "ForneceMed"),
                MedItem(name = "Luvas P (caixa)", quantity = 8, minimumQuantity = 20, unit = "caixas", category = "EPIs", location = "Almoxarifado B", expirationDate = "2026-08-01", supplier = "MedSupply"),
                MedItem(name = "Seringa 10ml", quantity = 320, minimumQuantity = 100, unit = "unidades", category = "Descartáveis", location = "Almoxarifado A", supplier = "ForneceMed"),
                MedItem(name = "Álcool 70% 1L", quantity = 45, minimumQuantity = 30, unit = "frascos", category = "Higiene", location = "Almoxarifado C", supplier = "ChemLab"),
                MedItem(name = "Máscara Cirúrgica (cx)", quantity = 5, minimumQuantity = 15, unit = "caixas", category = "EPIs", location = "Almoxarifado B", expirationDate = "2026-12-01", supplier = "MedSupply"),
                MedItem(name = "Cateter IV 20G", quantity = 150, minimumQuantity = 80, unit = "unidades", category = "Descartáveis", location = "Almoxarifado A", supplier = "ForneceMed"),
                MedItem(name = "Dipirona Sódica 500mg", quantity = 200, minimumQuantity = 100, unit = "comprimidos", category = "Medicamentos", location = "Farmácia", expirationDate = "2027-03-01", supplier = "PharmaBR"),
                MedItem(name = "Omeprazol 20mg", quantity = 0, minimumQuantity = 50, unit = "cápsulas", category = "Medicamentos", location = "Farmácia", supplier = "PharmaBR"),
                MedItem(name = "Gaze Estéril (emb)", quantity = 90, minimumQuantity = 40, unit = "embalagens", category = "Curativos", location = "Almoxarifado A", supplier = "MedSupply"),
                MedItem(name = "Termômetro Digital", quantity = 18, minimumQuantity = 10, unit = "unidades", category = "Equipamentos", location = "Almoxarifado C", supplier = "MedTech")
            )
            items.forEach { saveItem(it) }
        }
    }
}