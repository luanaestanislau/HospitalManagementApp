package br.com.fiap.hospitalmanagement.factory

import br.com.fiap.hospitalmanagement.service.StockService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getStockService(): StockService {
        return retrofit.create(StockService::class.java)
    }

}