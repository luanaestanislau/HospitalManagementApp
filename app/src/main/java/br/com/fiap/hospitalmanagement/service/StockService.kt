package br.com.fiap.hospitalmanagement.service

import br.com.fiap.hospitalmanagement.model.MedItem
import br.com.fiap.hospitalmanagement.model.StockAlert
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StockService {

    @GET("stock/items")
    fun getAllStockItems(): Call<List<MedItem>>

    @GET("stock/items/low")
    fun getLowStockItems(): Call<List<MedItem>>

    @GET("stock/items/{id}")
    fun getStockItemById(@Path("id") id: Int): Call<MedItem>

    @GET("stock/items/category/{category}")
    fun getItemsByCategory(@Path("category") category: String): Call<List<MedItem>>

    @POST("stock/items")
    suspend fun saveStockItem(@Body item: MedItem): MedItem

    @PUT("stock/items/{id}")
    suspend fun updateStockItem(@Path("id") id: Int, @Body item: MedItem): MedItem

    @GET("stock/alerts")
    fun getAlerts(): Call<List<StockAlert>>

    @GET("stock/forecast")
    fun getDemandForecast(@Query("days") days: Int = 7): Call<List<Map<String, Any>>>
}