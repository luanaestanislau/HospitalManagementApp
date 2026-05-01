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
    abstract fun userDao(): UserDao

    companion object {
        private lateinit var instance: MediStockDatabase

        fun getDatabase(context: Context): MediStockDatabase {
            if (!::instance.isInitialized) {
                val dbName = "medistock_db"
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