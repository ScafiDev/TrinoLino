package com.example.trinolino.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.Offer
import com.example.trinolino.Classi.Schedule
import com.example.trinolino.Object.UsersSession
import com.example.trinolino.retrofit.Client
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SchedulesViewModel : ViewModel() {

    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>> get() = _schedules

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> get() = _offers

    fun fetchSchedules(startStation: String, endStation: String, date: String, context: Context) {
        Client.retrofit.getSchedules(startStation, endStation, date).enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    applyOffersToSchedules(schedules)
                    _schedules.postValue(schedules)
                } else {
                    Toast.makeText(context, "Errore nel caricamento degli orari", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fetchUserOffers(userId: Int, context: Context) {
        Client.retrofit.getUserOffers(userId).enqueue(object : Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                if (response.isSuccessful) {
                    _offers.postValue(response.body())
                    Log.d("SchedulesViewModel", "Offerte caricate correttamente: ${response.body()}")
                } else {
                    Toast.makeText(context, "Errore nel caricamento delle offerte", Toast.LENGTH_SHORT).show()
                    Log.e("SchedulesViewModel", "Errore nel caricamento delle offerte: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                Toast.makeText(context, "Errore di rete nel caricamento delle offerte", Toast.LENGTH_SHORT).show()
                Log.e("SchedulesViewModel", "Errore di rete nel caricamento delle offerte: ${t.localizedMessage}")
            }
        })
    }

    fun updateSaldo(context: Context, amount: Int, callback: () -> Unit) {
        val requestBody = JsonObject().apply {
            addProperty("user_id", UsersSession.userId)
            addProperty("amount", amount)
        }

        Client.retrofit.updateSaldo(requestBody).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback()
                } else {
                    Toast.makeText(context, "Errore durante l'aggiornamento del saldo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete durante l'aggiornamento del saldo", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateFreeRidesAndPurchaseTickets(
        context: Context,
        userId: Int,
        schedule: Schedule,
        ticketsToBuy: Int,
        adultTickets: Int,
        childTickets: Int,
        callback: () -> Unit
    ) {
        val updateRidesJson = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("rides_to_use", ticketsToBuy)
        }

        Client.retrofit.updateFreeRides(updateRidesJson).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val message = response.body()?.get("message")?.asString
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    purchaseTickets(context, userId, schedule, adultTickets, childTickets, callback)
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(context, "Errore nell'aggiornamento delle corse gratuite: $error", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(context, "Errore di rete: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun purchaseTickets(
        context: Context,
        userId: Int,
        schedule: Schedule,
        adultTickets: Int,
        childTickets: Int,
        callback: () -> Unit
    ) {
        val ticketsToBuy = adultTickets + childTickets
        for (i in 1..ticketsToBuy) {
            val ticket = JsonObject().apply {
                addProperty("schedule_id", schedule.schedule_id)
                addProperty("user_id", userId)
                addProperty("seat_number", "A1")
            }

            Log.d("PurchaseTickets", "Creazione ticket $i di $ticketsToBuy")
            Client.retrofit.createTicket(ticket).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if (i == ticketsToBuy) {
                            Toast.makeText(context, "Biglietto acquistato con successo", Toast.LENGTH_SHORT).show()
                            callback()
                        }
                    } else {
                        Toast.makeText(context, "Errore nell'acquisto del biglietto", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, "Errore di rete", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }

    private fun applyOffersToSchedules(schedules: List<Schedule>) {
        val userOffers = _offers.value ?: return

        schedules.forEach { schedule ->
            userOffers.forEach { offer ->
                if (offer.start_station == schedule.start_station && offer.end_station == schedule.end_station) {
                    schedule.price = offer.price
                }
            }
        }
    }

    fun deleteRedeemedOffer(context: Context, userId: Int, offerId: Int, callback: () -> Unit) {
        Client.retrofit.deleteRedeemedOffer(userId, offerId).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback()
                } else {
                    if (context != null) {
                        Toast.makeText(context, "Errore durante la rimozione dell'offerta", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if (context != null) {
                    Toast.makeText(context, "Errore di rete durante la rimozione dell'offerta", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
