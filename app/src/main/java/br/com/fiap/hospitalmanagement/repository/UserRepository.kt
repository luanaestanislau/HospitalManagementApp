package br.com.fiap.hospitalmanagement.repository

import android.content.Context
import br.com.fiap.hospitalmanagement.dao.MediStockDatabase
import br.com.fiap.hospitalmanagement.model.User

class UserRepository(context: Context) {

    private val db = MediStockDatabase.getDatabase(context).userDao()

    fun getUserByEmail(email: String): User? {
        return db.getUserByEmail(email)
    }

    fun save(user: User) {
        db.save(user)
    }
}
