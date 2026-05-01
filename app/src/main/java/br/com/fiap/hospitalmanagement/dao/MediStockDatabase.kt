package br.com.fiap.hospitalmanagement.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.hospitalmanagement.model.MedItem
import br.com.fiap.hospitalmanagement.model.User

@Database(entities = [User::class, MedItem::class], version = 3)
abstract class MediStockDatabase : RoomDatabase() {

    abstract fun medItemDao(): MedItemDao

    companion object {
        private lateinit var instance: MediStockDatabase

        fun getDatabase(context: Context): MediStockDatabase {
            if (!::instance.isInitialized) {
                val dbName = "medistock_db"
                // Em ambientes de Preview (layoutlib), o getDatabasePath pode retornar null, 
                // o que causa um NPE interno no FrameworkSQLiteOpenHelper do Room.
                // Usamos inMemoryDatabaseBuilder nesses casos para permitir a renderização do Preview.
                val builder = if (context.getDatabasePath(dbName) == null) {
                    Room.inMemoryDatabaseBuilder(context, MediStockDatabase::class.java)
                } else {
                    Room.databaseBuilder(context, MediStockDatabase::class.java, dbName)
                }
                
                instance = builder
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration(true)
                    .build()
            }
            return instance
        }
    }
}