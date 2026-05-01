package br.com.fiap.hospitalmanagement.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.hospitalmanagement.model.MedItem

@Dao
interface MedItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(item: MedItem)

    @Update
    fun update(item: MedItem): Int

    @Delete
    fun delete(item: MedItem): Int

    @Query("SELECT * FROM tb_med_item ORDER BY name ASC")
    fun getAllItems(): List<MedItem>

    @Query("SELECT * FROM tb_med_item WHERE id = :id LIMIT 1")
    fun getItemById(id: Int): MedItem?

    @Query("SELECT * FROM tb_med_item WHERE quantity <= minimumQuantity ORDER BY quantity ASC")
    fun getLowStockItems(): List<MedItem>

    @Query("SELECT * FROM tb_med_item WHERE category = :category ORDER BY name ASC")
    fun getItemsByCategory(category: String): List<MedItem>

    @Query("SELECT * FROM tb_med_item WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteItems(): List<MedItem>

    @Query("SELECT COUNT(*) FROM tb_med_item WHERE quantity <= minimumQuantity")
    fun getLowStockCount(): Int

    @Query("UPDATE tb_med_item SET isFavorite = :isFavorite WHERE id = :id")
    fun setFavorite(id: Int, isFavorite: Boolean): Int
}