package com.example.trinolino.retrofit


import com.example.trinolino.Classi.OrderItem
import com.example.trinolino.Classi.Abbonamenti
import com.example.trinolino.Classi.MenuItem
import com.example.trinolino.Classi.Notizia
import com.example.trinolino.Classi.Offer
import com.example.trinolino.Classi.Schedule
import com.example.trinolino.Classi.Stazioni
import com.example.trinolino.Classi.Ticket
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RailManagerApi {
    @GET("pwm/Users")
    fun getUsers(): Call<JsonArray>

    @GET("pwm/Users/{id}")
    fun getUser(@Path("id") id: Int): Call<JsonObject>

    @POST("pwm/Users")
    fun createUser(@Body user: JsonObject): Call<JsonObject>

    @PUT("pwm/Users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: JsonObject): Call<JsonObject>

    @DELETE("pwm/Users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<JsonObject>

    @POST("pwm/login")
    fun loginUser(@Body credentials: JsonObject): Call<JsonObject>

    @GET("pwm/stations")
    fun getStations(): Call<List<Stazioni>>

    @GET("pwm/schedules")
    fun getSchedules(
        @Query("startStation") startStation: String,
        @Query("endStation") endStation: String,
        @Query("date") date: String
    ): Call<List<Schedule>>

    @POST("pwm/tickets")
    fun createTicket(@Body ticket: JsonObject): Call<JsonObject>

    @GET("pwm/tickets")
    fun getTickets(@Query("user_id") userId: Int): Call<List<Ticket>>

    @GET("pwm/offers")
    fun getOffers(): Call<List<Offer>>

    @POST("pwm/redeem_offer")
    fun redeemOffer(@Body redeemRequest: JsonObject): Call<JsonObject>

    @POST("pwm/update_saldo")
    fun updateSaldo(@Body body: JsonObject): Call<JsonObject>

    @GET("pwm/abbonamenti")
    fun getAbbonamenti(): Call<List<Abbonamenti>>
    @GET("pwm/menu_items")
    fun getMenuItems(): Call<List<MenuItem>>
    @POST("pwm/orders")
    fun createOrder(@Body body: JsonObject): Call<JsonObject>
    @GET("pwm/orders")
    fun getOrder(
        @Query("user_id") userId: Int,
        @Query("ticket_id") ticketId: Int
    ): Call<List<OrderItem>>
    @POST("pwm/update_free_rides")
    fun updateFreeRides(@Body body: JsonObject): Call<JsonObject>

    @POST("pwm/addcorsegratuite")
    fun addCorseGratuite(@Body requestBody: JsonObject): Call<JsonObject>
    @GET("pwm/riscattate")
    fun getUserOffers(@Query("user_id") userId: Int): Call<List<Offer>>
    @POST("pwm/delete_ticket")
    fun deleteTicket(@Body request: JsonObject): Call<JsonObject>

    @DELETE("pwm/redeem_offer")
    fun deleteRedeemedOffer( @Query("user_id") userId: Int, @Query("offer_id") offerId: Int): Call<JsonObject>
    @POST("pwm/check_email")
    fun checkEmail(@Body email: JsonObject): Call<JsonObject>
    @PUT("pwm/tickets/{ticket_id}/validate")
    fun validateTicket(@Path("ticket_id") ticketId: Int, @Body requestBody: JsonObject): Call<JsonObject>
    @GET("pwm/notizie")
    fun getNotizie(): Call<List<Notizia>>


    companion object {
        const val BASE_URL = "http://10.0.2.2:5000/"
        const val USER_URI = "pwm/Users"
    }
}
